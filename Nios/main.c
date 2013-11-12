/*
 * main.c
 *	Project:	EECE381BASE
 *  Created on: 2013-11-04
 *      Author: Keith
 */

#include<stdlib.h>
#include<stdio.h>
#include<sys/alt_cache.h>
#include"wk_video.h"
#include"lib_bitmap.h"
#include"sdcard.h"
#include"lib_instr.h"
#define NO_COMMS

#define GRID_COLOR		0xFFFF

unsigned short* image;
unsigned short* back_buffer;

void drawGrid(alt_up_pixel_buffer_dma_dev* screen)
{
	int i;
	for(i=0;i<=10;i++)
	{
		drawLine(screen, i*(VRAM_W/10),0, i*(VRAM_W/10),VRAM_H, GRID_COLOR);
		drawLine(screen, 0,i*(VRAM_H/10), VRAM_W,i*(VRAM_H/10), GRID_COLOR);
	}
}

void imgToBbuffer(unsigned short* img, unsigned short* bbuff, int w, int h)
{
	int i;
	for(i=0;i<(w*h);i++)
		bbuff[i]=img[i];
	return;
}

void saveBmp(short* img)
{
	bitmap_t* the_bmp;
	the_bmp=scr16ToBitmap24(img, STD_W, STD_H);
	bitmapToSDcard(the_bmp);
	free(the_bmp);
}

int main(int argc, char** argv)
{


	// Variable declarations
	alt_u8 grid_on,instr_isnew,buf_swap,run;
	int i;
	alt_up_pixel_buffer_dma_dev* screen;
	instr_t instr;
#ifdef NO_COMMS
	instr_t instrs[20];
	int instr_index=0;
	instrs[0].cmd=FILL_SCR;
	instrs[0].pixel=0;
	instrs[0].color=mkColor(255,255,255);
	for(i=1;i<19;i++)
	{
		instrs[i].cmd=FILL_PIXEL;
		instrs[i].pixel=i+STD_W*5;
		instrs[i].color=mkColor(255,0,0);
	}
	instrs[19].cmd=QUIT;
#endif

	//Initializations
	screen=pixelInit();

	grid_on=1;
	instr_isnew=0;
	buf_swap=1;
	run=1;

	instr.cmd=NONE;
	instr.color=0;
	instr.pixel=0;
	for(i=0;i<121;i++)
		instr.message[i]=0;

	image=(unsigned short*)malloc(STD_W*STD_H*sizeof(short));


	//SetUp
	clrScr(screen);

	//main loop

	while(run)
	{
#ifdef NO_COMMS
		instr.cmd=instrs[instr_index].cmd;
		instr.pixel=instrs[instr_index].pixel;
		instr.color=instrs[instr_index].color;
		instr_index++;
		instr_isnew=1;
		buf_swap=1;
#endif
		if(instr_isnew)
		{
			instr_isnew=0;
			switch(instr.cmd)
			{
			case FILL_PIXEL:
				fillPixel(image,instr.pixel,instr.color);
				break;
			case FILL_SCR:
				for(i=0;i<STD_W*STD_H;i++)
					fillPixel(image,i,instr.color);
				break;
			case GET_PIXEL:
				instr.color=image[instr.pixel];
				break;
			case GET_W:
				instr.pixel=STD_W;
				break;
			case GET_H:
				instr.pixel=STD_H;
				break;
			case FILL_COLOR:
				fillColor(image,instr.pixel,instr.color);
				break;
			case QUIT:
				run=0;
				break;
			case SAVE:
				saveBmp(image);
				break;
			default:
				break;
			}
			imgToBbuffer(image,back_buffer,STD_W,STD_H);
			strcpy(instr.message,"confirm");
			instr_send(instr);
		}


		if(buf_swap)
		{
			if(grid_on)
				drawGrid(screen);

			swapBuffer(screen);
			back_buffer=alt_remap_uncached((void*)screen->back_buffer_start_address,STD_W*STD_H);
			buf_swap=0;
		}
	}
	printf("Process terminated\n");
	free(screen);
	free(image);
	return 0;
}
