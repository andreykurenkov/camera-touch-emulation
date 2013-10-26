package com.activities;

import java.util.ArrayList;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import com.camera.CamPreview;
import com.camera.CamPreview.CamCallback;
import com.detection.Hand;
import com.detection.HandDetect;
import com.example.magic.R;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Point;
import android.hardware.Camera;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class CombinedTestActivity extends Activity  {
	private ImageView image;
	private DrawPointerView view;
	private BaseLoaderCallback  mLoaderCallback = new BaseLoaderCallback(this) {
		@Override
		public void onManagerConnected(int status) {
			switch (status) {
			case LoaderCallbackInterface.SUCCESS:
			{ 
				System.loadLibrary("magic");
				loaded = true;
				HandDetect.init(CombinedTestActivity.this);
				Log.i("IntroActivity", "OpenCV loaded successfully");
				//mOpenCvCameraView.enableView();
			} break;
			default:
			{
				super.onManagerConnected(status);
			} break;
			}
		}
	};
	
public boolean loaded = false;
private CamPreview preview;

	public class CamUpdate extends CamCallback{

		public CamUpdate(){
			preview.super();
		}

		public void onPreviewFrame(byte[] data, Camera camera){
			if(loaded){
				super.onPreviewFrame(data, camera);
				view.points.clear();
				view.invalidate();
				
				image.setImageBitmap(bit);
				
				
			}
		}

	}

	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		setContentView(R.layout.combined_test_view);
		image = (ImageView) this.findViewById(R.id.demo_view);
		// Setup the camera and the preview object
		Camera mCamera = Camera.open(0);
		preview = new CamPreview(this,mCamera);
		preview.setSurfaceTextureListener(preview);

		// Connect the preview object to a FrameLayout in your UI
		// You'll have to create a FrameLayout object in your UI to place this preview in
		FrameLayout frame = (FrameLayout) findViewById(R.id.cameraView); 
		frame.addView(preview);

		// Attach a callback for preview
		CamUpdate camCallback = new CamUpdate();
		mCamera.setPreviewCallback(camCallback);
		view = new DrawPointerView(this);
		addContentView(view,new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
	}


	@Override
	public void onResume()
	{
		super.onResume();
		OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_3, this, mLoaderCallback);
	}

	@SuppressWarnings("unused")
	private Scalar convertScalarHsv2Rgba(Scalar hsvColor) {
		Mat pointMatRgba = new Mat();
		Mat pointMatHsv = new Mat(1, 1, CvType.CV_8UC3, hsvColor);
		Imgproc.cvtColor(pointMatHsv, pointMatRgba, Imgproc.COLOR_HSV2RGB_FULL, 4);

		return new Scalar(pointMatRgba.get(0, 0));
	}
}
