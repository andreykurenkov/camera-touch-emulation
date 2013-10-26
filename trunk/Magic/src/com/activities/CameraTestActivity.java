package com.activities;

import java.util.List;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.camera.CamPreview;
import com.camera.CamPreview.CamCallback;
import com.detection.Hand;
import com.detection.HandDetect;
import com.example.magic.R;

public class CameraTestActivity extends Activity  {
	/** Called when the activity is first created. */
	private boolean loaded = false;

	private BaseLoaderCallback  mLoaderCallback = new BaseLoaderCallback(this) {
		@Override
		public void onManagerConnected(int status) {
			switch (status) {
			case LoaderCallbackInterface.SUCCESS:
			{ 
				loaded = true;
				System.loadLibrary("magic");
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

	private CamPreview preview;
	private ImageView view;

	public class CamUpdate extends CamCallback{

		public CamUpdate(){
			preview.super();
		}

		public void onPreviewFrame(byte[] data, Camera camera){
			if(loaded){
				super.onPreviewFrame(data, camera);
				view.setImageBitmap(bit);
				view.invalidate();
			}
		}

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		setContentView(R.layout.camera_test_view);
		view = (ImageView)this.findViewById(R.id.test_camera_out);

		// Setup the camera and the preview object
		Camera mCamera = Camera.open(0);
		preview = new CamPreview(this,mCamera);
		preview.setSurfaceTextureListener(preview);

		// Connect the preview object to a FrameLayout in your UI
		// You'll have to create a FrameLayout object in your UI to place this preview in
		FrameLayout frame = (FrameLayout) findViewById(R.id.test_camera_view); 
		frame.addView(preview);

		// Attach a callback for preview
		CamCallback CamUpdate = new CamUpdate();
		mCamera.setPreviewCallback(CamUpdate);
	}

	@Override
	public void onResume()
	{
		super.onResume();
		OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_3, this, mLoaderCallback);
	}	
}

