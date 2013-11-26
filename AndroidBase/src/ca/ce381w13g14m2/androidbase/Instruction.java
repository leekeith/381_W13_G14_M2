package ca.ce381w13g14m2.androidbase;

import android.graphics.Color;
/*
 * DoIP Android
 * ============
 * Instruction: Data structure containing all required fields
 * 				for transmitting an instruction over TCP connection
 * 				Contains methods for converting a bytestream to an
 * 				instruction and an instruction into a bytestream
 * Author: Keith L
 */
public class Instruction {
	
	private instr_type cmd;
	private int pixel;
	private short color;
	
	// Constructor for given command
	public Instruction(instr_type cmd, int pixel, short color)
	{
		this.cmd=cmd;
		this.pixel=pixel;
		this.color=color;
	}
	
	// Constructor for given bytestream
	public Instruction(byte buffer[])
	{
		this.recieveInstruction(buffer);
	}
	
	// Setters
	// =======
	public void setCmd(instr_type cmd)
	{
		this.cmd=cmd;
	}
	
	public void setPixel(int pixel)
	{
		if(pixel>0)
			this.pixel=pixel;
		else
			this.pixel=0;
	}
	
	public void setColor(int color)
	{
		int r,g,b;
		r=Color.red(color);
		g=Color.green(color);
		b=Color.blue(color);
		this.color=(short)(((r&0xF8)<<8)+((g&0xFC)<<3)+((b&0xf8)>>3));
	}
	
	//Getters
	//=======
	public instr_type getCmd()
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
	
	//Convert instruction into bytestream for transmission
	public byte[] toTransmit(String msg)
	{
		byte[] seq=new byte[7+msg.length()];
		
		seq[0]=(byte)this.cmd.getValue();
		
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
	
	//Populate instruction from bytestream
	public String recieveInstruction(byte[] seq)
	{
		String msg=new String();
		
		this.cmd=instr_type.getType((char)seq[0]);
		
		this.pixel=(int)((int)seq[4]&0xff | (seq[3]<<8)&0xff00 | (seq[2]<<16)&0xff0000 | (seq[1]<<24)&0xff000000);
		
		this.color=(short)(seq[5]+(short)(seq[6]<<8));
		
		for(int i=7;i<seq.length;i++)
			msg.toCharArray()[i-7]=(char)seq[i];
		
		return msg;
	}

}
