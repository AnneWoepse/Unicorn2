package edu.upenn.cis573.hwk2;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;

public class Image {
	public Bitmap unicorn;
	public Bitmap explode;

	public int getWidth(Bitmap image) {
		return image.getWidth();
	}
	
	public int getHeight(Bitmap image) {
		return image.getHeight();
	}
	
	public Bitmap drawUnicorn(Resources resource) {
		unicorn = BitmapFactory.decodeResource(resource, R.drawable.unicorn);
		unicorn = Bitmap.createScaledBitmap(unicorn, 150, 150, false);
		return unicorn;
	}
	
	public Bitmap drawExplosion(Resources resource) {
		explode = BitmapFactory.decodeResource(resource, R.drawable.explosion);
	    explode = Bitmap.createScaledBitmap(explode, 150, 150, false);
		return explode;
	}

	public Canvas drawUnicornAtPoint(Canvas canvas, Point p) {
		// draws the unicorn at the specified point
		canvas.drawBitmap(unicorn, p.x, p.y, null);
		return canvas;
	}
	
	public Canvas drawExplosionAtPoint(Canvas canvas, Point p) {
		// draws exploding image where unicorn is killed
		canvas.drawBitmap(explode, p.x, p.y, null);
		return canvas;
	}

}
