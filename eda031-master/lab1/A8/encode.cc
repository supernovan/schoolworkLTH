///*
// *  encode.cc
// *
// *  Created on: Feb 1, 2016
// *      Author: mattias
// */
//#include <iostream>
//#include <fstream>
//#include <string>
//
//#include "coding.h"
//using namespace std;
//
//int main(int argc, char* argv[])
//{
//	if (argc != 2) {
//		cout <<"Error, either too many or too few input names \n";
//		return(1);
//	}
//	string file = argv[1];
//	ifstream infile(file);
//	ofstream outfile(file + ".enc");
//	if (!infile && !outfile) {
//		char c;
//		while(infile.get(c)) {
//			outfile << Coding::encode(c);
//		}
//	} else {
//		std::cout << "Couldn't open one of the files \n";
//		return 1;
//	}
//	infile.close();
//	outfile.close();
//}
