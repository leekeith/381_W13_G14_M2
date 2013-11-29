package ca.ce381w13g14m2.androidbase;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

import android.R.color;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

/*
 * DoIP Android
 * ============
 * Main Activity:	Main ui thread.  Contains drawView and buttons for
 * 					selecting color and instruction type
 * Author: Syed R, Jon M, Keith L, Karl K
 */
public class MainActivity extends Activity {
	DrawView drawView;
    SeekBar seekbar;
    static int brushWidth;
    TheApplication app;
    
    //Called when activity starts
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//configure views
		drawView=(DrawView)findViewById(R.id.drawView1);
		seekbar = (SeekBar) findViewById(R.id.seekBar1);
		
		
		seekbar.setOnSeekBarChangeListener( new OnSeekBarChangeListener(){
			
			//Seekbar to set width of brush
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
		        brushWidth = progress;
			}
			
			//Do-nothing events
			@Override
			public void onStartTrackingTouch(SeekBar arg0) {}
			@Override
			public void onStopTrackingTouch(SeekBar arg0) {}
		}); 
		
		//Configure application settings
		app=(TheApplication)getApplication();
		app.tcp_timer=new Timer();
		
		app.instr.setColor(Color.RED);
		drawView.setOnTouchListener(new OnTouchListener(){
			//Override drawView's onTouch with app context
			public boolean onTouch(View view, MotionEvent event)
			{
				drawView.onTouch(view, event);
				app.instr.setColor(drawView.color);
				app.instr.setCmd(drawView.cmd);
				app.instr.setPixel(drawView.i);
				
				return true;
			}
		});
		drawView.radio=(RadioGroup)findViewById(R.id.radioGroup1);
		drawView.radio.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			//Store the instruction type to return to after fillColor
			@Override
			public void onCheckedChanged(RadioGroup radios, int checked) {
				if(checked!=R.id.radio_fill_color)
					drawView.oldRadioId=checked;
			}
		});
		drawView.oldRadioId=drawView.radio.getCheckedRadioButtonId();
		
	}
	
	//Reconnects to middleman when returning to this activity
	@Override
	public void onResume()
	{
		super.onResume();
		TCPTimerTask task=new TCPTimerTask();
		app.tcp_timer.schedule(task,3000,50);
		if(app.getSock()!=null)
		{
			findViewById(R.id.check_box2).setEnabled(false);
			new SocketConnect().execute(app);
		}
		Log.w("MainActivity", "app.ip="+app.getAddr()+"; app.port="+app.getPort().toString());
		
		
	}
	
	//Display options menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	//Call ConnectScreen when "Connect" clicked.
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
		switch (item.getItemId()) {
	        case R.id.action_connect:
	        	Intent i=new Intent(this,ConnectScreen.class);
	              startActivity(i);
	              return true;
	      //  case R.id.action_settings:
	      //      return true;
	        default:
	            return super.onOptionsItemSelected(item);
		}
    }
	
	//Close TCP connection (if open) on pause
	@Override
	public void onPause()
	{
		super.onPause();
		if(app.getSock()!=null)
			closeSocket(null);
	}
	
	//Send fill screen command to middleman
	public void fill_screen(View view)
	{	
		drawView.clear = true;
		drawView.postInvalidate();
		View draw_area = (ca.ce381w13g14m2.androidbase.DrawView) findViewById(R.id.drawView1);
		draw_area.setBackgroundColor(DrawView.color);
		app.instr.setCmd(instr_type.FILL_SCR);
		app.instr.setColor(DrawView.color);
		app.instr.setPixel(0);
	}
	
<<<<<<< HEAD
	//Set brush color when color box pressed
	//======================================
=======
	public void color_red(View view) {
		DrawView.color = Color.RED;
		color_border();
		view.setBackgroundResource(R.drawable.red_border);
	}
>>>>>>> d2913f8da0c29e1ce67f562036f019d80fd0fcdf
	public void color_blue(View view) {
		DrawView.color = Color.BLUE;
		color_border();
		view.setBackgroundResource(R.drawable.blue_border);
	}
	public void color_yellow(View view) {
		DrawView.color = Color.YELLOW;
		color_border();
		view.setBackgroundResource(R.drawable.yellow_border);
	}
	public void color_green(View view) {
		DrawView.color = Color.GREEN;
		color_border();
		view.setBackgroundResource(R.drawable.green_border);
	}
	public void color_white(View view) {
		DrawView.color = Color.WHITE;
		color_border();
		view.setBackgroundResource(R.drawable.white_border);
	}
	public void color_black(View view) {
		DrawView.color = Color.BLACK;
		color_border();
		view.setBackgroundResource(R.drawable.black_border);
	}
	public void color_cyan(View view) {
		DrawView.color = Color.CYAN;
		color_border();
		view.setBackgroundResource(R.drawable.cyan_border);
	}
	public void color_magenta(View view) {
		DrawView.color = Color.MAGENTA;
		color_border();
		view.setBackgroundResource(R.drawable.magenta_border);
	}
	public void color_dkgray(View view) {
		DrawView.color = Color.DKGRAY;
		color_border();
		view.setBackgroundResource(R.drawable.dkgray_border);
	}
<<<<<<< HEAD
	
	//Clear drawView and send a FillScr(White)
	//command to middleman
=======

	public void color_border()
	{
		View red = (ImageButton) findViewById(R.id.ImageButton01);
		View blue = (ImageButton) findViewById(R.id.ImageButton02);
		View yellow = (ImageButton) findViewById(R.id.ImageButton03);
		View green = (ImageButton) findViewById(R.id.ImageButton04);
		View cyan = (ImageButton) findViewById(R.id.ImageButton05);
		View magenta = (ImageButton) findViewById(R.id.ImageButton06);
		View white = (ImageButton) findViewById(R.id.ImageButton07);
		View dkgray = (ImageButton) findViewById(R.id.ImageButton08);
		View black = (ImageButton) findViewById(R.id.ImageButton09);

		red.setBackgroundResource(R.drawable.red);
		blue.setBackgroundResource(R.drawable.blue);
		yellow.setBackgroundResource(R.drawable.yellow);
		green.setBackgroundResource(R.drawable.green);
		cyan.setBackgroundResource(R.drawable.cyan);
		magenta.setBackgroundResource(R.drawable.magenta);
		white.setBackgroundResource(R.drawable.white);
		dkgray.setBackgroundResource(R.drawable.dkgray);
		black.setBackgroundResource(R.drawable.black);
	}

>>>>>>> d2913f8da0c29e1ce67f562036f019d80fd0fcdf
	public void onClear(View view)
	{
		View draw_area = (ca.ce381w13g14m2.androidbase.DrawView) findViewById(R.id.drawView1);
		draw_area.setBackgroundResource(R.drawable.grid);
		drawView.clear = true;
		drawView.postInvalidate();
        CheckBox check = (CheckBox) findViewById(R.id.CheckBox01);
        check.setChecked(true);
		app.instr.setCmd(instr_type.FILL_SCR);
		app.instr.setColor(Color.WHITE);
		app.instr.setPixel(0);
	}
	
	public void showGrid(View view)
	{
        CheckBox check = (CheckBox) findViewById(R.id.CheckBox01);
		View draw_area = (ca.ce381w13g14m2.androidbase.DrawView) findViewById(R.id.drawView1);
		if (check.isChecked())
			draw_area.setBackgroundResource(R.drawable.grid);
		else
			draw_area.setBackgroundColor(Color.WHITE);
	}
	
	//Disconnect from middleman
	public void closeSocket(View view) {
		Socket s = app.getSock();
		try {
			s.getOutputStream().close();
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//Asynch task connecting to middleman over TCP port
	public class SocketConnect extends AsyncTask<TheApplication, Void, Socket>{
		//While normal operation continues, wait for connection
		protected Socket doInBackground(TheApplication... apps){
			Socket s=null;
			try{
				s=new Socket(apps[0].getAddr(),apps[0].getPort());
			}
			//Debug connection failiures
			catch(UnknownHostException e){
				Log.w("SocketConnect", "Unknown Host");
				e.printStackTrace();
			} catch (IOException e) {
				Log.w("SocketConnect", "IO Exception");
				e.printStackTrace();
			}
			
			return s;
		
		}
		
		//When connection complete, pass socket to Application
		//Indicate connected via checkbox
		protected void onPostExecute(Socket s){
			app.setSock(s);
			CheckBox cb=(CheckBox)findViewById(R.id.check_box2);
			cb.setEnabled(true);
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
	
	
	//Task to be done on timer interrupt
	//Checks for incomming instructions and sends any pending instructions
	public class TCPTimerTask extends TimerTask{
		
		//Runs on every timer event.
		@Override
		public void run() {
			InputStream in;
			OutputStream out;
			if (app.getSock() != null && app.getSock().isConnected()&& !app.sock.isClosed()) {
				//Get instruction
				byte[] buf=new byte[7];
				Instruction instr;
				try {
					in = app.getSock().getInputStream();

					// See if any bytes are available from the Middleman

					int bytes_avail = in.available();
					if (bytes_avail >=7) {
						in.read(buf);
						instr=new Instruction(buf);
						Log.i("TimerTask","Cmd:"+instr.getCmd().toString()+" P:"+Integer.toHexString(instr.getPixel())+" C:("+Color.red(instr.getColor())+","+Color.green(instr.getColor())+","+Color.blue(instr.getColor())+")");					}
				}catch (IOException e) {
					e.printStackTrace();
				}
				
				if(app.instr.getCmd()!=instr_type.NONE)
				{
					//if instruction pending, transmit
					try{
						out=app.getSock().getOutputStream();
						out.write(app.instr.toTransmit(""));
						app.instr.setCmd(instr_type.NONE);
					}catch (IOException e) {
						e.printStackTrace();
					} 
				}
			}
		}
	}
	

}
