#include <string>
#include <vector>
#include <fstream>
#include <iostream>
#include <algorithm>
#include "word.h"
#include "dictionary.h"

using namespace std;

Dictionary::Dictionary() {
	ifstream infile ("web2.txt");
    string word;
    if (infile.is_open()) {
        while (getline(infile, word)) {
            transform(word.begin(), word.end(), word.begin(), ::tolower);
            vector<string> sub_strings;
            for (unsigned int i = 0; i < word.length()-2; i++) {
                string tri = word.substr(i, 3);
                sub_string.push_back(tri);
            }
            if (words.size() > words_size) {
                dict.insert(word);
                words.push_back(Word(word, sub_string));
            }
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
     cout << dic -> contains("pool");
     cout << dic -> contains("abasdasfsa");
     delete dic;
     return 0;
} 

