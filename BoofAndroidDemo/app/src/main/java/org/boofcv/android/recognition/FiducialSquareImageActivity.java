package org.boofcv.android.recognition;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import org.boofcv.android.R;

import java.util.List;

import boofcv.abst.fiducial.FiducialDetector;
import boofcv.abst.fiducial.SquareImage_to_FiducialDetector;
import boofcv.alg.filter.binary.BinaryImageOps;
import boofcv.alg.misc.PixelMath;
import boofcv.factory.fiducial.ConfigFiducialImage;
import boofcv.factory.fiducial.FactoryFiducial;
import boofcv.factory.filter.binary.ConfigThreshold;
import boofcv.factory.filter.binary.ThresholdType;
import boofcv.struct.image.GrayU8;

/**
 * Detects and shows square binary fiducials
 *
 * @author Peter Abeles
 */
public class FiducialSquareImageActivity extends FiducialSquareActivity
{
	FiducialManager manager;
	List<FiducialManager.Info> list;

	public FiducialSquareImageActivity() {
		super(FiducialSquareImageHelpActivity.class);
		layout = R.layout.fiducial_image_controls;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// hold off until it has a chance to load the fiducial camera
		detectFiducial = false;
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onResume() {
		synchronized (this) {
			manager = new FiducialManager(this);
			manager.loadList();
			list = manager.copyList();
		}
		if( list.size() == 0 ) {
			textToDraw = "ADD FIDUCIALS!";
		} else {
			textToDraw = null;
		}
		super.onResume();

		// The camera might have initialized before this function is called. In that scenario
		// it didn't have any markers loaded yet
		detectFiducial = true;
		if( cameraInitialized ) { // not locking here since only one variable needs to be read
			createNewProcessor();
		}
	}

	public void pressedLibrary( View view ) {
		Intent intent = new Intent(this, FiducialImageLibraryAcitivity.class );
		startActivity(intent);
	}

	@Override
	protected FiducialDetector<GrayU8> createDetector() {

		SquareImage_to_FiducialDetector<GrayU8> detector;
		ConfigFiducialImage config = new ConfigFiducialImage();

		synchronized ( lock ) {
			ConfigThreshold configThreshold;
			if (robust) {
				configThreshold = ConfigThreshold.local(ThresholdType.LOCAL_MEAN, 13);
			} else {
				configThreshold = ConfigThreshold.fixed(binaryThreshold);
			}
			detector = FactoryFiducial.squareImage(config, configThreshold, GrayU8.class);
		}

		// need to check for this just in case my fix earlier doesn't work
		// a detector is always created after the manager has been loaded
		synchronized (this) {
			if (list != null && manager != null) {
				for (int i = 0; i < list.size(); i++) {
					GrayU8 binary = manager.loadBinaryImage(list.get(i).id);
					BinaryImageOps.invert(binary, binary);
					PixelMath.multiply(binary, 255, 0, 255, binary);
					detector.addPatternImage(binary, 125, list.get(i).sideLength);
				}
			}
		}

		return detector;
	}
}
