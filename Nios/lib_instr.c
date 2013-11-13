/*
 * lib_instr.c
 *
 *  Created on: 2013-11-06
 *      Author: Keith
 */
#include"lib_instr.h"

void instr_make(instr_t* instr, char string[128])
{
	int i;
	instr->cmd=string[0];
	instr->pixel=string[1]&(string[2]<<8)&(string[3]<<16)&(string[4]<<24);
	instr->color=string[5]&(string[6]<<8);
	for(i=0;i<120 && string[7+i]!=0;i++)
		instr->message[i]=string[7+i];
	instr->message[i]=0;
}

void instr_send(instr_t* instr)
{
	char string[128];
	int o;
	string[0]=instr->cmd;
	string[1]=instr->pixel&0xff;
	string[2]=(instr->pixel&0xff00)>>8;
	string[3]=(instr->pixel&0xff0000)>>16;
	string[4]=(instr->pixel&0xff000000)>>24;
	string[5]=instr->color&0xff;
	string[6]=(instr->color&0xff00)>>8;
	for(o=7;o<128;o++)
		string[o]=instr->message[o-7];
	//Send string to Android
	printf(instr->message);
	printf("\n");
}

