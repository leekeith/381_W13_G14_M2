/*
 * rs232.c
 *
 *  Created on: 2013-11-20
 *      Author: Owner
 */



#include <stdio.h>
#include "altera_up_avalon_rs232.h"
#include <string.h>
#include"sys/alt_irq.h"
#include<sys/alt_alarm.h>
#include "rs232.h"
#include"lib_instr.h"

//Set up rs232
alt_up_rs232_dev* rs232_inti(){
	alt_up_rs232_dev* uart;
	uart = alt_up_rs232_open_dev(RS232_NAME);
	return uart;
}

//Clear read fifo
void clear_fifo(alt_up_rs232_dev* uart, unsigned char* data, unsigned char* parity){
	  while(alt_up_rs232_get_used_space_in_read_FIFO(uart)){
		  	  alt_up_rs232_read_data(uart, data, parity);
	  }
}

//Make a new command if new command detected, return a string when a command is completed
void make_command(alt_up_rs232_dev* uart, unsigned char instr[]){
	unsigned char data,parity;
    int counter = 0;
    //char stringr[7];
    if(alt_up_rs232_get_used_space_in_read_FIFO(uart)>0)
    {
		while(alt_up_rs232_get_used_space_in_read_FIFO(uart) <7);

		while(counter < 7){
			alt_up_rs232_read_data(uart, &data, &parity);
			if(counter==0)
			{
				while(data!=FILL_PIXEL && data!=FILL_SCR && data!=SAVE && data!=FILL_COLOR && data!=QUIT && data!=LINE_START && data!=LINE_PT && data!=LINE_END)
					alt_up_rs232_read_data(uart, &data, &parity);
			}
			instr[counter] = data;
			printf("%X",instr[counter]);
			counter++;
		}
		printf("\n");
    }
}

//Sends data out when write fifo has space
void send_data(char message[], alt_up_rs232_dev* uart){
	int i = 0;
	while(i< 7){
		if(alt_up_rs232_get_available_space_in_write_FIFO(uart) > 50){
		  	  for (i=0; i<7; i++)
			  alt_up_rs232_write_data(uart, message[i]);
		}
	}
}
