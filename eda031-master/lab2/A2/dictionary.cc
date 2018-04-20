#include <string>
#include <vector>
#include <fstream>
#include <iostream>
#include <algorithm>
#include "dictionary.h"

using namespace std;

Dictionary::Dictionary() {
	ifstream infile ("web2.txt");
    string word;
    if (infile.is_open()) {
        while (getline(infile, word)) {
            transform(word.begin(), word.end(), word.begin(), ::tolower);
            dict.insert(word);
        }
    }
    else {
        cerr << "Could not open the file\n";
        exit(1);
    }
}

bool Dictionary::contains(const string& word) const {
	return dict.find(word) != dict.end();
}

vector<string> Dictionary::get_suggestions(const string& word) const {
	vector<string> suggestions;
	return suggestions;
}

int main(int argc, char* argv[]) {
     Dictionary* dic = new Dictionary();
     cout << "does the dictionary contain pool, 1 for true, 0 for false: "<<dic -> contains("pool")<<endl;
     cout << "does the dictionary contain abasdasfsa?: "<<dic -> contains("abasdasfsa")<<endl;
     delete dic;
     return 0;
} 

