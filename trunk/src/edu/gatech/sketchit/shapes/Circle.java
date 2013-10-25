package edu.gatech.sketchit.shapes;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.opencv.core.Point3;

import edu.gatech.sketchit.MyGLRenderer;

import android.opengl.GLES20;

public class Circle extends Shape {
	public float radius;
	public Circle(Point3 a, float radius) {
		super();
		this.radius = radius;
		Point3[] circ = new Point3[30];
		circ[0] = a;
		for(int i=0;i<circ.length-1;i++) {
			float percent =  i / (float)(circ.length - 1);
			float rad = (float) (percent * 2*Math.PI);
//			System.out.println()
			circ[i+1] = new Point3(a.x - radius*Math.cos(rad), a.y - radius*Math.sin(rad), a.z);
			System.out.println(circ[i]);
		}
		this.vertices = circ;
		
		loadBuffer();
		
	//	int vertexCount = 8;
		//coords = new float[3*vertexCount];
//		int idx = 0;
//		coords[idx++] = (float)a.x;
//		coords[idx++] = (float)a.y;
//		coords[idx++] = 0;
//		
//		int outerVertexCount = vertexCount - 1;	
//		
//		for(int i=0;i<outerVertexCount;i++) {
//			float percent =  (i / (float)(outerVertexCount-1));
//			float rad = (float) (percent * 2*Math.PI);
//				coords[idx++] = (float) (a.x + Math.round(1000*radius*Math.cos(rad))/1000);
//			coords[idx++] = (float) (a.y + Math.round(1000*radius*Math.sin(rad))/1000);
//			coords[idx++] = 0.3f;
//		}
//		coords[0] = 1f;
//		coords[1] = 0;
//		coords[2] = 0;
//		
//		coords[3] = .707f;
//		coords[4] = .707f;
//		coords[5] = 0;
//		
//		coords[6] = 0;
//		coords[7] = 1f;
//		coords[8] = 0;
//			
//		coords[9] = -.707f;
//		coords[10] = .707f;
//		coords[11] = 0;
//		
//		coords[12] = -1f;
//		coords[13] = 0;
//		coords[14] = 0;
//		
//		coords[15] = -.707f;
//		coords[16] = -.707f;
//		coords[17] = 0;
//		
//		coords[18] = 0;
//		coords[19] = -1f;
//		coords[20] = 0;
//		
//		coords[21] = .707f;
//		coords[22] = -.707f;
//		coords[23] = 0;
		
    	vertexBuffer = ByteBuffer.allocateDirect(coords.length * MyGLRenderer.mBytesPerFloat)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();
		vertexBuffer.put(coords).position(0);
		
		colorBuffer = ByteBuffer.allocateDirect(color.length * MyGLRenderer.mBytesPerFloat)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();
		colorBuffer.put(color).position(0);
		drawCode = GLES20.GL_TRIANGLE_FAN;
	}
	@Override
	public boolean contains(Point3 hand) {
		// TODO Auto-generated method stub
		return false;
	}
}
