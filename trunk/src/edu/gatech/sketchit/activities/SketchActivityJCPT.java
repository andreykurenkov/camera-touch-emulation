package edu.gatech.sketchit.activities;

import java.util.HashMap;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.opengles.GL10;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.core.Mat;
import org.opencv.core.Point3;

import com.threed.jpct.Camera;
import com.threed.jpct.FrameBuffer;
import com.threed.jpct.Light;
import com.threed.jpct.Object3D;
import com.threed.jpct.Primitives;
import com.threed.jpct.RGBColor;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.World;
import com.threed.jpct.util.MemoryHelper;

import edu.gatech.sketchit.BottomOverlay;
import edu.gatech.sketchit.R;
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

public class SketchActivityJCPT extends Activity implements CvCameraViewListener2{


	//private MyGLSurfaceView mGLView;
	private static boolean init = false;

	private GLSurfaceView mGLView;
	private MyRenderer renderer = null;
	private FrameBuffer fb = null;
	private World world = null;
	private RGBColor back = new RGBColor(50, 50, 100);

	private static HashMap<finger_id, Finger> detectors;
	private static HandState rightHand, leftHand;
	private CameraBridgeViewBase mOpenCvCameraView;

	private float touchTurn = 0;
	private float touchTurnUp = 0;

	private float xpos = -1;
	private float ypos = -1;

	private Light sun = null;

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
		SketchActivityJCPT.detectors=hashMap;
		if(hashMap.containsKey(finger_id.Pointer_Left) && hashMap.containsKey(finger_id.Thumb_Left)){
			Finger leftMiddle = hashMap.containsKey(finger_id.Middle_Left)?hashMap.get(finger_id.Middle_Left):null;
			leftHand = new HandState(hashMap.get(finger_id.Pointer_Left),hashMap.get(finger_id.Thumb_Left),leftMiddle);
		}
		if(hashMap.containsKey(finger_id.Pointer_Right) && hashMap.containsKey(finger_id.Thumb_Right)){
			Finger rightMiddle = hashMap.containsKey(finger_id.Middle_Right)?hashMap.get(finger_id.Middle_Right):null;
			rightHand = new HandState(hashMap.get(finger_id.Pointer_Right),hashMap.get(finger_id.Thumb_Right),rightMiddle);
		}
		by.startActivity(new Intent(by, SketchActivityJCPT.class));  
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.gl_screen_view);

		mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.gl_screen_view);
		mOpenCvCameraView.setCvCameraViewListener(this);


		mGLView = new GLSurfaceView(getApplication());

		mGLView.setEGLConfigChooser(new GLSurfaceView.EGLConfigChooser() {
			public EGLConfig chooseConfig(EGL10 egl, EGLDisplay display) {
				// Ensure that we get a 16bit framebuffer. Otherwise, we'll fall
				// back to Pixelflinger on some device (read: Samsung I7500)
				int[] attributes = new int[] { EGL10.EGL_DEPTH_SIZE, 16, EGL10.EGL_NONE };
				EGLConfig[] configs = new EGLConfig[1];
				int[] result = new int[1];
				egl.eglChooseConfig(display, attributes, configs, 1, result);
				return configs[0];
			}
		});

		renderer = new MyRenderer();
		mGLView.setRenderer(renderer);
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

	public boolean onTouchEvent(MotionEvent me) {

		if (me.getAction() == MotionEvent.ACTION_DOWN) {
			xpos = me.getX();
			ypos = me.getY();
			return true;
		}

		if (me.getAction() == MotionEvent.ACTION_UP) {
			xpos = -1;
			ypos = -1;
			touchTurn = 0;
			touchTurnUp = 0;
			return true;
		}

		if (me.getAction() == MotionEvent.ACTION_MOVE) {
			float xd = me.getX() - xpos;
			float yd = me.getY() - ypos;

			xpos = me.getX();
			ypos = me.getY();

			touchTurn = xd / -100f;
			touchTurnUp = yd / -100f;
			return true;
		}

		return super.onTouchEvent(me);
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
			if(cursor!=null)
					renderer.moveCursor1(-cursor.x/100, cursor.y/50, cursor.z);
			//Log.i("Sketch",cursor.toString());
			if(clicked!=null){
				Log.i("CLICK",clicked.toString());
				long downTime = rightHand.getTimeOfDown();
				//MotionEvent event = MotionEvent.obtain(downTime, eventTime, action, x, y, metaState)
			}


		}
		return mRgba;
	}

	protected boolean isFullscreenOpaque() {
		return true;
	}

	class MyRenderer implements GLSurfaceView.Renderer {
		private Object3D cursor1,cursor2;


		public MyRenderer() {


		}

		public void moveCursor1(double x, double y, double z) {
			Log.i("sketch",x+" "+y+" "+z);
			SimpleVector at = cursor1.getTranslation();
			cursor1.translate(-at.x,-at.y,-at.z);
			cursor1.translate((float)x,(float)y,(float)z);

		}

		public void onSurfaceChanged(GL10 gl, int w, int h) {
			fb = new FrameBuffer(gl, w, h);

		}

		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			if (fb != null) {
				fb.dispose();
			}


			world = new World();
			world.setAmbientLight(20, 20, 20);

			cursor1 = Primitives.getCone(3);
			cursor1.setAdditionalColor(255, 0, 0);
			cursor1.strip();
			cursor1.build();

			world.addObject(cursor1);

			sun = new Light(world);
			sun.setIntensity(250, 250, 250);

			Camera cam = world.getCamera();
			cam.moveCamera(Camera.CAMERA_MOVEOUT, 50);
			cam.lookAt(cursor1.getTransformedCenter());


			MemoryHelper.compact();

		}

		public void onDrawFrame(GL10 gl) {
			fb.clear(back);
			world.renderScene(fb);
			world.draw(fb);
			fb.display();

		}
	}
}
