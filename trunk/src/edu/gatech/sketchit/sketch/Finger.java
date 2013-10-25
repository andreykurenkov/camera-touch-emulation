package edu.gatech.sketchit.sketch;

import java.util.HashMap;
import org.opencv.core.Scalar;
import edu.gatech.sketchit.cv.ColorDetector;

public class Finger {
	public static enum finger_id{Middle_Left,Pointer_Left, Thumb_Left, Middle_Right,Pointer_Right, Thumb_Right};
	private static HashMap<finger_id,Finger> fingers = new HashMap<finger_id,Finger>();
	//middle (left hand)","pointing (left hand)", "thumb (left hand)",
	//"middle (right hand)","pointing (right hand)", "thumb (right hand)
	private Scalar color;
	private ColorDetector cDetect;
	public Finger(finger_id fi, Scalar color, ColorDetector cDetect){
		this.color = color;
		this.cDetect = cDetect;
		fingers.put(fi, this);
	}
	
	public ColorDetector getColorDetector(){
		return this.cDetect;
	}
	
	public Scalar getColor(){
		return this.color;
	}
	
	public static HashMap<finger_id,Finger> getHashMap(){
		return fingers;
	}
	
}
