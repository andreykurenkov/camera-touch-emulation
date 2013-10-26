package com.camera;

import java.io.IOException;
import java.util.List;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import com.detection.Hand;
import com.detection.HandDetect;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.util.Log;
import android.view.Gravity;
import android.view.TextureView;
import android.view.TextureView.SurfaceTextureListener;
import android.widget.FrameLayout;

public class CamPreview extends TextureView implements SurfaceTextureListener {

	private Camera mCamera;

	public CamPreview(Context context, Camera camera) {
		super(context);
		mCamera = camera;
	}

	@Override
	public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
		Camera.Size previewSize = mCamera.getParameters().getPreviewSize();
		setLayoutParams(new FrameLayout.LayoutParams(
				previewSize.width, previewSize.height, Gravity.CENTER));

		try{
			mCamera.setPreviewTexture(surface);
		} catch (IOException t) {}

		mCamera.startPreview();
		this.setVisibility(INVISIBLE); // Make the surface invisible as soon as it is created
	}

	@Override
	public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
		// Put code here to handle texture size change if you want to
	}

	@Override
	public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
		return true;
	}

	@Override
	public void onSurfaceTextureUpdated(SurfaceTexture surface) {
		// Update your view here!
	}

	public class CamCallback implements Camera.PreviewCallback{
		protected Bitmap bit;
		protected Mat mRgba; 
		
		public void onPreviewFrame(byte[] data, Camera camera){
			bit = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
			mRgba = new Mat();
			Mat mYuv = new Mat(getHeight() + getHeight() / 2, getWidth(), CvType.CV_8UC1);
			
			mYuv.put(0, 0, data);    
			Imgproc.cvtColor(mYuv, mRgba, Imgproc.COLOR_YUV420sp2RGB, 4);
			Utils.matToBitmap(mRgba, bit);

		}
	}
}