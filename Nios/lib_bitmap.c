/*
 * lib_bitmap.c
 *
 *  Created on: 2013-11-04
 *      Author: Keith
 */
#include<stdlib.h>
#include<string.h>
#include"sdcard.h"
#include"lib_bitmap.h"
#include<altera_up_sd_card_avalon_interface.h>

#define ABS(x)	((x >= 0) ? (x) : (-(x)))


void color16to24(char* c24,short c16)
{
	short temp;
	temp=c16&(0xf800);
	c24[0]=(char)(temp>>8);
	temp=c16&(0x07E0);
	c24[1]=(char)(temp>>3);
	temp=c16&(0x001F);
	c24[2]=(char)(temp<<3);
}

bitmap_t* scr16ToBitmap24(short* data, int width, int height)
{
	int numBytes,rowPad,i,j;
	char c24[3];

	bitmap_t* the_bmp;

	numBytes=width*height*3;
	rowPad=(width*3)%4;

	the_bmp=(bitmap_t*)malloc(sizeof(bitmap_t));
	the_bmp->image=(unsigned char*)malloc(numBytes*sizeof(char));

	strcpy((char*)the_bmp->filename,"scrcap.bmp");
	the_bmp->signature[0]='B';
	the_bmp->signature[1]='M';
	the_bmp->tot_size=0x36+numBytes;
	the_bmp->res1=0;
	the_bmp->res2=0;
	the_bmp->offset=0x36;

	the_bmp->dib.dib_size=40;
	the_bmp->dib.width=width;
	the_bmp->dib.height=height;
	the_bmp->dib.num_planes=1;
	the_bmp->dib.b_per_pixel=24;
	the_bmp->dib.compression_mode=0;
	the_bmp->dib.raw_size=numBytes;
	the_bmp->dib.horiz_res=0;
	the_bmp->dib.vert_res=0;
	the_bmp->dib.palette_sz=0;
	the_bmp->dib.important_c_count=0;

	for(i=0;i<height;i++)
	{
		for(j=0;j<rowPad;j++)
			the_bmp->image[i*(width*3+rowPad)-rowPad+j]=0;

		for(j=0;j<(width*3);j+=3)
		{
			color16to24(c24, data[i*width+j]);
			the_bmp->image[i*(width*3+rowPad)+j]=c24[0];
			the_bmp->image[i*(width*3+rowPad)+j+1]=c24[1];
			the_bmp->image[i*(width*3+rowPad)+j+2]=c24[2];
		}

	}
	return the_bmp;
}

int bitmapToSDcard(bitmap_t* bmp)
{
	unsigned char* data;
	unsigned int i,timeout;
	alt_u8 create;
	create=1;
	short int handle;
	if(!sdcard_present())
		return -1;
	if(!sdcard_FAT16())
		return -1;

	data=(unsigned char*)malloc(bmp->offset*sizeof(char));

	data[0]=bmp->signature[0];
	data[1]=bmp->signature[1];

	data[2]=((char*)(&bmp->tot_size))[0];
	data[3]=((char*)(&bmp->tot_size))[1];
	data[4]=((char*)(&bmp->tot_size))[2];
	data[5]=((char*)(&bmp->tot_size))[3];

	data[6]=((char*)(&bmp->res1))[0];
	data[7]=((char*)(&bmp->res1))[1];

	data[8]=((char*)(&bmp->res2))[0];
	data[9]=((char*)(&bmp->res2))[1];

	data[10]=((char*)(&bmp->offset))[0];
	data[11]=((char*)(&bmp->offset))[1];
	data[12]=((char*)(&bmp->offset))[2];
	data[13]=((char*)(&bmp->offset))[3];

	data[14]=((char*)(&bmp->dib.dib_size))[0];
	data[15]=((char*)(&bmp->dib.dib_size))[1];
	data[16]=((char*)(&bmp->dib.dib_size))[2];
	data[17]=((char*)(&bmp->dib.dib_size))[3];

	data[18]=((char*)(&bmp->dib.width))[0];
	data[19]=((char*)(&bmp->dib.width))[1];
	data[20]=((char*)(&bmp->dib.width))[2];
	data[21]=((char*)(&bmp->dib.width))[3];

	data[22]=((char*)(&bmp->dib.height))[0];
	data[23]=((char*)(&bmp->dib.height))[1];
	data[24]=((char*)(&bmp->dib.height))[2];
	data[25]=((char*)(&bmp->dib.height))[3];

	data[26]=((char*)(&bmp->dib.num_planes))[0];
	data[27]=((char*)(&bmp->dib.num_planes))[1];

	data[28]=((char*)(&bmp->dib.b_per_pixel))[0];
	data[29]=((char*)(&bmp->dib.b_per_pixel))[1];

	data[30]=((char*)(&bmp->dib.compression_mode))[0];
	data[31]=((char*)(&bmp->dib.compression_mode))[1];
	data[32]=((char*)(&bmp->dib.compression_mode))[2];
	data[33]=((char*)(&bmp->dib.compression_mode))[3];

	data[34]=((char*)(&bmp->dib.raw_size))[0];
	data[35]=((char*)(&bmp->dib.raw_size))[1];
	data[36]=((char*)(&bmp->dib.raw_size))[2];
	data[37]=((char*)(&bmp->dib.raw_size))[3];

	data[38]=((char*)(&bmp->dib.horiz_res))[0];
	data[39]=((char*)(&bmp->dib.horiz_res))[1];
	data[40]=((char*)(&bmp->dib.horiz_res))[2];
	data[41]=((char*)(&bmp->dib.horiz_res))[3];

	data[42]=((char*)(&bmp->dib.vert_res))[0];
	data[43]=((char*)(&bmp->dib.vert_res))[1];
	data[44]=((char*)(&bmp->dib.vert_res))[2];
	data[45]=((char*)(&bmp->dib.vert_res))[3];

	data[46]=((char*)(&bmp->dib.palette_sz))[0];
	data[47]=((char*)(&bmp->dib.palette_sz))[1];
	data[48]=((char*)(&bmp->dib.palette_sz))[2];
	data[49]=((char*)(&bmp->dib.palette_sz))[3];

	data[50]=((char*)(&bmp->dib.important_c_count))[0];
	data[51]=((char*)(&bmp->dib.important_c_count))[1];
	data[52]=((char*)(&bmp->dib.important_c_count))[2];
	data[53]=((char*)(&bmp->dib.important_c_count))[3];

	timeout=0;
	do{
	handle=sdcard_fopen((char*)bmp->filename, create);
	timeout++;
	create=0;
	}while(handle<0 && timeout<50);

	if(handle<0)
		return handle;
	//char b;
	//sdcard_writefile_sz((char*)data,handle,bmp->offset);
	for(i=0;i<bmp->offset;i++)
	{
		alt_up_sd_card_write(handle, data[i]);
	}

	for(i=0;i<bmp->dib.raw_size;i++)
	{
		alt_up_sd_card_write(handle, bmp->image[i]);
		//if(b==0)printf("%d\n",i);
	}

	//sdcard_writefile_sz((char*)bmp->image,handle,bmp->dib.raw_size);

	//alt_up_sd_card_write(handle,-1);



	sdcard_fclose(handle);

	timeout=0;
		do{
		handle=sdcard_fopen((char*)bmp->filename, create);
		timeout++;
		create=0;
		}while(handle<0 && timeout<50);

		if(handle<0)
			return handle;
		//char b;
		//sdcard_writefile_sz((char*)data,handle,bmp->offset);
		for(i=0;i<bmp->offset;i++)
		{
			alt_up_sd_card_write(handle, data[i]);
		}



	free(data);
	return 0;
}

void fillPixel(unsigned short* image, int pix_offset, short color)
{
	image[pix_offset]=color;
}

void fillColor(unsigned short* image, int pix_offset, short color, range_t* r)
{
	int i;
	unsigned short startColor=image[pix_offset];
	r->start=STD_H*STD_W;
	r->stop=0;
	for(i=0;i<STD_W*STD_H;i++)
	{
		if(image[i]==startColor)
		{
			if(i<r->start)
				r->start=i;
			if(i>r->stop)
				r->stop=i;
			fillPixel(image,i,color);
		}
	}
}


//Adapted for use from altera_up_avalon_pixel_buffer_dma.c; Thanks, Altera!
void fillLine(unsigned short* image, int last_offset, int next_offset, short color, range_t* r)
{
	if(last_offset<r->start)
		r->start=last_offset;
	if(next_offset<r->start)
		r->start=next_offset;
	if(last_offset>r->stop)
		r->stop=last_offset;
	if(next_offset>r->stop)
		r->stop=next_offset;
	r->sync=1;

	register int x_0,y_0,x_1,y_1;
	register char steep;
	register int deltax,deltay,error,ystep,x,y;

	y_0=last_offset/STD_W;
	x_0=last_offset%STD_W;

	y_1=next_offset/STD_W;
	x_1=next_offset%STD_W;

	steep=(ABS(y_1-y_0))>ABS(x_1-x_0)? 1:0;

	/* Preprocessing inputs */
		if (steep > 0) {
			// Swap x_0 and y_0
			error = x_0;
			x_0 = y_0;
			y_0 = error;
			// Swap x_1 and y_1;
			error = x_1;
			x_1 = y_1;
			y_1 = error;
		}
		if (x_0 > x_1) {
			// Swap x_0 and x_1
			error = x_0;
			x_0 = x_1;
			x_1 = error;
			// Swap y_0 and y_1
			error = y_0;
			y_0 = y_1;
			y_1 = error;
		}

		/* Setup local variables */
		deltax = x_1 - x_0;
		deltay = ABS(y_1 - y_0);
		error = -(deltax / 2);
		y = y_0;
		if (y_0 < y_1)
			ystep = 1;
		else
			ystep = -1;

		/* Draw a line - either go along the x axis (steep = 0) or along the y axis (steep = 1). The code is replicated to
		 * compile well on low optimization levels. */
		if (steep == 1)
		{
			for (x=x_0; x <= x_1; x++) {
				fillPixel(image, x*STD_W+y,color);
				error = error + deltay;
				if (error > 0) {
					y = y + ystep;
					error = error - deltax;
				}
			}
		}
		else
		{
			for (x=x_0; x <= x_1; x++) {
				fillPixel(image,y*STD_W+x,color);
				error = error + deltay;
				if (error > 0) {
					y = y + ystep;
					error = error - deltax;
				}
			}
		}
	}




