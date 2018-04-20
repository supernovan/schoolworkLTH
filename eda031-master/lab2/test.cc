#include <iostream>
#include <fstream>
#include <string>
#include <vector>
#include <algorithm>
#include <ctype.h>
using namespace std;


int main() {
	string word;
	ofstream outfile ("test.txt");
	ifstream infile ("words1.txt");

	if (infile.is_open()) {
		while (getline(infile, word)) {
			transform(word.begin(), word.end(), word.begin(), ::tolower);
			outfile << word << " ";
			size_t len = word.length();

			if (len >= 3) {
				vector<string> stringVector;
				for (unsigned int i = 0; i<len-2; i++) {
					string tri = word.substr(i, 3);
					stringVector.push_back(tri);
				}
				sort(stringVector.begin(), stringVector.end());
				outfile << len-2 << " ";
				for (unsigned int i = 0; i<len-2; i++) {
					outfile << stringVector[i] << " ";
				}
				outfile << "\n";
			}
		}
	}
	else {
		cout << "Unable to open the file" << "\n";
	}
	outfile.close();
	infile.close();
}
