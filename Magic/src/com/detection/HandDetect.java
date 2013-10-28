package com.detection;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.*;
import org.opencv.android.CameraBridgeViewBase.*;

import android.util.Log;
import android.content.*;

import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class HandDetect {
	
	private static CascadeClassifier fistClassifier;
	private static CascadeClassifier palmClassifier;
	
	public static void init(Context context ){

		loadClassifier(context, "palm.xml");
		loadClassifier(context, "fist.xml");
		
	}
	
	
	public static void loadClassifier(Context context, String fileName){
        try {
	        InputStream is = context.getAssets().open(fileName);
	        File cascadeDir = context.getDir("cascade", Context.MODE_PRIVATE);
	        File mCascadeFile = new File(cascadeDir, fileName);
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
		return findHands( src.rgba());
	}
	
	public static List<Hand> findHands(Mat src){
		return findHands(src,false);
	}
	
	public static List<Hand> findHands(Mat src, boolean fast){
		src = src.clone();
		ArrayList<Hand> foo = new ArrayList<Hand>();

		Imgproc.GaussianBlur(src,  src, new Size(5,5), 1);
		Imgproc.cvtColor(src, src, Imgproc.COLOR_BGR2GRAY);

	    src.convertTo(src, CvType.CV_8U);
	    MatOfRect fistLocations = new MatOfRect();
	    MatOfRect palmLocations = new MatOfRect();
	    
	    int width = src.width();
	    int height = src.height();
	    
	    if(width>height){
	    	double ratioWidthToMax = 0.25;
	    	double ratioWidthMaxToMin = 0.64;
	    	double ratioWidthToHeight = 1.5;
		    double scale = 1.015;
		    int neightbors = 1;
		    
		    double maxWidth = width * ratioWidthToMax;
		    double minWidth = maxWidth * ratioWidthMaxToMin;
		    double maxHeight = maxWidth * ratioWidthToHeight;
		    double minHeight = minWidth * ratioWidthToHeight;
		    
		    palmClassifier.detectMultiScale(src, palmLocations, scale, neightbors, 2,
	                new Size(minWidth,minHeight),new Size(maxWidth, maxHeight));
		    
		    ratioWidthToMax = 0.2;
	    	ratioWidthMaxToMin = 0.75;
	    	ratioWidthToHeight = 1;
	    	
		    scale = 1.1;
		    neightbors = 2;
		    
		    maxWidth = width * ratioWidthToMax;
		    minWidth = maxWidth * ratioWidthMaxToMin;
		    maxHeight = maxWidth * ratioWidthToHeight;
		    minHeight = minWidth * ratioWidthToHeight;
		    
		    fistClassifier.detectMultiScale(src, fistLocations, scale, neightbors, 2,
	                new Size(minWidth,minHeight),new Size(maxWidth, maxHeight));  
	    }
	    
	    for (Rect rect : fistLocations.toArray()) {
	    	foo.add(new Hand( rect, false));	        
	    }
	    
	    for( Rect rect : palmLocations.toArray()) {
	    	foo.add(new Hand(rect, true));
	       
	    }
	    
		return foo;
		
	}
	
	public static Point centerOf(Rect rect){
		return new Point(rect.x + rect.width/2, rect.y+rect.height );
	}
	
	
}
