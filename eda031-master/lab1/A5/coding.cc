/*
 * coding.cc
 *
 *  Created on: Feb 1, 2016
 *      Author: mattias
 */
#include "coding.h"


unsigned char Coding::encode(unsigned char c) {
	return c^key;
}

unsigned char Coding::decode(unsigned char c) {
	return c^key;
}



