package ca.ce381w13g14m2.androidbase;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Timer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class MainActivity extends Activity {
	DrawView drawView;
    SeekBar seekbar;
    static int brushWidth;
    TheApplication app;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);   

		seekbar = (SeekBar) findViewById(R.id.seekBar1);
		seekbar.setOnSeekBarChangeListener( new OnSeekBarChangeListener(){
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
		        brushWidth = progress;
			}
			@Override
			public void onStartTrackingTouch(SeekBar arg0) {}
			@Override
			public void onStopTrackingTouch(SeekBar arg0) {}
		}); 
		app=(TheApplication)getApplication();

		app.tcp_timer=new Timer();
		
		
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		new SocketConnect().execute(app);
		Log.w("MainActivity", "app.ip="+app.getAddr()+"; app.port="+app.getPort().toString());
		
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
		switch (item.getItemId()) {
	        case R.id.action_connect:
	        	Intent i=new Intent(this,ConnectScreen.class);
	              startActivity(i);CheckBox cb=(CheckBox)findViewById(R.id.checkBox1);
	              return true;
	        case R.id.action_settings:
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
		}
    }
	
	@Override
	public void onPause()
	{
		super.onPause();
		closeSocket(null);
	}
	

	public void color_blue(View view) {
		DrawView.color = Color.BLUE;
	}
	
	public void color_yellow(View view) {
		DrawView.color = Color.YELLOW;
	}
	
	public void color_green(View view) {
		DrawView.color = Color.GREEN;
	}
	
	public void color_red(View view) {
		DrawView.color = Color.RED;
	}
	
	public void onClear(View view)
	{
		DrawView.clear = true;
	}
	
	public void closeSocket(View view) {
		Socket s = app.getSock();
		try {
			s.getOutputStream().close();
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public class SocketConnect extends AsyncTask<TheApplication, Void, Socket>{
		protected Socket doInBackground(TheApplication... apps){
			Socket s=null;
			try{
				s=new Socket(apps[0].getAddr(),apps[0].getPort());
			}catch(UnknownHostException e){
				Log.w("SocketConnect", "Unknown Host");
				e.printStackTrace();
			} catch (IOException e) {
				Log.w("SocketConnect", "IO Exception");
				e.printStackTrace();
			}
			
			return s;
		
		}
		protected void onPostExecute(Socket s){
			app.setSock(s);
			CheckBox cb=(CheckBox)findViewById(R.id.checkBox1);
			if(s==null)
			{
				cb.setChecked(false);
			}
			else if(s.isClosed() || !s.isConnected())
			{
				cb.setChecked(false);
				
			}
			else cb.setChecked(true);
			
		}
	
	}
}
