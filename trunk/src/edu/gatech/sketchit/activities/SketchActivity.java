package edu.gatech.sketchit.activities;

import java.util.HashMap;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.core.Mat;
import org.opencv.core.Point3;

import edu.gatech.sketchit.BottomOverlay;
import edu.gatech.sketchit.MyGLRenderer;
import edu.gatech.sketchit.R;
import edu.gatech.sketchit.cv.ColorDetector;
import edu.gatech.sketchit.shapes.Circle;
import edu.gatech.sketchit.shapes.*;
import edu.gatech.sketchit.sketch.Finger;
import edu.gatech.sketchit.sketch.Finger.finger_id;
import edu.gatech.sketchit.sketch.HandState;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.RelativeLayout;

public class SketchActivity extends Activity implements CvCameraViewListener2{
	private MyGLSurfaceView mGLView;
	private static HashMap<finger_id, Finger> detectors;
	private static HandState rightHand, leftHand;
	private CameraBridgeViewBase mOpenCvCameraView;


	private BaseLoaderCallback  mLoaderCallback = new BaseLoaderCallback(this) {
		@Override
		public void onManagerConnected(int status) {
			switch (status) {
			case LoaderCallbackInterface.SUCCESS:
			{
				Log.i("SketchActivity", "OpenCV loaded successfully");
				mOpenCvCameraView.enableView();
			} break;
			default:
			{
				super.onManagerConnected(status);
			} break;
			}
		}
	};
	public static void launch(Context by, HashMap<finger_id, Finger> hashMap){
		SketchActivity.detectors=hashMap;
		if(hashMap.containsKey(finger_id.Pointer_Left) && hashMap.containsKey(finger_id.Thumb_Left)){
			Finger leftMiddle = hashMap.containsKey(finger_id.Middle_Left)?hashMap.get(finger_id.Middle_Left):null;
			leftHand = new HandState(hashMap.get(finger_id.Pointer_Left),hashMap.get(finger_id.Thumb_Left),leftMiddle);
		}
		if(hashMap.containsKey(finger_id.Pointer_Right) && hashMap.containsKey(finger_id.Thumb_Right)){
			Finger rightMiddle = hashMap.containsKey(finger_id.Middle_Right)?hashMap.get(finger_id.Middle_Right):null;
			rightHand = new HandState(hashMap.get(finger_id.Pointer_Right),hashMap.get(finger_id.Thumb_Right),rightMiddle);
		}
		by.startActivity(new Intent(by, SketchActivity.class));  
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.gl_screen_view);

		mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.gl_screen_view);
		mOpenCvCameraView.setCvCameraViewListener(this);
		mGLView = new MyGLSurfaceView(this);
		addContentView(mGLView,new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));

		RelativeLayout rl = new RelativeLayout(this);
		BottomOverlay bo = new BottomOverlay(this);
		RelativeLayout.LayoutParams layout_main = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 150);
		layout_main.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		bo.setLayoutParams(layout_main);
		rl.addView(bo);

		addContentView(rl,new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}

	@Override
	public void onPause()
	{
		super.onPause();
		if (mOpenCvCameraView != null)
			mOpenCvCameraView.disableView();
	}

	@Override
	public void onResume()
	{
		super.onResume();
		OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_3, this, mLoaderCallback);
	}

	@Override
	public void onCameraViewStarted(int width, int height) {

		// TODO Auto-generated method stub

	}

	@Override
	public void onCameraViewStopped() {
		// TODO Auto-generated method stub

	}

	@Override
	public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
		Mat mRgba = inputFrame.rgba();
		if(leftHand!=null){
			Point3 clicked = leftHand.updateClickedState(mRgba);
		}
		if(rightHand!=null){
			Point3 clicked = rightHand.updateClickedState(mRgba);
			Point3 cursor = rightHand.getPointing().getColorDetector().detectBiggestBlob(mRgba);
			Point3 cursor2 = rightHand.getThumb().getColorDetector().detectBiggestBlob(mRgba);
			//Log.i("sketch",cursor+" "+cursor2);
			if(cursor!=null){
				//Log.i("Sketch",cursor.toString());
				mGLView.getRenderer().setCursor1(cursor);
				mGLView.getRenderer().setCursor2(cursor2);
				if(clicked!=null){
					Log.i("CLICK",clicked.toString());
					long downTime = rightHand.getTimeOfDown();
					//MotionEvent event = MotionEvent.obtain(downTime, eventTime, action, x, y, metaState)
				}
			}
		}
		return mRgba;
	}
}


class MyGLSurfaceView extends GLSurfaceView {
	private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
	private float mPreviousX;
	private float mPreviousY;
	private float mPreviousZ;
	private final MyGLRenderer mRenderer;
	private boolean generated;
	private boolean zoomMode;

	public MyGLSurfaceView(Context context) {
		super(context);
		setEGLContextClientVersion(2);
		super.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
		mRenderer = new MyGLRenderer();
		setRenderer(mRenderer);
		setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

	}

	public void setZoomMode(boolean on) {
		zoomMode = on;
	}

	private void generate() {
		//		Point3[] rand = Shape.randomShape(4);
		//		Shape r = new Rectangle(rand[0], rand[1], rand[2], rand[3]);
		//		mRenderer.addShape(r);
		//		requestRender();

		for(int i=0;i<150;i++) {
			Point3[] rand = Shape.randomShape(2);
			Shape r = new Line(rand[0], rand[1]);
			mRenderer.addShape(r);
		}
		requestRender();
//		for(int i=0;i<5;i++) {
//			Point3[] rand = Shape.randomShape(4);
//			Shape r = new Rectangle(rand[0], rand[1], rand[2], rand[3]);
//			mRenderer.addShape(r);
//		}
		//Shape r = new Circle(new Point3(0, 0, 0), 3f);
	//	mRenderer.addShape(r);
	//	requestRender();
	}

	@Override
	public boolean onTouchEvent(MotionEvent e) {
		if(!generated) {
			generated = true;
			generate();
			//			return true;
		}

		float x = e.getX();
		float y = e.getY();

		switch (e.getAction()) {
		case MotionEvent.ACTION_MOVE:

			float dx = x - mPreviousX;
			float dy = y - mPreviousY;
            mRenderer.mAngleX += (dx) * TOUCH_SCALE_FACTOR; 
			if(zoomMode) {
				mRenderer.zoom(dy);
			}
			else {
	            mRenderer.mAngleY += (dy) * TOUCH_SCALE_FACTOR;  // = 180.0f / 320
			}
			requestRender();
		}


		mPreviousX = x;
		mPreviousY = y;
		return true;
	}	

	public MyGLRenderer getRenderer(){
		return mRenderer;
	}
}
