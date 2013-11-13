/*
 * lib_instr.h
 *
 *  Created on: 2013-11-06
 *      Author: Keith
 */

#ifndef LIB_INSTR_H_
#define LIB_INSTR_H_

typedef enum e_instr_type{
	FILL_PIXEL='P',
	FILL_SCR='F',
	SAVE='S',
	GET_PIXEL='G',
	GET_W='W',
	GET_H='H',
	FILL_COLOR='C',
	QUIT='X',
	NONE='0',
	CONFIRM='N'
}instr_type;

typedef struct e_instr
{
	instr_type cmd;
	unsigned int pixel;
	unsigned short color;
	char message[121];
}instr_t;

typedef struct e_range
{
int start,stop,pos,sync;
}range_t;

void instr_make(instr_t* instr, char string[128]);
void instr_send(instr_t* instr);

#endif /* LIB_INSTR_H_ */
