package edu.upenn.cis573.hwk2;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.AsyncTask;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;


public class GameView extends View {
    private Bitmap image;
    private ArrayList<Point> points = new ArrayList<Point>();
    private boolean killed = false;
    private boolean newUnicorn = true;
    private Point imagePoint = new Point(-150,100);
    private int score = 0;
    private int yChange = 0;
    private static final int lineColor = Color.RED;
    private static final int lineWidth = 10;
    public long startTime;
    public long endTime;

    public GameView(Context context) {
	    super(context);
	    setBackgroundResource(R.drawable.space);
	    image = BitmapFactory.decodeResource(getResources(), R.drawable.unicorn);
	    image = Bitmap.createScaledBitmap(image, 150, 150, false);
    }
    
    public GameView(Context context, AttributeSet attributeSet) {
    	super(context, attributeSet);
	    setBackgroundResource(R.drawable.space);
	    image = BitmapFactory.decodeResource(getResources(), R.drawable.unicorn);
	    image = Bitmap.createScaledBitmap(image, 150, 150, false);
    }
    
    /*
     * This method is automatically invoked when the View is displayed.
     * It is also called after you call "invalidate" on this object.
     */
    protected void onDraw(Canvas canvas) {    	

    	// resets the position of the unicorn if one is killed or reaches the right edge
    	if (newUnicorn || imagePoint.x >= this.getWidth()) {
    		imagePoint.x = -150;
    		imagePoint.y = (int)(Math.random() * 200 + 200);
    		yChange = (int)(10 - Math.random() * 20);
    		newUnicorn = false;
    		killed = false;
    	}

		// show the exploding image when the unicorn is killed
    	if (killed) {
    		Bitmap explode = BitmapFactory.decodeResource(getResources(), R.drawable.explosion);
    	    explode = Bitmap.createScaledBitmap(explode, 150, 150, false);
    		canvas.drawBitmap(explode, imagePoint.x, imagePoint.y, null);
    		newUnicorn = true;
    		try { Thread.sleep(10); } catch (Exception e) { }
    		invalidate();
    		return;
    	}

    	// draws the unicorn at the specified point
		canvas.drawBitmap(image, imagePoint.x, imagePoint.y, null);
    	
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
    	
    }

    /* 
     * This method is automatically called when the user touches the screen.
     */
    public boolean onTouchEvent(MotionEvent event) {
    	
    	if (event.getAction() == MotionEvent.ACTION_DOWN) {
    		points.add(new Point((int)event.getX(),(int)event.getY()));
    	}
    	else if (event.getAction() == MotionEvent.ACTION_MOVE) {
    		points.add(new Point((int)event.getX(),(int)event.getY()));
    	}
    	else if (event.getAction() == MotionEvent.ACTION_UP) {
    		points.clear();
    	}
    	else {
    		return false;
    	}
    	
    	// see if the point is within the boundary of the image
    	int width = image.getWidth();
    	int height = image.getHeight();
    	float x = event.getX();
    	float y = event.getY();
    	// the !killed thing here is to prevent a "double-kill" that could occur
    	// while the "explosion" image is being shown
    	if (!killed && x > imagePoint.x && x < imagePoint.x + width && y > imagePoint.y && y < imagePoint.y + height) {
    		killed = true;
    		score++;
    		((TextView)(GameActivity.instance.getScoreboard())).setText(""+score);
    	}
    	
    	// forces a redraw of the View
    	invalidate();
    	
    	return true;
    }    

    
    /*
     * This inner class is responsible for making the unicorn appear to move.
     * When "exec" is called on an object of this class, "doInBackground" gets
     * called in a background thread. It just waits 10ms and then updates the
     * image's position. Then "onPostExecute" is called.
     */
    class BackgroundDrawingTask extends AsyncTask<Integer, Void, Integer> {
    	
    	// this method gets run in the background
    	protected Integer doInBackground(Integer... args) {
    		try { 
    			// note: you can change these values to make the unicorn go faster/slower
    			Thread.sleep(10); 
    			imagePoint.x += 10;
    			imagePoint.y += yChange;
    		} 
    		catch (Exception e) { }
    		// the return value is passed to "onPostExecute" but isn't actually used here
    		return 1; 
    	}
    	
    	// this method gets run in the UI thread
    	protected void onPostExecute(Integer result) {
    		// redraw the View
    		invalidate();
    		if (score < 10) {
    			// need to start a new thread to make the unicorn keep moving
    			BackgroundDrawingTask task = new BackgroundDrawingTask();
    			task.execute();
    		}
    		else {
    			// game over, man!
    			endTime = System.currentTimeMillis();
    			// these methods are deprecated but it's okay to use them... probably.
    			GameActivity.instance.removeDialog(1);
    			GameActivity.instance.showDialog(1);
    		}
    	}    	
    }

}

