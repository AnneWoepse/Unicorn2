package edu.upenn.cis573.hwk2;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.AsyncTask;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class GameView extends View {
	private boolean killed = false;
	private boolean newUnicorn = true;
	private Point imagePoint = new Point(-150, 100);
	private int score = 0;
	private int yChange = 0;
	Stroke stroke = new Stroke();
	Image picture = new Image();
	Activity parentActivity;
	
	public GameView(Context context) {
		super(context);
		backgroundResource();
	}

	public GameView(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
		backgroundResource();
	}

	public void backgroundResource() {
		setBackgroundResource(R.drawable.space);
		picture.drawUnicorn(getResources());
	}
	
	public Point getImagePoint() {
		return imagePoint;
	}
	
	public int getScore() {
		return score;
	}
	
	public int getYChange() {
		return yChange;
	}

	/*
	 * This method is automatically invoked when the View is displayed. It is
	 * also called after you call "invalidate" on this object.
	 */
	protected void onDraw(Canvas canvas) {
		// resets the position of the unicorn if one is killed or reaches the
		// right edge
		if (newUnicorn || imagePoint.x >= this.getWidth()) {
			imagePoint.x = -150;
			imagePoint.y = (int) (Math.random() * 200 + 200);
			yChange = (int) (10 - Math.random() * 20);
			newUnicorn = false;
			killed = false;
		}

		// show the exploding image when the unicorn is killed
		if (killed) {
			picture.drawExplosion(getResources());
			picture.drawExplosionAtPoint(canvas, imagePoint);
			newUnicorn = true;
			try {
				Thread.sleep(10);
			} catch (Exception e) {
			}
			invalidate();
			return;
		}

		picture.drawUnicornAtPoint(canvas, imagePoint);

		stroke.pointToPointLine(canvas);

	}

	/*
	 * This method is automatically called when the user touches the screen.
	 */
	public boolean onTouchEvent(MotionEvent event) {

		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			stroke.addPoint(new Point((int) event.getX(), (int) event.getY()));
		} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
			stroke.addPoint(new Point((int) event.getX(), (int) event.getY()));
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			stroke.points.clear();
		} else {
			return false;
		}

		// see if the point is within the boundary of the image
		int width = picture.getWidth(picture.unicorn);
		int height = picture.getHeight(picture.unicorn);
		float x = event.getX();
		float y = event.getY();
		// the !killed thing here is to prevent a "double-kill" that could occur
		// while the "explosion" image is being shown
		if (!killed && x > imagePoint.x && x < imagePoint.x + width
				&& y > imagePoint.y && y < imagePoint.y + height) {
			killed = true;
			score++;
			((TextView) (GameActivity.instance.getScoreboard())).setText(""
					+ score);
		}

		// forces a redraw of the View
		invalidate();

		return true;
	}
	
}
