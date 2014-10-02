package edu.upenn.cis573.hwk2;

//import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class GameActivity extends UnicornActivity {
	
	// a global, static instance so that the GameView object can refer to this object

	public static GameView gv;
	
	// keeps track of the best time so far
	private static float bestTime = 1000000;
	public long startTime;
	public static long endTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.play_game);
		
        gv.parentActivity = this;
        
        // this method is deprecated but, trust me, it's easier this way
        showDialog(0);
	}
	
    public void onButtonClick(View v) {
    	// this terminates the Activity and goes back to the previous one
    	finish();
    }
    
    protected Dialog onCreateDialog(int id) {
    	if (id == 0) {
	    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
            // this is the message to display
	    	builder.setMessage("Ready?"); 
            // this is the button to display
	    	builder.setPositiveButton(R.string.yes,
	    		new DialogInterface.OnClickListener() {
                       // this is the method to call when the button is clicked 
	    	           public void onClick(DialogInterface dialog, int id) {
                           // this will hide the dialog
	    	        	   dialog.cancel();
	    	        	   // then start the unicorn moving across the screen
	    	               GameView gv = (GameView)findViewById(R.id.gameView);
	    	               BackgroundDrawingTask t = new BackgroundDrawingTask(gv);
	    	               t.execute();
	    	               startTime = System.currentTimeMillis();
	    	           }
	    	         });
    		return builder.create();
    	}
    	else if (id == 1) {
	    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
            // figure out which message to display
            GameView gv = (GameView)findViewById(R.id.gameView);
	    	long time = endTime - startTime;
	    	// a little magic to convert to tenths of a second
	    	float displayTime = (time / 100) / (float)10.0;
	    	if (bestTime == 10000000) {
	    		bestTime = displayTime;
		    	builder.setMessage("Great job! You finished in " + displayTime + " seconds"); 
	    	}
	    	else if (displayTime <= bestTime) {
	    		bestTime = displayTime;
		    	builder.setMessage("You finished in " + displayTime + " seconds! That's the fastest so far!"); 
	    	}
	    	else builder.setMessage("Great job! You finished in " + displayTime + " seconds; the best so far is " + bestTime);
	    	
            // this is the button to display
	    	builder.setPositiveButton(R.string.yes,
	    		new DialogInterface.OnClickListener() {
                       // this is the method to call when the button is clicked 
	    	           public void onClick(DialogInterface dialog, int id) {
                           // this will hide the dialog
	    	        	   dialog.cancel();
	    	        	   // this will terminate this Activity
	    	        	   finish();
	    	           }
	    	         });
    		return builder.create();
    		
    	}
    	else return null;
    }
    
    
}


