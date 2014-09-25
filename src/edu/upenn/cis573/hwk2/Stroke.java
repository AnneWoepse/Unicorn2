package edu.upenn.cis573.hwk2;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

public class Stroke {
    public ArrayList<Point> points = new ArrayList<Point>();
    private static final int lineColor = Color.RED;
    private static final int lineWidth = 10;
	
    public void addPoint(Point point) {
    	points.add(point);
    }
    
	public Canvas pointToPointLine(Canvas canvas) {
		// draws the stroke
    	if (points.size() > 1) {    		
    		for (int i = 0; i < points.size()-1; i++) {
    			Point start = points.get(i);
    			Point stop = points.get(i+1);
    			Paint paint = new Paint();
    			paint.setColor(lineColor);
    			paint.setStrokeWidth(lineWidth);
    			canvas.drawLine(start.x, start.y, stop.x, stop.y, paint);
    		}
    	}
		return canvas;
	}

}
