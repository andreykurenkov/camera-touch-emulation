package edu.gatech.sketchit;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import org.opencv.core.Point3;
import edu.gatech.sketchit.shapes.*;


import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;
import android.util.Log;

public class MyGLRenderer implements Renderer {
	//model space
	private float[] mModelMatrix = new float[16];
	
	//viewport (camera)
	private float[] mViewMatrix = new float[16];
	
	//projects 3d to 2d space
	private float[] mProjectionMatrix = new float[16];
	
	//final combination
	private float[] mMVPMatrix = new float[16];
	
	private int mMVPMatrixHandle;
	private int mPositionHandle;
	private int mColorHandle;

	public static final int mBytesPerFloat = 4;
	private final int mStrideBytes = 3 * mBytesPerFloat;	
	private final int mPositionOffset = 0;
	private final int mPositionDataSize = 3;
//	private final int mColorOffset = 3;
//	private final int mColorDataSize = 4;	
	
	private List<Shape> shapes = new ArrayList<Shape>();
	private Shape cursor1;
	private Shape cursor2;
	
	public float mAngleX;
	public float mAngleY;
	public float zoom;
	
	private int lastWidth;
	private int lastHeight;
	
	public void zoom(float z) {
		zoom += z;
		onSurfaceChanged(null, lastWidth, lastHeight);
	}
	
	private void draw(Shape s) {
		s.vertexBuffer.position(mPositionOffset);
        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false,
        		3, s.vertexBuffer);        
                
        GLES20.glEnableVertexAttribArray(mPositionHandle);        
        
        // Pass in the color information
        GLES20.glVertexAttribPointer(mColorHandle, 4, GLES20.GL_FLOAT, false,
        		4, s.colorBuffer);        
        
        GLES20.glEnableVertexAttribArray(mColorHandle);
        
		// This multiplies the view matrix by the model matrix, and stores the result in the MVP matrix
        // (which currently contains model * view).
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);
        
        // This multiplies the modelview matrix by the projection matrix, and stores the result in the MVP matrix
        // (which now contains model * view * projection).
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);

        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);
        
//        GLES20.glVertexAttribPointer(0, 3, GLES20.GL_FLOAT, false, 3, ptr)
//        GLES20.glDrawElements(GLES20.GL_TRIANGLE_FAN, count, type, indices)
        GLES20.glDrawArrays(s.drawCode,0, s.drawCode == GLES20.GL_LINE_LOOP ? 30 : 3);     
	}
	
	@Override
	public void onDrawFrame(GL10 gl) {
		GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);			        
        
		Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.rotateM(mModelMatrix, 0, mAngleX, 0.0f, 1.0f, 0.0f);  
        Matrix.rotateM(mModelMatrix, 0, mAngleY, 1.0f, 0.0f, 0.0f);
        
        for(int i=0;i<shapes.size();i++) {
        	Shape s = shapes.get(i);
        	draw(s);
        }
        if(cursor1 != null) {
        	Log.i("MyGLRenderer","Cursor1 is "+cursor1);
        	for(Point3 p:cursor1.vertices){
        		Log.i("MyGLRenderer","vertex is "+p);
        	}
        	draw(cursor1);
        }
        if(cursor2 != null) {
        	//Log.i("MyGLRenderer","Cursor2 is "+cursor2);
        	draw(cursor2);
        }
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		lastWidth = width;
		lastHeight = height;
		GLES20.glViewport(0, 0, width, height);

		// Create a new perspective projection matrix. The height will stay the same
		// while the width will vary as per aspect ratio.
		final float ratio = (float) width / height;
		final float left = -ratio;
		final float right = ratio;
		final float bottom = -1.0f;
		final float top = 1.0f;
		final float near = 1.0f + zoom*.001f;
		final float far = 8.0f + zoom*0.1f;
		
		Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		GLES20.glClearColor(0.5f, 0.5f, 0.5f, 0.5f);
		
		// Position the eye behind the origin.
		final float eyeX = 0.0f;
		final float eyeY = 0.0f;
		final float eyeZ = 1.5f;

		// We are looking toward the distance
		final float lookX = 0.0f;
		final float lookY = 0.0f;
		final float lookZ = -5.0f;

		// Set our up vector. This is where our head would be pointing were we holding the camera.
		final float upX = 0.0f;
		final float upY = 1.0f;
		final float upZ = 0.0f;

		// Set the view matrix. This matrix can be said to represent the camera position.
		// NOTE: In OpenGL 1, a ModelView matrix is used, which is a combination of a model and
		// view matrix. In OpenGL 2, we can keep track of these matrices separately if we choose.
		Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);

		final String vertexShader =
			"uniform mat4 u_MVPMatrix;      \n"		// A constant representing the combined model/view/projection matrix.
			
		  + "attribute vec4 a_Position;     \n"		// Per-vertex position information we will pass in.
		  + "attribute vec4 a_Color;        \n"		// Per-vertex color information we will pass in.			  
		  
		  + "varying vec4 v_Color;          \n"		// This will be passed into the fragment shader.
		  
		  + "void main()                    \n"		// The entry point for our vertex shader.
		  + "{                              \n"
		  + "   v_Color = a_Color;          \n"		// Pass the color through to the fragment shader. 
		  											// It will be interpolated across the triangle.
		  + "   gl_Position = u_MVPMatrix   \n" 	// gl_Position is a special variable used to store the final position.
		  + "               * a_Position;   \n"     // Multiply the vertex by the matrix to get the final point in
		  + "}                              \n";    // normalized screen coordinates.
		
		final String fragmentShader =
			"precision mediump float;       \n"		// Set the default precision to medium. We don't need as high of a 
													// precision in the fragment shader.				
		  + "varying vec4 v_Color;          \n"		// This is the color from the vertex shader interpolated across the 
		  											// triangle per fragment.			  
		  + "void main()                    \n"		// The entry point for our fragment shader.
		  + "{                              \n"
		  + "   gl_FragColor = v_Color;     \n"		// Pass the color directly through the pipeline.		  
		  + "}                              \n";												
		
		// Load in the vertex shader.
		int vertexShaderHandle = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);

		if (vertexShaderHandle != 0) 
		{
			// Pass in the shader source.
			GLES20.glShaderSource(vertexShaderHandle, vertexShader);

			// Compile the shader.
			GLES20.glCompileShader(vertexShaderHandle);

			// Get the compilation status.
			final int[] compileStatus = new int[1];
			GLES20.glGetShaderiv(vertexShaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

			// If the compilation failed, delete the shader.
			if (compileStatus[0] == 0) 
			{				
				GLES20.glDeleteShader(vertexShaderHandle);
				vertexShaderHandle = 0;
			}
		}

		if (vertexShaderHandle == 0)
		{
			throw new RuntimeException("Error creating vertex shader.");
		}
		
		// Load in the fragment shader shader.
		int fragmentShaderHandle = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);

		if (fragmentShaderHandle != 0) 
		{
			// Pass in the shader source.
			GLES20.glShaderSource(fragmentShaderHandle, fragmentShader);

			// Compile the shader.
			GLES20.glCompileShader(fragmentShaderHandle);

			// Get the compilation status.
			final int[] compileStatus = new int[1];
			GLES20.glGetShaderiv(fragmentShaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

			// If the compilation failed, delete the shader.
			if (compileStatus[0] == 0) 
			{				
				GLES20.glDeleteShader(fragmentShaderHandle);
				fragmentShaderHandle = 0;
			}
		}

		if (fragmentShaderHandle == 0)
		{
			throw new RuntimeException("Error creating fragment shader.");
		}
		
		// Create a program object and store the handle to it.
		int programHandle = GLES20.glCreateProgram();
		
		if (programHandle != 0) 
		{
			// Bind the vertex shader to the program.
			GLES20.glAttachShader(programHandle, vertexShaderHandle);			

			// Bind the fragment shader to the program.
			GLES20.glAttachShader(programHandle, fragmentShaderHandle);
			
			// Bind attributes
			GLES20.glBindAttribLocation(programHandle, 0, "a_Position");
			GLES20.glBindAttribLocation(programHandle, 1, "a_Color");
			
			// Link the two shaders together into a program.
			GLES20.glLinkProgram(programHandle);

			// Get the link status.
			final int[] linkStatus = new int[1];
			GLES20.glGetProgramiv(programHandle, GLES20.GL_LINK_STATUS, linkStatus, 0);

			// If the link failed, delete the program.
			if (linkStatus[0] == 0) 
			{				
				GLES20.glDeleteProgram(programHandle);
				programHandle = 0;
			}
		}
		
		if (programHandle == 0)
		{
			throw new RuntimeException("Error creating program.");
		}
        
        // Set program handles. These will later be used to pass in values to the program.
        mMVPMatrixHandle = GLES20.glGetUniformLocation(programHandle, "u_MVPMatrix");        
        mPositionHandle = GLES20.glGetAttribLocation(programHandle, "a_Position");
        mColorHandle = GLES20.glGetAttribLocation(programHandle, "a_Color");        
        
        // Tell OpenGL to use this program when rendering.
        GLES20.glUseProgram(programHandle);       
		
	}
	
	public void addShape(Shape s) {
		shapes.add(s);
	}
	public void removeShape(Shape s) {
		shapes.remove(s);
	}
	
	public void setCursor1(Point3 location) {
		location.x = location.x / lastWidth;
		location.y = location.y / lastHeight;
		cursor1 = new Cursor(location);
		this.onDrawFrame(null);
	}
	public void setCursor2(Point3 location) {
		location.x = location.x / lastWidth;
		location.y = location.y / lastHeight;
		cursor2 = new Cursor(location);
		this.onDrawFrame(null);
	}
	
	public void clearCursor1() {
		cursor1 = null;
	}
	
	public void clearCursor2() {
		cursor2 = null;
	}
}
