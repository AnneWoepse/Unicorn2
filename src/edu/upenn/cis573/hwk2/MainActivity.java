package edu.upenn.cis573.hwk2;

//import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends UnicornActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome_screen);
	}

	/*
	 * This method is called automatically when the user clicks on the button.
	 */
	public void onButtonClick(View v) {
		// this code is used to launch the GameActivity
		Intent i = new Intent(this, GameActivity.class);
		startActivity(i);
	}
}
