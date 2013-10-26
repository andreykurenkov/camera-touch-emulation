package com.activities;

import java.util.ArrayList;

import org.opencv.core.Point;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

public class DrawPointerView extends View {

	public DrawPointerView(Context context) {
		super(context);
	}
	Point drawPoint = new Point(0,0);
	double vX = 2;
	double vY = 3.5;

	ArrayList<Point> points = new ArrayList<Point>();

	public void onDraw(Canvas c){
		Paint p = new Paint();
		
		drawPoint.set(new double[]{drawPoint.x+vX,drawPoint.y+vY});
		c.drawCircle((float)drawPoint.x, (float)drawPoint.y, 8, p);
		if(drawPoint.x>c.getWidth() || drawPoint.x<0)
			vX*=-1;
		if(drawPoint.y>c.getHeight() || drawPoint.y<0)
			vY*=-1;
		
	}

}
