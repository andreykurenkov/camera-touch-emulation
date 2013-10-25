package edu.gatech.sketchit.shapes;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

import org.opencv.core.Point3;

import android.opengl.GLES20;

public class Rectangle extends Shape {
	public Rectangle(Point3 a, Point3 b, Point3 c, Point3 d) {
		super(a,b,c,c,d,a);
		drawCode = GLES20.GL_TRIANGLES;
	}
	
	public Rectangle(Point3 a, Point3 b, Point3 c, Point3 d, float[] color){
		this(a,b,c,d);
		this.color = color;
	}

	@Override
	public boolean contains(Point3 hand) {
		if(inLine(vertices[0], vertices[1], hand) || inLine(vertices[1], vertices[2], hand) || inLine(vertices[2], vertices[3], hand) || inLine(vertices[0], vertices[3], hand))
			return true;
		return false;
	}
}
