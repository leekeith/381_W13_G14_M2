package ca.ce381w13g14m2.androidbase;

import java.net.Socket;
import java.util.Timer;

import android.app.Application;

public class TheApplication extends Application{
	protected Socket sock;
	public Timer tcp_timer;
	protected String ip[]={"192","168","0","102"};
	protected Integer port=new Integer(50002);
	
	public void setSock(Socket s)
	{
		this.sock=s;
	}
	
	public void setIpByte(int index, String byteval)
	{
		this.ip[index]=byteval;
	}
	
	public void setPort(Integer port)
	{
		this.port=port;
	}
	
	public String getAddr()
	{
		return ip[0]+"."+ip[1]+"."+ip[2]+"."+ip[3];
	}
	
	public String getIpByte(int index)
	{
		return ip[index];
	}
	
	public Socket getSock()
	{
		return this.sock;
	}
	
	public Integer getPort()
	{
		return this.port;
	}
	
}
