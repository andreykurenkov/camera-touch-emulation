package com.detection;

import org.opencv.core.Point;

public class Hand {

	private final Point center;
	private final boolean open;
	
	
	public Hand(Point center, boolean open){
		this.center = center;
		this.open = open;
		
	}
	
	public Point getCenter(){
		return center;
	}
	
	public boolean isOpen(){
		return open;
	}
	
	
	
}
