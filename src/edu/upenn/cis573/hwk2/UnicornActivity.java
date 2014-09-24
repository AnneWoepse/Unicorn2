package edu.upenn.cis573.hwk2;

import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class UnicornActivity extends Activity {

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_about) {
			Toast.makeText(getApplicationContext(), "Unicorn Game! \n(c)2014 Univ of Pennsylvania", Toast.LENGTH_SHORT).show(); 
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
}
