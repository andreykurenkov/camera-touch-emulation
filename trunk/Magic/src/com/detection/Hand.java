package com.detection;

import org.opencv.core.Point;
import org.opencv.core.Rect;

public class Hand {

	private final Rect rect;
	private final Point center;
	private final boolean open;
	
	public Hand(Rect rect, boolean open){
		this.rect = rect;
		this.center = HandDetect.centerOf(rect);
		this.open = open;
	}
	
	public Hand(Point center, boolean open){
		this.rect = null;
		this.center = center;
		this.open = open;
	}
	
	public Rect getRect(){
		return rect;
	}
	
	public Point getCenter(){
		return center;
	}
	
	public boolean isOpen(){
		return open;
	}
	
	
	
}
