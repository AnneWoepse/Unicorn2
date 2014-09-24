package edu.upenn.cis573.hwk2;

import java.util.ArrayList;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;


public class GameView extends View {
    private Bitmap image;
    private ArrayList<Integer> xPoints = new ArrayList<Integer>();
    private ArrayList<Integer> yPoints = new ArrayList<Integer>();
    private boolean killed = false;
    private boolean newUnicorn = true;
    private int imagePointX = -150;
    private int imagePointY = 100;
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
    	if (newUnicorn || imagePointX >= this.getWidth()) {
    		imagePointX = -150;
    		imagePointY = (int)(Math.random() * 200 + 200);
    		yChange = (int)(10 - Math.random() * 20);
    		newUnicorn = false;
    		killed = false;
    	}

		// show the exploding image when the unicorn is killed
    	if (killed) {
    		Bitmap explode = BitmapFactory.decodeResource(getResources(), R.drawable.explosion);
    	    explode = Bitmap.createScaledBitmap(explode, 150, 150, false);
    		canvas.drawBitmap(explode, imagePointX, imagePointY, null);
    		newUnicorn = true;
    		try { Thread.sleep(10); } catch (Exception e) { }
    		invalidate();
    		return;
    	}

    	// draws the unicorn at the specified point
		canvas.drawBitmap(image, imagePointX, imagePointY, null);
    	
		// draws the stroke
    	if (xPoints.size() > 1) {
    		for (int i = 0; i < xPoints.size()-1; i++) {
    			int startX = xPoints.get(i);
    			int stopX = xPoints.get(i+1);
    			int startY = yPoints.get(i);
    			int stopY = yPoints.get(i+1);
    			Paint paint = new Paint();
    			paint.setColor(lineColor);
    			paint.setStrokeWidth(lineWidth);
    			canvas.drawLine(startX, startY, stopX, stopY, paint);
    		}
    	}
    	
    }

    /* 
     * This method is automatically called when the user touches the screen.
     */
    public boolean onTouchEvent(MotionEvent event) {
    	
    	if (event.getAction() == MotionEvent.ACTION_DOWN) {
    		xPoints.add((int)event.getX());
    		yPoints.add((int)event.getY());
    	}
    	else if (event.getAction() == MotionEvent.ACTION_MOVE) {
    		xPoints.add((int)event.getX());
    		yPoints.add((int)event.getY());
    	}
    	else if (event.getAction() == MotionEvent.ACTION_UP) {
    		xPoints.clear();
    		yPoints.clear();
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
    	if (!killed && x > imagePointX && x < imagePointX + width && y > imagePointY && y < imagePointY + height) {
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
    			imagePointX += 10; 
    			imagePointY += yChange; 
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

