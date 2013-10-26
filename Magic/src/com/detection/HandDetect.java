package com.detection;

import org.opencv.core.*;
import org.opencv.highgui.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.features2d.*;
import org.opencv.objdetect.*;

import java.util.ArrayList;
import java.util.List;

public class HandDetect {

	private static CascadeClassifier fistClassifier = new CascadeClassifier("fist.xml");
	private static CascadeClassifier palmClassifier = new CascadeClassifier("palm.xml");
	
	public static List<Hand> findHands(Mat src){
		
		ArrayList<Hand> foo = new ArrayList<Hand>();

		
		Imgproc.GaussianBlur(src,  src, new Size(5,5), 1);
	    Imgproc.cvtColor(src, src, Imgproc.COLOR_BGR2GRAY);
	    
	    src.convertTo(src, CvType.CV_8U);
	    MatOfRect fistLocations = new MatOfRect();
	    MatOfRect palmLocations = new MatOfRect();
	    
	    fistClassifier.detectMultiScale(src, fistLocations);
	    palmClassifier.detectMultiScale(src, palmLocations);
	    
	    
	    for (Rect rect : fistLocations.toArray()) {
	        Core.rectangle(src, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0));
	    }
	    
	    for( Rect rect : palmLocations.toArray()) {
	    	Core.rectangle(src, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y+rect.height), new Scalar(255, 0 ,0));
	    }
	    
		
		return foo;
		
	}
	
}
