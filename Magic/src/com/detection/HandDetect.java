package com.detection;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.*;
import org.opencv.android.CameraBridgeViewBase.*;

import android.R;
import android.util.Log;
import android.content.*;
import android.content.res.*;

import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class HandDetect {
	
	private static CascadeClassifier fistClassifier;
	private static CascadeClassifier palmClassifier;
	
	public static void init(Context context ){

        try {
	        InputStream is = context.getAssets().open("fist.xml");
	        File cascadeDir = context.getDir("cascade", Context.MODE_PRIVATE);
	        File mCascadeFile = new File(cascadeDir, "fist.xml");
	        FileOutputStream os = new FileOutputStream(mCascadeFile);
	
	        byte[] buffer = new byte[4096];
	        int bytesRead;
	        while ((bytesRead = is.read(buffer)) != -1) {
	            os.write(buffer, 0, bytesRead);
	        }
	        is.close();
	        os.close();
	
	        fistClassifier = new CascadeClassifier(mCascadeFile.getAbsolutePath());
	        if (fistClassifier.empty()) {
	            Log.d( "Failed to load cascade classifier", "ERROR");
	            fistClassifier = null;
	        } else
	            Log.d("Loaded cascade classifier from: " , mCascadeFile.getAbsolutePath());
	
	
	        } catch (IOException e) {
	            e.printStackTrace();
	            Log.d( "Failed to load cascade. Exception thrown: ", e.getMessage());
	       }
        
        

        try {
	        InputStream is = context.getAssets().open("palm.xml");
	        File cascadeDir = context.getDir("cascade", Context.MODE_PRIVATE);
	        File mCascadeFile = new File(cascadeDir, "palm.xml");
	        FileOutputStream os = new FileOutputStream(mCascadeFile);
	
	        byte[] buffer = new byte[4096];
	        int bytesRead;
	        while ((bytesRead = is.read(buffer)) != -1) {
	            os.write(buffer, 0, bytesRead);
	        }
	        is.close();
	        os.close();
	
	        palmClassifier = new CascadeClassifier(mCascadeFile.getAbsolutePath());
	        if (palmClassifier.empty()) {
	            Log.d( "Failed to load cascade classifier", "ERROR");
	            palmClassifier = null;
	        } else
	            Log.d("Loaded cascade classifier from: " , mCascadeFile.getAbsolutePath());
	
	        } catch (IOException e) {
	            e.printStackTrace();
	            Log.d( "Failed to load cascade. Exception thrown: ", e.getMessage());
	       }
		
	}
	
	public static List<Hand> findHands(CvCameraViewFrame src){
		return findHands( src.rgba().clone() );
	}
	
	private static List<Hand> findHands(Mat src){
		ArrayList<Hand> foo = new ArrayList<Hand>();

		Log.d("Classifier Empty: " , fistClassifier.empty()+"");
		Imgproc.GaussianBlur(src,  src, new Size(5,5), 1);
		Imgproc.cvtColor(src, src, Imgproc.COLOR_BGR2GRAY);

	    src.convertTo(src, CvType.CV_8U);
	    MatOfRect fistLocations = new MatOfRect();
	    MatOfRect palmLocations = new MatOfRect();

	    fistClassifier.detectMultiScale(src, fistLocations);
	    palmClassifier.detectMultiScale(src, palmLocations);
	    
	    for (Rect rect : fistLocations.toArray()) {
	    	foo.add(new Hand( rect, false));
	        Core.rectangle(src, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0));
	        
	    }
	    
	    for( Rect rect : palmLocations.toArray()) {
	    	foo.add(new Hand(rect, true));
	    	Core.rectangle(src, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y+rect.height), new Scalar(255, 0 ,0));
	        
	    }
	    
		return foo;
		
	}
	
	
	
	public static Point centerOf(Rect rect){
		return new Point(rect.x + rect.width/2, rect.y+rect.height );
	}
	
	
}
