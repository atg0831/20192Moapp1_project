package org.boofcv.android.recognition;

import android.os.Bundle;

import boofcv.abst.fiducial.FiducialDetector;
import boofcv.abst.fiducial.calib.CalibrationPatterns;
import boofcv.factory.fiducial.FactoryFiducial;
import boofcv.struct.image.GrayU8;

/**
 * Detects calibration target fiducials
 */
public class FiducialCalibrationActivity extends FiducialSquareActivity {

	public static ConfigAllCalibration cc = new ConfigAllCalibration();

	public FiducialCalibrationActivity() {
		super(FiducialCalibrationHelpActivity.class);
		disableControls = true;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// don't start processing fiducials until the user has selected the specifics
		detectFiducial = false;
		super.onCreate(savedInstanceState);

		SelectCalibrationFiducial dialog = new SelectCalibrationFiducial(cc);
		dialog.show(this, ()->{
			detectFiducial=true;//needs to be before createNewProcessor
			createNewProcessor();
		});
	}

	@Override
	protected FiducialDetector<GrayU8> createDetector() {

		if( cc.targetType == CalibrationPatterns.CHESSBOARD ) {
			return FactoryFiducial.calibChessboard(null,cc.chessboard, GrayU8.class);
		} else if( cc.targetType == CalibrationPatterns.SQUARE_GRID ) {
			return FactoryFiducial.calibSquareGrid(null,cc.squareGrid, GrayU8.class);
		} else if( cc.targetType == CalibrationPatterns.CIRCLE_HEXAGONAL ) {
			return FactoryFiducial.calibCircleHexagonalGrid(null,cc.hexagonal, GrayU8.class);
		} else if( cc.targetType == CalibrationPatterns.CIRCLE_GRID ) {
			return FactoryFiducial.calibCircleRegularGrid(null,cc.circleGrid, GrayU8.class);
		} else {
			throw new RuntimeException("Unknown");
		}
	}
}
