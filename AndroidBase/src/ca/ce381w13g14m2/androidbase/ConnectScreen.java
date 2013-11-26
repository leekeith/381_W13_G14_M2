package ca.ce381w13g14m2.androidbase;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
//import ca.ce381w13g14m2.androidbase.Info;

public class ConnectScreen extends Activity {
	TheApplication app;
	//Info data = new Info();
	@SuppressLint("NewApi")
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		// This call will result in better error messages if you
		// try to do things in the wrong thread.
		
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());

		
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connect_screen);		
		
		app=(TheApplication)getApplication();
		
		EditText et = (EditText) findViewById(R.id.RecvdMessage);
		et.setKeyListener(null);
		
		OnTouchListener ipBoxListener=new OnTouchListener(){
				@Override
				public boolean onTouch(View view, MotionEvent event)
				{
					EditText ipBox=(EditText)view;
					ipBox.setText("");
					return false;
				}
			};
		OnFocusChangeListener ipBoxNav=new OnFocusChangeListener(){

			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				EditText et=(EditText)view;
				if(!hasFocus && et.getText().toString().length()==0)
				{
					switch(et.getId())
					{
					case R.id.ip1:
						et.setText(app.getIpByte(0));
						break;
					case R.id.ip2:
						et.setText(app.getIpByte(1));
						break;
					case R.id.ip3:
						et.setText(app.getIpByte(2));
						break;
					case R.id.ip4:
						et.setText(app.getIpByte(3));
						break;
					default:
						break;	
					}
				}
				else if(hasFocus)
				{
					et.setText("");
				}
				
			}
		};
			
		
		findViewById(R.id.ip1).setOnTouchListener(ipBoxListener);
		findViewById(R.id.ip2).setOnTouchListener(ipBoxListener);
		findViewById(R.id.ip3).setOnTouchListener(ipBoxListener);
		findViewById(R.id.ip4).setOnTouchListener(ipBoxListener);
		
		findViewById(R.id.ip1).setOnFocusChangeListener(ipBoxNav);
		findViewById(R.id.ip2).setOnFocusChangeListener(ipBoxNav);
		findViewById(R.id.ip3).setOnFocusChangeListener(ipBoxNav);
		findViewById(R.id.ip4).setOnFocusChangeListener(ipBoxNav);
		
		
		
		// Set up a timer task.  We will use the timer to check the
		// input queue every 500 ms
		
		TCPReadTimerTask tcp_task = new TCPReadTimerTask();
		app.tcp_timer.schedule(tcp_task,3000, 500);
		
		
		}
	@Override
	public void onResume()
	{
		super.onResume();
		EditText et;
		et=(EditText)findViewById(R.id.ip1);
		et.setText(app.getIpByte(0).toString());
		et=(EditText)findViewById(R.id.ip2);
		et.setText(app.getIpByte(1).toString());
		et=(EditText)findViewById(R.id.ip3);
		et.setText(app.getIpByte(2).toString());
		et=(EditText)findViewById(R.id.ip4);
		et.setText(app.getIpByte(3).toString());
		et=(EditText)findViewById(R.id.port);
		et.setText(Integer.toString(app.getPort()));
		app=(TheApplication)getApplication();
		Log.w("ConnectScreen","IP: "+app.getAddr());
		CheckBox cb=(CheckBox)findViewById(R.id.check_box2);
		if(app.getSock()==null)
			cb.setChecked(false);
		else if(!app.getSock().isClosed() && app.getSock().isConnected())
			cb.setChecked(true);
		else cb.setChecked(false);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.connect_screen, menu);
		return true;
	}
	
	@Override
	public void onPause()
	{
		super.onPause();
		if(app.getSock()!=null)
		closeSocket(null);
	}
	// Route called when the user presses "connect"
	
	public void openSocket(View view) {
		Button button =(Button)findViewById(R.id.button_connect);
		button.setEnabled(false);
		
		// open the socket.  SocketConnect is a new subclass
	    // (defined below).  This creates an instance of the subclass
		// and executes the code in it.
		EditText text_ip;
		text_ip = (EditText) findViewById(R.id.ip1);
		app.setIpByte(0,text_ip.getText().toString());
		text_ip = (EditText) findViewById(R.id.ip2);
		app.setIpByte(1,text_ip.getText().toString());
		text_ip = (EditText) findViewById(R.id.ip3);
		app.setIpByte(2,text_ip.getText().toString());
		text_ip = (EditText) findViewById(R.id.ip4);
		app.setIpByte(3,text_ip.getText().toString());
		text_ip=(EditText)findViewById(R.id.port);
		app.setPort(Integer.parseInt(text_ip.getText().toString()));
		Log.w("ConnectScreen","IP: "+app.getAddr());
		new SocketConnect().execute((Void) null);
	}

	//  Called when the user wants to send a message
	
	public void sendMessage(View view) {
		
		// Get the message from the box
		
		EditText et = (EditText) findViewById(R.id.MessageText);
		String msg = et.getText().toString();

		// Create an array of bytes.  First byte will be the
		// message length, and the next ones will be the message
		
		byte buf[] = new byte[msg.length() + 1];
		buf[0] = (byte) msg.length(); 
		System.arraycopy(msg.getBytes(), 0, buf, 1, msg.length());

		// Now send through the output stream of the socket
		
		OutputStream out;
		try {
			out = app.getSock().getOutputStream();
			try {
				out.write(buf, 0, msg.length() + 1);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Called when the user closes a socket
	
	public void closeSocket(View view) {
		Socket s = app.getSock();
		try {
			s.getOutputStream().close();
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	
	// Construct an IP address from the four boxes
	
	public String getConnectToIP() {
		TheApplication app=(TheApplication)getApplication();
		String addr = "";
		EditText text_ip;
		text_ip = (EditText) findViewById(R.id.ip1);
		addr += text_ip.getText().toString();
		text_ip = (EditText) findViewById(R.id.ip2);
		addr += "." + text_ip.getText().toString();
		text_ip = (EditText) findViewById(R.id.ip3);
		addr += "." + text_ip.getText().toString();
		text_ip = (EditText) findViewById(R.id.ip4);
		addr += "." + text_ip.getText().toString();
		return addr;
	}

	// Gets the Port from the appropriate field.
	
	public Integer getConnectToPort() {
		Integer port;
		EditText text_port;

		text_port = (EditText) findViewById(R.id.port);
		port = Integer.parseInt(text_port.getText().toString());

		return port;
	}
	
	

    // This is the Socket Connect asynchronous thread.  Opening a socket
	// has to be done in an Asynchronous thread in Android.  Be sure you
	// have done the Asynchronous Tread tutorial before trying to understand
	// this code.
	
	public class SocketConnect extends AsyncTask<Void, Void, Socket> {

		// The main parcel of work for this thread.  Opens a socket
		// to connect to the specified IP.
		@Override
		protected Socket doInBackground(Void... voids) {
			Socket s = null;
			
			String ip = getConnectToIP();
			Integer port = getConnectToPort();

			try {
				s = new Socket(ip, port);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return s;
		}

		// After executing the doInBackground method, this is 
		// automatically called, in the UI (main) thread to store
		// the socket in this app's persistent storage
		@Override
		protected void onPostExecute(Socket s) {
			app.setSock(s);
			findViewById(R.id.button_connect).setEnabled(true);
			CheckBox cb=(CheckBox)findViewById(R.id.check_box2);
			if(app.getSock()==null)
				cb.setChecked(false);
			else if(app.getSock().isConnected() && !app.getSock().isClosed())
				cb.setChecked(true);
			else
				cb.setChecked(false);
			
		}
	}

	// This is a timer Task.  Be sure to work through the tutorials
	// on Timer Tasks before trying to understand this code.
	
	public class TCPReadTimerTask extends TimerTask {
		public void run() {
			if (app.getSock() != null && app.getSock().isConnected()
					&& !app.sock.isClosed()) {
				
				try {
					InputStream in = app.getSock().getInputStream();

					// See if any bytes are available from the Middleman
					
					int bytes_avail = in.available();
					if (bytes_avail > 0) {
						
						// If so, read them in and create a sring
						
						byte buf[] = new byte[bytes_avail];
						in.read(buf);

						final String s = new String(buf, 0, bytes_avail, "US-ASCII");
		
						// As explained in the tutorials, the GUI can not be
						// updated in an asyncrhonous task.  So, update the GUI
						// using the UI thread.
						
						runOnUiThread(new Runnable() {
							public void run() {
								EditText et = (EditText) findViewById(R.id.RecvdMessage);
								et.setText(s);
							}
						});
						
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	public void back(View view) {
		// TODO Auto-generated method stub
		
	}

	public void send(View view) {
		// TODO Auto-generated method stub
	
		String msg = "asdf";
		
		byte buf[] = new byte[msg.length() + 1];
		buf[0] = (byte) msg.length(); 
		System.arraycopy(msg.getBytes(), 0, buf, 1, msg.length());

		// Now send through the output stream of the socket
		
		OutputStream out;
		try {
			out = app.getSock().getOutputStream();
			try {
				out.write(buf, 0, msg.length() + 1);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
