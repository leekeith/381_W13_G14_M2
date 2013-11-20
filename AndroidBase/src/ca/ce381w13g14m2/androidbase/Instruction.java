package ca.ce381w13g14m2.androidbase;

import android.graphics.Color;

public class Instruction {
	
	private char cmd;
	private int pixel;
	private short color;
	
	public Instruction(char cmd, int pixel, short color)
	{
		this.cmd=cmd;
		this.pixel=pixel;
		this.color=color;
	}
	
	public void setCmd(char cmd)
	{
		this.cmd=cmd;
	}
	
	public void setPixel(int pixel)
	{
		this.pixel=pixel;
	}
	
	public void setColor(int color)
	{
		int r,g,b;
		r=Color.red(color);
		g=Color.green(color);
		b=Color.blue(color);
		this.color=(short)((r&0x0000f800)+(g&0x0000fc00>>5)+(b&0x0000f800>>11));
	}
	
	public char getCmd()
	{
		return this.cmd;
	}
	
	public int getPixel()
	{
		return this.pixel;
	}
	public int getColor()
	{
		return Color.rgb((int)(this.color&0xf800),(int)(this.color&0x07e0)<<5,(int)(this.color&0x003f)<<11);
	}
	
	public byte[] toTransmit(String msg)
	{
		byte[] seq=new byte[7+msg.length()];
		
		seq[0]=(byte)this.cmd;
		
		seq[1]=(byte)((this.pixel&0xff000000)>>24);
		seq[2]=(byte)((this.pixel&0x00ff0000)>>16);
		seq[3]=(byte)((this.pixel&0x0000ff00)>>8);
		seq[4]=(byte)(this.pixel&0x000000ff);
		
		seq[5]=(byte)((this.color&0xff00)>>8);
		seq[6]=(byte)(this.color&0x00ff);
		
		for(int i=0;i<msg.length();i++)
			seq[7+i]=(byte)msg.toCharArray()[i];
		
		if(seq.length==7+msg.length())
			return seq;
		else return new byte[0];
	}
	
	public String recieveInstruction(char[] seq)
	{
		String msg=new String();
		
		this.cmd=seq[0];
		
		this.pixel=(int)(seq[1]+(int)(seq[2]<<8)+(int)(seq[3]<<16)+(int)(seq[4]<<24));
		
		this.color=(short)(seq[5]+(short)(seq[6]<<8));
		
		for(int i=7;i<seq.length;i++)
			msg.toCharArray()[i-7]=seq[i];
		
		return msg;
	}

}
