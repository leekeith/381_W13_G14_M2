/*
 * main.c
 *	Project:	EECE381BASE
 *  Created on: 2013-11-04
 *      Author: Keith
 */

#include<stdlib.h>
#include<stdio.h>
#include<sys/alt_cache.h>
#include<sys/alt_alarm.h>
#include<sys/alt_irq.h>
#include"wk_video.h"
#include"lib_bitmap.h"
#include"sdcard.h"
#include"lib_instr.h"
#include"wk_io.h"

//Precomple definitions
#define NO_COMMS
#define GRID_COLOR		0xFFFF

//Globals
unsigned short* image;
unsigned short* back_buffer;
alt_u8 buf_swap;

alt_u32 isr_alarm(void* context)
{
	buf_swap=1;
	return 1;
}
//Draws white horizontal and vertical lines on-screen,
//dividing the screen on a 10x10 grid
void drawGrid(alt_up_pixel_buffer_dma_dev* screen)
{
	int i;
	drawLine(screen, 0,0, 0,VRAM_H-1, GRID_COLOR);
	drawLine(screen, VRAM_W-1,0, VRAM_W-1,VRAM_H-1, GRID_COLOR);
	drawLine(screen, 0,VRAM_H-1, VRAM_W-1,VRAM_H-1, GRID_COLOR);
	drawLine(screen, 0,0, VRAM_W-1,0, GRID_COLOR);
	for(i=1;i<10;i++)
	{
		drawLine(screen, i*(VRAM_W/10),0, i*(VRAM_W/10),VRAM_H-1, GRID_COLOR);
		drawLine(screen, 0,i*(VRAM_H/10), VRAM_W-1,i*(VRAM_H/10), GRID_COLOR);
	}
}

//Copies image data from DRAM to VRAM
//Pre: DRAM pointer must be set as uncached
void imgToBbuffer(unsigned short* img, unsigned short* bbuff, int w, int h)
{
	int i;
	for(i=0;i<(w*h);i++)
		bbuff[i]=img[i];
	return;
}

//Converts image data to BITMAP24 and generates a header, then writes to
//the SD card
void saveBmp(short* img)
{
	bitmap_t* the_bmp;
	the_bmp=scr16ToBitmap24(img, STD_W, STD_H);
	bitmapToSDcard(the_bmp);
	free(the_bmp);
}

void setRange(range_t* r, int p)
{
	if(p<r->start)
		r->start=p;
	if((p+1)>r->stop)
		r->stop=p+1;
	r->sync=1;
}

int main(int argc, char** argv)
{


	// Variable declarations
	alt_u8 grid_on,grid_was_on,instr_isnew,run,redraw;
	int i,nticks;
	alt_up_pixel_buffer_dma_dev* screen;
	instr_t instr;
	range_t* range;
	alt_alarm* alarm;

#ifdef NO_COMMS
	//Prebuilt instructions to test pixel buffer operation
	instr_t instrs[20];
	int instr_index=0;
	instrs[0].cmd=FILL_SCR;
	instrs[0].pixel=0;
	instrs[0].color=mkColor(127,0,255);
	for(i=1;i<=19;i++)
	{
		instrs[i].cmd=FILL_PIXEL;
		instrs[i].pixel=i+STD_W*5;
		instrs[i].color=mkColor(55,122,255);
	}
	//instrs[19].cmd=QUIT;
#endif

	//Initializations
	screen=pixelInit();
	alarm=(alt_alarm*)malloc(sizeof(alt_alarm));
	nticks=alt_ticks_per_second();


	//Boolean loop controllers
	grid_on=1;
	grid_was_on=0;
	instr_isnew=0;
	buf_swap=1;
	run=1;
	redraw=0;

	//Initialize current instruction to NULL
	instr.cmd=NONE;
	instr.color=0;
	instr.pixel=0;
	for(i=0;i<121;i++)
		instr.message[i]=0;

	range->start=STD_W*STD_H;
	range->stop=0;
	range->sync=0;
	//allocate DRAM image space
	image=(unsigned short*)malloc(STD_W*STD_H*sizeof(short));


	//SetUp
	clrScr(screen);
	back_buffer=alt_remap_uncached((void*)screen->back_buffer_start_address,STD_W*STD_H);
	if(alt_alarm_start(alarm,nticks/30,isr_alarm,NULL)<0)printf("\nNo Alarm 1\n");

	//main loop

	while(run)
	{
#ifdef NO_COMMS
		//Get next premaed instruction
		if(instr.cmd==NONE&&instr_index<20)
		{
			instr.cmd=instrs[instr_index].cmd;
			instr.pixel=instrs[instr_index].pixel;
			instr.color=instrs[instr_index].color;
			instr_index++;
			instr_isnew=1;
			//buf_swap=1;
		}
#endif
		//Check if grid status has changed
		grid_on=GET_SW&1;
		if(grid_on!=grid_was_on)
		{
			//redraw image to remove grid
			//buf_swap=1;
			grid_was_on=grid_on;
			redraw=1;
		}

		//Instruction execution
		if(instr_isnew)
		{
			instr_isnew=0;
			switch(instr.cmd)
			{
			case FILL_PIXEL:
				//Draws a single pixel
				fillPixel(image,instr.pixel,instr.color);
				setRange(range,instr.pixel);
				instr.cmd=NONE;
				redraw=1;
				break;
			case FILL_SCR:
				//Fills entire screen with selected color
				for(i=0;i<STD_W*STD_H;i++)
					fillPixel(image,i,instr.color);
				instr.cmd=NONE;
				redraw=1;
				break;
			case GET_PIXEL:
				//Transmit 16 bit color value from location 'instr.pixel' to host
				instr.color=image[instr.pixel];
				instr.cmd=NONE;
				break;
			case GET_W:
				//Transmit image width to host
				instr.pixel=STD_W;
				instr.cmd=NONE;
				break;
			case GET_H:
				//Transmit image height to host
				instr.pixel=STD_H;
				instr.cmd=NONE;
				break;
			case FILL_COLOR:
				//Fill every pixel with matching color value to 'instr.pixel'
				//to 'instr.color'
				fillColor(image,instr.pixel,instr.color,range);
				redraw=1;
				break;
			case QUIT:
				//Exit loop
				run=0;
				instr.cmd=NONE;
				break;
			case SAVE:
				//Save image as BITMAP24 to SD card
				saveBmp(image);
				instr.cmd=NONE;
				break;
			default:
				break;
			}
			strcpy(instr.message,"confirm");
			instr_send(&instr);
		}



		if(buf_swap)
		{
			//If the buffer is to be swapped, redraw grid and swap
			//printf("swap\n");

			if(redraw)
			{
				if(range->sync)
					imgToBbuffer(image+range->start,back_buffer+range->start,range->stop-range->start,1);
				else
					imgToBbuffer(image,back_buffer,STD_W,STD_H);
			}
			if(grid_on)
				drawGrid(screen);
			swapBuffer(screen);
			//update pointer to back buffer
			back_buffer=alt_remap_uncached((void*)screen->back_buffer_start_address,STD_W*STD_H);
			if(redraw)
			{
				if(range->sync)
				{
					imgToBbuffer((image+range->start),(back_buffer+range->start),(range->stop-range->start),1);
					range->start=STD_W*STD_H;
					range->stop=0;
					range->sync=0;
				}
				//Redraw image to back buffer
				else
					imgToBbuffer(image,back_buffer,STD_W,STD_H);
				redraw=0;
			}
			buf_swap=0;
			redraw=1;
		}
	}
	printf("Process terminated\n");
	free(screen);
	free(image);
	return 0;
}
