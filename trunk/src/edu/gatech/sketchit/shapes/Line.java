package edu.gatech.sketchit.shapes;

import org.opencv.core.Point3;

import android.opengl.GLES20;

public class Line extends Shape {
	public Line(Point3 a, Point3 b) {
		super(a, b);
		drawCode = GLES20.GL_LINES;
	}

	@Override
	public boolean contains(Point3 hand) {
		if(inLine(vertices[0], vertices[1], hand))
			return true;
		return false;
	}
	
}
