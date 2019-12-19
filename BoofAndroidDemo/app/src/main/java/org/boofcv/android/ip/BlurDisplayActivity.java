package org.boofcv.android.ip;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;

import org.boofcv.android.DemoBitmapCamera2Activity;
import org.boofcv.android.DemoProcessingAbstract;
import org.boofcv.android.R;

import boofcv.abst.filter.blur.BlurFilter;
import boofcv.factory.filter.blur.FactoryBlurFilter;
import boofcv.struct.image.GrayU8;

/**
 * Blurs the input video image using different algorithms.
 *
 * @author Peter Abeles
 */
public class BlurDisplayActivity extends DemoBitmapCamera2Activity
		implements AdapterView.OnItemSelectedListener
{
	private static final String TAG = "BlurActivity";
	Spinner spinnerView;

	// amount of blur applied to the image
	int radius;

	public BlurDisplayActivity() {
		super(Resolution.MEDIUM);
		super.changeResolutionOnSlow = true;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


		LayoutInflater inflater = getLayoutInflater();
		LinearLayout controls = (LinearLayout)inflater.inflate(R.layout.blur_controls,null);

		spinnerView = controls.findViewById(R.id.spinner_algs);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
				R.array.blurs, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerView.setAdapter(adapter);
		spinnerView.setOnItemSelectedListener(this);

		SeekBar seek = controls.findViewById(R.id.slider_width);
		radius = seek.getProgress();

		seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				radius = progress;
				synchronized (lockProcessor) {
					if( processor == null) {
						Log.e(TAG,"onProgressChanged() and processing is NULL");
						return;
					}
					if (radius > 0)
						((BlurProcessing)processor).setRadius(radius);
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {}
		});

		setControls(controls);
		activateTouchToShowInput();
	}

	@Override
	public void createNewProcessor() {
		startBlurProcess(spinnerView.getSelectedItemPosition());
	}

	@Override
	public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id ) {
		startBlurProcess(pos);
	}

	private void startBlurProcess(int pos) {
		// not sure what these do if the radius is set to 0
		int radius = Math.max(1,this.radius);
		BlurProcessing processing;
		switch (pos) {
			case 0:
				processing = new BlurProcessing(FactoryBlurFilter.mean(GrayU8.class, radius));
				break;

			case 1:
				processing = new BlurProcessing(FactoryBlurFilter.gaussian(GrayU8.class,-1,radius));
				break;

			case 2:
				processing = new BlurProcessing(FactoryBlurFilter.median(GrayU8.class,radius));
				break;

			default:
				throw new RuntimeException("Unknown");
		}
		processing.setRadius(radius);
		setProcessing(processing);
	}

	@Override
	public void onNothingSelected(AdapterView<?> adapterView) {}

	protected class BlurProcessing extends DemoProcessingAbstract<GrayU8> {
		GrayU8 blurred;
		final BlurFilter<GrayU8> filter;

		public BlurProcessing(BlurFilter<GrayU8> filter) {
			super(GrayU8.class);
			this.filter = filter;
		}

		public void setRadius( int radius ) {
			synchronized ( filter ) {
				filter.setRadius(radius);
			}
		}

		@Override
		public void initialize(int imageWidth, int imageHeight, int sensorOrientation) {
			blurred = new GrayU8(imageWidth,imageHeight);
		}

		@Override
		public void onDraw(Canvas canvas, Matrix imageToView) {
			drawBitmap(canvas,imageToView);
		}

		@Override
		public void process(GrayU8 input) {
			if( radius > 0 ) {
				synchronized ( filter ) {
					filter.process(input, blurred);
				}
				convertToBitmapDisplay(blurred);
			} else {
				convertToBitmapDisplay(input);
			}
		}
	}
}