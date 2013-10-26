package com.detection;

import org.opencv.core.*;
import org.opencv.highgui.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.features2d.*;
import org.opencv.objdetect.*;

public class HandDetect {

	/*
    Imgproc.GaussianBlur(src, src, new Size(5,5), 1);
    Imgproc.GaussianBlur(src,  src, new Size(5,5), 1);
    Imgproc.cvtColor(src, src, Imgproc.COLOR_BGR2GRAY);
    
    //work on extracting hands
    
    src.convertTo(src, CvType.CV_8U);
    MatOfRect fistLocations = new MatOfRect();
    MatOfRect palmLocations = new MatOfRect();
    
    //fistClassifier.detectMultiScale(src, fistLocations);
    palmClassifier.detectMultiScale(src, palmLocations);
    
    
    for (Rect rect : fistLocations.toArray()) {
        Core.rectangle(src, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0));
    }
    
    for( Rect rect : palmLocations.toArray()) {
    	Core.rectangle(src, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y+rect.height), new Scalar(255, 0 ,0));
    }
    */
    
     //SIFT on a hand
    
    /*
    src=src.submat((int)src.size().height/2, (int)src.size().height, (int)src.size().width/2, (int)src.size().width);
    FeatureDetector sift = FeatureDetector.create(FeatureDetector.SIFT );
    MatOfKeyPoint out = new MatOfKeyPoint();
    
    src.convertTo(src, CvType.CV_32F);
    Mat dest = new Mat();
    Mat d2 = new Mat();
    Mat d3 = new Mat();
    Mat d4 = new Mat();
    
    Mat kernel = Imgproc.getGaborKernel(new Size(5,5), 5, 0, 55.5/100, 1, 0, CvType.CV_32F);
    Mat k2 = Imgproc.getGaborKernel(new Size(5,5), 45, 0, .55, 1, 0, CvType.CV_32F);
    Mat k3 = Imgproc.getGaborKernel(new Size(5,5), 90, 0, .55, 1, 0, CvType.CV_32F);
    Mat k4 = Imgproc.getGaborKernel(new Size(5,5), 135, 0, .55, 1, 0, CvType.CV_32F);
    
    Core.normalize(kernel, kernel);
    
    Imgproc.filter2D(src, dest, CvType.CV_32F, kernel);
  
    Imgproc.filter2D(src, d2, CvType.CV_32F, k2);
    
    Imgproc.filter2D(src,d3, CvType.CV_32F, k3);
    
    Imgproc.filter2D(src, d4, CvType.CV_32F, k4);
    
    Core.add(dest, d2, src);
    Mat temp = new Mat();
    Core.add(d4, d3, temp);
    
    Core.divide(temp, new Scalar(2), temp);
    Core.divide(src, new Scalar(2), src);
    
    Core.add(src,temp,src);
    Core.divide(src, new Scalar(4), src);
   
    
    src.convertTo(src, CvType.CV_8U);
    
    sift.detect(src, out);
    Features2d.drawKeypoints(src, out, src);
    */
	
}
