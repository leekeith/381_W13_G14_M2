package ca.ce381w13g14m2.androidbase;

import java.net.Socket;
import java.util.Timer;

import android.app.Application;

public class TheApplication extends Application{
	public Socket sock;
	public Timer tcp_timer;
	public String ip="192.168.0.1";
	public Integer port=50002;
	

}
