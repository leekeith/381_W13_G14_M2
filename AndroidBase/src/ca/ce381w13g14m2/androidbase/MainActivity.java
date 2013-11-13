package ca.ce381w13g14m2.androidbase;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {
	DrawView drawView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main); 

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
/*	
	// Called when the user clicks the Send button 
	public void onButtonClick(View view) {
	    //Intent intent = new Intent(this, GFXSurface.class);
	    //startActivity(intent);
		Intent intent = new Intent(this, Draw.class);
	    startActivity(intent);
	}
*/	
}
