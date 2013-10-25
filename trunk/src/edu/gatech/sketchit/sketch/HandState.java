package edu.gatech.sketchit.sketch;

import org.opencv.core.Mat;
import org.opencv.core.Point3;
import android.os.SystemClock;
import android.util.Log;

public class HandState {
	private Finger pointing;
	private Finger middle;
	private Finger thumb;
	private int clickedCount;
	private long timeOfDown, timeOfUp;
	public final static double CLICK_DIST=165;
	
	public HandState(Finger pointing, Finger thumb){
		this(pointing,thumb,null);
	}
	
	public HandState(Finger pointing, Finger thumb, Finger middle){
		this.pointing = pointing;
		this.thumb = thumb;
		this.middle = middle;
		timeOfDown = -1;
		timeOfUp = SystemClock.uptimeMillis();
		clickedCount = 0;
	}
	
	public Point3 isClick(Mat img){
		Point3 pointingPoint = pointing.getColorDetector().detectBiggestBlob(img);
		Point3 thumbPoint = thumb.getColorDetector().detectBiggestBlob(img);
		if(pointingPoint==null || thumbPoint==null)
			return null;
		Point3 diffPoint = new Point3( pointingPoint.x - thumbPoint.x,
										pointingPoint.y - thumbPoint.y,
										pointingPoint.z - thumbPoint.z);
		double dist = Math.sqrt(diffPoint.dot(diffPoint));
		Point3 mid = new Point3( (pointingPoint.x + thumbPoint.x)/2,
								 (pointingPoint.y + thumbPoint.y)/2,
				   				 (pointingPoint.z + thumbPoint.z)/2);
		return dist<CLICK_DIST?mid:null;
	}
	
	public Point3 updateClickedState(Mat img){
		Point3 click = isClick(img);
		if(click!=null){
			if(clickedCount == 0)
				timeOfDown = SystemClock.uptimeMillis();
			clickedCount++;
		}else{
			if(clickedCount>0)
				timeOfUp = SystemClock.uptimeMillis();
			clickedCount=0;
		}
		return click;
	}
	
	public int getClickedCount(){
		return clickedCount;
	}

	public Finger getPointing() {
		return pointing;
	}

	public void setPointing(Finger pointing) {
		this.pointing = pointing;
	}

	public Finger getMiddle() {
		return middle;
	}

	public void setMiddle(Finger middle) {
		this.middle = middle;
	}

	public Finger getThumb() {
		return thumb;
	}

	public void setThumb(Finger thumb) {
		this.thumb = thumb;
	}

	public long getTimeOfDown() {
		return timeOfDown;
	}

	public void setTimeOfDown(long timeOfDown) {
		this.timeOfDown = timeOfDown;
	}

	public long getTimeOfUp() {
		return timeOfUp;
	}

	public void setTimeOfUp(long timeOfUp) {
		this.timeOfUp = timeOfUp;
	}

	public void setClickedCount(int clickedCount) {
		this.clickedCount = clickedCount;
	}
	
}
