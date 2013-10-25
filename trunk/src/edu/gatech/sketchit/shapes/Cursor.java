package edu.gatech.sketchit.shapes;

import org.opencv.core.Point3;

import android.opengl.GLES20;

public class Cursor extends Rectangle {
	public Cursor(Point3 location) {
		super(	new Point3(location.x - .1, location.y - .1, location.z),
				new Point3(location.x - .1, location.y + .1, location.z),
				new Point3(location.x + .1, location.y + .1, location.z),
				new Point3(location.x + .1, location.y - .1, location.z),
				new float[]{1.0f, .2f, .1f, .7f});
			}
	
	@Override
	public boolean contains(Point3 hand) {
		// TODO Auto-generated method stub
		return false;
	}

}
