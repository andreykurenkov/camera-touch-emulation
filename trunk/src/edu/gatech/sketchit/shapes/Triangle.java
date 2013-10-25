package edu.gatech.sketchit.shapes;

import org.opencv.core.Point3;

import android.opengl.GLES20;

public class Triangle extends Shape {
    
	public Triangle(Point3 a, Point3 b, Point3 c) {
		super(a, b, c);
		drawCode = GLES20.GL_TRIANGLES;
	}

	@Override
	public boolean contains(Point3 hand) {
		if(inLine(vertices[0], vertices[1], hand) || inLine(vertices[1], vertices[2], hand) || inLine(vertices[0], vertices[2], hand))
			return true;
		return false;
	}
}
