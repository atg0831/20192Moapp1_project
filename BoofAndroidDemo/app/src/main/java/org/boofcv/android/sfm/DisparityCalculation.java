package org.boofcv.android.sfm;

import android.util.Log;

import org.ddogleg.fitting.modelset.ModelMatcher;
import org.ddogleg.struct.FastQueue;
import org.ejml.data.DMatrixRMaj;
import org.ejml.data.FMatrixRMaj;
import org.ejml.ops.ConvertMatrixData;

import java.util.ArrayList;
import java.util.List;

import boofcv.abst.feature.associate.AssociateDescription;
import boofcv.abst.feature.detdesc.DetectDescribePoint;
import boofcv.abst.feature.disparity.StereoDisparity;
import boofcv.alg.descriptor.UtilFeature;
import boofcv.alg.distort.ImageDistort;
import boofcv.alg.filter.derivative.DerivativeLaplacian;
import boofcv.alg.geo.PerspectiveOps;
import boofcv.alg.geo.RectifyImageOps;
import boofcv.alg.geo.rectify.RectifyCalibrated;
import boofcv.alg.geo.robust.ModelMatcherMultiview;
import boofcv.alg.misc.ImageMiscOps;
import boofcv.factory.distort.LensDistortionFactory;
import boofcv.factory.geo.ConfigEssential;
import boofcv.factory.geo.ConfigRansac;
import boofcv.factory.geo.EnumEssential;
import boofcv.factory.geo.FactoryMultiViewRobust;
import boofcv.struct.border.BorderType;
import boofcv.struct.calib.CameraPinholeBrown;
import boofcv.struct.distort.Point2Transform2_F64;
import boofcv.struct.feature.AssociatedIndex;
import boofcv.struct.feature.TupleDesc;
import boofcv.struct.geo.AssociatedPair;
import boofcv.struct.image.GrayF32;
import boofcv.struct.image.ImageType;
import georegression.struct.point.Point2D_F64;
import georegression.struct.se.Se3_F64;

/**
 * Computes the disparity image from two views.  Features are first associated between the two images, image motion
 * found, rectification and dense stereo calculation.
 *
 * @author Peter Abeles
 */
public class DisparityCalculation<Desc extends TupleDesc> {

	DetectDescribePoint<GrayF32,Desc> detDesc;
	AssociateDescription<Desc> associate;
	CameraPinholeBrown intrinsic;

	StereoDisparity<GrayF32, GrayF32> disparityAlg;

	FastQueue<Desc> listSrc;
	FastQueue<Desc> listDst;
	FastQueue<Point2D_F64> locationSrc = new FastQueue<Point2D_F64>(Point2D_F64.class,true);
	FastQueue<Point2D_F64> locationDst = new FastQueue<Point2D_F64>(Point2D_F64.class,true);

	List<AssociatedPair> inliersPixel;

	boolean directionLeftToRight;

	GrayF32 distortedLeft;
	GrayF32 distortedRight;
	GrayF32 rectifiedLeft;
	GrayF32 rectifiedRight;

	// Laplacian that has been applied to rectified images
	GrayF32 edgeLeft;
	GrayF32 edgeRight;

	// has the disparity been computed
	boolean computedDisparity = false;

	public DisparityCalculation(DetectDescribePoint<GrayF32, Desc> detDesc,
								AssociateDescription<Desc> associate ,
								CameraPinholeBrown intrinsic ) {
		this.detDesc = detDesc;
		this.associate = associate;
		this.intrinsic = intrinsic;

		listSrc = UtilFeature.createQueue(detDesc, 10);
		listDst = UtilFeature.createQueue(detDesc, 10);
	}

	public void setDisparityAlg(StereoDisparity<GrayF32, GrayF32> disparityAlg) {
		this.disparityAlg = disparityAlg;
	}

	public void init( int width , int height ) {
		distortedLeft = new GrayF32(width,height);
		distortedRight = new GrayF32(width,height);
		rectifiedLeft = new GrayF32(width,height);
		rectifiedRight = new GrayF32(width,height);
		edgeLeft = new GrayF32(width,height);
		edgeRight = new GrayF32(width,height);
	}

	public void setSource( GrayF32 image ) {
		distortedLeft.setTo(image);
		detDesc.detect(image);
		describeImage(listSrc, locationSrc);
		associate.setSource(listSrc);
	}

	public void setDestination( GrayF32 image ) {
		distortedRight.setTo(image);
		detDesc.detect(image);
		describeImage(listDst, locationDst);
		associate.setDestination(listDst);

	}

	private void describeImage(FastQueue<Desc> listDesc, FastQueue<Point2D_F64> listLoc) {
		listDesc.reset();
		listLoc.reset();
		int N = detDesc.getNumberOfFeatures();
		for( int i = 0; i < N; i++ ) {
			listLoc.grow().set(detDesc.getLocation(i));
			listDesc.grow().setTo(detDesc.getDescription(i));
		}
	}

	/**
	 * Associates image features, computes camera motion, and rectifies images.
	 *
	 * @return true it was able to rectify the input images or false if not
	 */
	public boolean rectifyImage() {
		computedDisparity = false;

		associate.associate();
		List<AssociatedPair> pairs = convertToNormalizedCoordinates();

		Se3_F64 leftToRight = estimateCameraMotion(pairs);

		if( leftToRight == null ) {
			Log.e("disparity","estimate motion failed");
			Log.e("disparity","  left.size = "+locationSrc.size());
			Log.e("disparity","  right.size = "+locationDst.size());
			Log.e("disparity", "  associated size = " + associate.getMatches().size());
			Log.e("disparity","  pairs.size = "+pairs.size());

			return false;
		} else if( leftToRight.getT().x > 0 ) {
			// the user took a picture from right to left instead of left to right
			// so now everything needs to be swapped
			leftToRight = leftToRight.invert(null);
			GrayF32 tmp = distortedLeft;
			distortedLeft = distortedRight;
			distortedRight = tmp;
			tmp = edgeLeft;
			edgeLeft = edgeRight;
			edgeRight = tmp;
			directionLeftToRight = false;
		} else {
			directionLeftToRight = true;
		}

		DMatrixRMaj rectifiedK = new DMatrixRMaj(3,3);
		rectifyImages(leftToRight, rectifiedK);

		return true;
	}

	/**
	 * Computes the disparity between the two rectified images
	 */
	public void computeDisparity() {
		if( disparityAlg == null )
			return;

		disparityAlg.process(edgeLeft, edgeRight);
		computedDisparity = true;
	}

	/**
	 * Convert a set of associated point features from pixel coordinates into normalized image coordinates.
	 */
	public List<AssociatedPair> convertToNormalizedCoordinates() {

		Point2Transform2_F64 tran = LensDistortionFactory.narrow(intrinsic).undistort_F64(true,false);

		List<AssociatedPair> calibratedFeatures = new ArrayList<>();

		FastQueue<AssociatedIndex> matches = associate.getMatches();
		for( AssociatedIndex a : matches.toList() ) {
			Point2D_F64 p1 = locationSrc.get( a.src );
			Point2D_F64 p2 = locationDst.get( a.dst );

			AssociatedPair c = new AssociatedPair();

			tran.compute(p1.x, p1.y, c.p1);
			tran.compute(p2.x, p2.y, c.p2);

			calibratedFeatures.add(c);
		}

		return calibratedFeatures;
	}

	/**
	 * Estimates image motion up to a scale factor
	 * @param matchedNorm List of associated features in normalized image coordinates
	 */
	public volatile int numInside = 0;
	public Se3_F64 estimateCameraMotion( List<AssociatedPair> matchedNorm )
	{
		numInside++;
		ConfigEssential configEssential = new ConfigEssential();
		configEssential.which = EnumEssential.NISTER_5;
		configEssential.numResolve = 5;

		ConfigRansac configRansac = new ConfigRansac();
		configRansac.maxIterations = 400;
		configRansac.inlierThreshold = 0.15;
		ModelMatcherMultiview<Se3_F64, AssociatedPair> epipolarMotion =
				FactoryMultiViewRobust.baselineRansac(configEssential,configRansac);
		epipolarMotion.setIntrinsic(0,intrinsic);
		epipolarMotion.setIntrinsic(1,intrinsic);

		if (!epipolarMotion.process(matchedNorm)) {
			numInside--;
			return null;
		}

		createInliersList(epipolarMotion);

		numInside--;
		return epipolarMotion.getModelParameters();
	}

	/**
	 * Save a list of inliers in pixel coordinates
	 */
	private void createInliersList( ModelMatcher<Se3_F64, AssociatedPair> epipolarMotion ) {
		inliersPixel = new ArrayList<>();

		FastQueue<AssociatedIndex> matches = associate.getMatches();

		int N = epipolarMotion.getMatchSet().size();
		for( int i = 0; i < N; i++ ) {

			AssociatedIndex a = matches.get( epipolarMotion.getInputIndex(i));

			Point2D_F64 p1 = locationSrc.get( a.src );
			Point2D_F64 p2 = locationDst.get( a.dst );

			inliersPixel.add( new AssociatedPair(p1,p2));
		}
	}

	/**
	 * Remove lens distortion and rectify stereo images
	 *
	 * @param leftToRight    Camera motion from left to right
	 * @param rectifiedK     Output camera calibration matrix for rectified camera
	 */
	public void rectifyImages(Se3_F64 leftToRight,
							  DMatrixRMaj rectifiedK) {
		RectifyCalibrated rectifyAlg = RectifyImageOps.createCalibrated();

		// original camera calibration matrices
		DMatrixRMaj K = PerspectiveOps.pinholeToMatrix(intrinsic, (DMatrixRMaj)null);

		rectifyAlg.process(K, new Se3_F64(), K, leftToRight);

		// rectification matrix for each image
		DMatrixRMaj rect1 = rectifyAlg.getRect1();
		DMatrixRMaj rect2 = rectifyAlg.getRect2();

		FMatrixRMaj rect1_f = new FMatrixRMaj(3,3);
		FMatrixRMaj rect2_f = new FMatrixRMaj(3,3);

		ConvertMatrixData.convert(rect1,rect1_f);
		ConvertMatrixData.convert(rect2,rect2_f);

		// New calibration matrix,
		rectifiedK.set(rectifyAlg.getCalibrationMatrix());

		// Adjust the rectification to make the view area more useful
//		RectifyImageOps.allInsideLeft(intrinsic, rect1, rect2, rectifiedK);

		// undistorted and rectify images
		ImageDistort<GrayF32,GrayF32> distortLeft =
				RectifyImageOps.rectifyImage(intrinsic, rect1_f, BorderType.ZERO, ImageType.single(GrayF32.class));
		ImageDistort<GrayF32,GrayF32> distortRight =
				RectifyImageOps.rectifyImage(intrinsic, rect2_f, BorderType.ZERO, ImageType.single(GrayF32.class));

		// Apply the Laplacian for some lighting invariance
		ImageMiscOps.fill(rectifiedLeft,0);
		distortLeft.apply(distortedLeft, rectifiedLeft);
		DerivativeLaplacian.process(rectifiedLeft,edgeLeft,null);

		ImageMiscOps.fill(rectifiedRight, 0);
		distortRight.apply(distortedRight, rectifiedRight);
		DerivativeLaplacian.process(rectifiedRight,edgeRight,null);
	}

	public List<AssociatedPair> getInliersPixel() {
		return inliersPixel;
	}

	public GrayF32 getDisparity() {
		return disparityAlg.getDisparity();
	}

	public StereoDisparity<GrayF32, GrayF32> getDisparityAlg() {
		return disparityAlg;
	}

	public boolean isDisparityAvailable() {
		return computedDisparity;
	}

	public boolean isDirectionLeftToRight() {
		return directionLeftToRight;
	}
}
