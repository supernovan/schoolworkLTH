#include <string>
#include <vector>
#include <algorithm>
#include "word.h"
#include "iostream"

using namespace std;

Word::Word(const string& w, const vector<string>& t) : 
    word{w}, trigrams{t}  {}

string Word::get_word() const {
	return word;
}

unsigned int Word::get_matches(const vector<string>& t) const {
	unsigned int matches = 0;
    for (string tri : trigrams) {
        if (find(t.begin(), t.end(), tri) != t.end()) {
            matches++;
        }
    }
    return matches;
}
