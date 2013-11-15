package ca.ce381w13g14m2.androidbase;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class MainActivity extends Activity {
	DrawView drawView;
    SeekBar seekbar;
    static int brushWidth;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);   

		seekbar = (SeekBar) findViewById(R.id.seekbar1);
		seekbar.setOnSeekBarChangeListener( new OnSeekBarChangeListener(){
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
        brushWidth = progress;
	}
	@Override
	public void onStartTrackingTouch(SeekBar arg0) {}
	@Override
	public void onStopTrackingTouch(SeekBar arg0) {}
}); 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void color_yellow(View view) {DrawView.color = Color.YELLOW;}
	public void color_blue(View view) {DrawView.color = Color.BLUE;}
	public void color_green(View view) {DrawView.color = Color.GREEN;}
	public void color_red(View view) {DrawView.color = Color.RED;}

	// Called when the user clicks the Clear button 
	public void onButtonClick(View view) {
	    //Intent intent = new Intent(this, Draw.class);
	    //startActivity(intent);
		DrawView.clear = true;
	}

}
