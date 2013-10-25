package edu.gatech.sketchit.shapes;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Random;

import org.opencv.core.Point3;

import edu.gatech.sketchit.MyGLRenderer;
import android.opengl.GLES20;



public abstract class Shape {
	
    public FloatBuffer vertexBuffer;
    public FloatBuffer colorBuffer;
        
    protected int vertexCount;
	protected float[] color = {1.0f, 0.0f, 0.0f, 1.0f};
    protected float[] coords;
    public Point3[] vertices;
    
    public int drawCode;
    
    public Shape(){}
    public Shape(Point3... vertices) {
    	this.vertices = vertices;
		loadBuffer();
    }
    
    protected void loadBuffer() {
    	coords = new float[3*vertices.length];
		for(int i=0;i<vertices.length;i++) {
			coords[3*i] = (float)vertices[i].x;
			coords[3*i+1] = (float)vertices[i].y;
			coords[3*i+2] = (float)vertices[i].z;
		}
    	vertexBuffer = ByteBuffer.allocateDirect(coords.length * MyGLRenderer.mBytesPerFloat)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();
		vertexBuffer.put(coords).position(0);
		
		colorBuffer = ByteBuffer.allocateDirect(color.length * MyGLRenderer.mBytesPerFloat)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();
		colorBuffer.put(color).position(0);
    }
    
    
    
    public static Point3[] randomShape(int numVertices){
    	Point3[] vertices = new Point3[numVertices];
		Random rand = new Random();
		for(int i=0;i<numVertices;i++) {
			Point3 p = new Point3();
			p.x = rand.nextFloat()*(rand.nextBoolean()?-1:1);
			p.y = rand.nextFloat()*(rand.nextBoolean()?-1:1);
			p.z = rand.nextFloat()*(rand.nextBoolean()?-1:1);
			vertices[i] = p;
		}
		return vertices;
    }
    
    //AASHISH MODIFIED
    protected int contains_variance = 5;  
    public abstract boolean contains(Point3 hand);
    protected boolean inLine(Point3 a, Point3 b, Point3 hand){
    	Point3 origvect = new Point3(a.x - b.x, a.y - b.y, a.z - b.z);
    	Point3 currvect = new Point3(a.x - hand.x, a.y - hand.y, a.z - hand.z);
    	
    	double dotprod = origvect.dot(currvect);
    	double magorig = Math.sqrt(origvect.x+origvect.y+origvect.z);
    	double magcurr = Math.sqrt(currvect.x+currvect.y+currvect.z);
    	double theta = Math.acos(dotprod/(magorig+magcurr));
    	double normal = magcurr*Math.sin(theta);
    	
    	if(normal < contains_variance)
    		return true;
    	return false;
    }
    
}
