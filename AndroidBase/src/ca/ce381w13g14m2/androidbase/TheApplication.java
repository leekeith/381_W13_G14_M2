package ca.ce381w13g14m2.androidbase;

import java.net.Socket;
import java.util.Timer;

import android.app.Application;
import android.widget.RadioGroup;

/*
 * DoIP Android
 * ============
 * TheApplication: 	Container for activities.  Holds global
 * 					values to be carried over between these
 * 					activities
 * Author:			Keith L
 */
public class TheApplication extends Application{
	protected Socket sock;
	public Timer tcp_timer;
	protected String ip[];
	protected Integer port;
	public Instruction instr;
	
	//Constructor
	public TheApplication()
	{
		sock=null;
		tcp_timer=null;
		ip=new String[]{"192","168","0","102"};
		port=50002;
		instr=new Instruction(instr_type.NONE,0,(short)0);
	}
	
	//Setters
	//=======
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
	
	//Getters
	//=======
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
