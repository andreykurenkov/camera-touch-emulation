package com.activities;

import java.util.ArrayList;

import org.opencv.core.Point;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

public class DrawPointerView extends View {

	public DrawPointerView(Context context) {
		super(context);
	}

	ArrayList<Point> points = new ArrayList<Point>();

	public void onDraw(Canvas c){
		Paint p = new Paint();

		for(Point point:points)
			c.drawCircle((float)point.x, (float)point.y, 3, p);
	}

}
