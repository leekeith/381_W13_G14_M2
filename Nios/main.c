/*
 * main.c
 *	Project:	EECE381BASE
 *  Created on: 2013-11-04
 *      Author: Keith
 */

#include<stdlib.h>
#include<stdio.h>
#include"wk_video.h"
#include"lib_bitmap.h"
#include"sdcard.h"

#define GRID_COLOR		0xFFFF

int main(int argc, char** argv)
{


	// Variable declarations
	alt_u8 grid_on,instr_isnew,buf_swap;
	int i;
	pixel_buffer_t* screen;
	instr_t instr;

	//Initializations
	screen=pixelInit();

	grid_on=1;
	instr_isnew=0;
	buf_swap=1;

	instr.cmd=NULL;
	instr.color=NULL;
	instr.pixel=NULL;
	for(i=0;i<121;i++)
		instr.message[i]=NULL;


	//SetUp
	clrScr(screen);

	//main loop

	while(1)
	{
		if(instr_isnew)
		{
			instr_isnew=0;
		}

		if(grid_on)
		{
			for(i=0;i<=10;i++)
			{
				drawLine(screen, i*(VRAM_W/10),0, i*(VRAM_W/10),VRAM_H, GRID_COLOR);
				drawLine(screen, 0,i*(VRAM_H/10), VRAM_W,i*(VRAM_H/10), GRID_COLOR);
			}
		}
		if(buf_swap)
		{
			swapBuffer(screen);
			buf_swap=0;
		}
	}

	return 0;
}
