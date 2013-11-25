/*
 * rs232.h
 *
 *  Created on: 2013-11-20
 *      Author: Owner
 */



#ifndef RS232_H_
#define RS232_H_


#include <stdio.h>
#include "altera_up_avalon_rs232.h"
#include <string.h>
#include"sys/alt_irq.h"
#include<sys/alt_alarm.h>

typedef struct
{

} RS232;

alt_up_rs232_dev* rs232_inti();
void clear_fifo(alt_up_rs232_dev* uart, unsigned char* data, unsigned char* parity);
void make_command(alt_up_rs232_dev* uart, unsigned char instr[]);
void send_data(char message[], alt_up_rs232_dev* uart);

#endif /* RS232_H_ */
