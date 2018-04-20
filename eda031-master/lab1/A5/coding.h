/*
 * code.h
 *
 *  Created on: Feb 1, 2016
 *      Author: mattias
 */

#ifndef CODE_H_
#define CODE_H_


class Coding {
public:
	static unsigned char encode(unsigned char c);

	static unsigned char decode(unsigned char c);

private:
	static const unsigned char key = 37;
};


#endif /* CODE_H_ */
