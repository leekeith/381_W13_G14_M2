/*
 * lib_bitmap.h
 *
 *  Created on: 2013-11-04
 *      Author: Keith
 */

#ifndef LIB_BITMAP_H_
#define LIB_BITMAP_H_
#include"lib_instr.h"

#define STD_W 320
#define STD_H 240

typedef struct dibhead
{
	unsigned int dib_size;
	unsigned int width;
	unsigned int height;
	unsigned short num_planes;
	unsigned short b_per_pixel;
	unsigned int compression_mode;
	unsigned int raw_size;
	unsigned int horiz_res;
	unsigned int vert_res;
	unsigned int palette_sz;
	unsigned int important_c_count;
}dibhead_t;

typedef struct bitmap
{
	unsigned char filename[16];
	unsigned char signature[2];
	unsigned int tot_size;
	unsigned short res1;
	unsigned short res2;
	unsigned int offset;
	dibhead_t dib;
	unsigned char* image;
}bitmap_t;

bitmap_t* scr16ToBitmap24(short* data, int width, int height);
int bitmapToSDcard(bitmap_t* bmp);

void fillPixel(unsigned short* image, int pix_offset, short color);
void fillColor(unsigned short* image, int pix_offset, short color, range_t* r);
void fillLine(unsigned short* image, int last_offset, int next_offset, short color, range_t* r);
#endif /* LIB_BITMAP_H_ */
