#include <string>
#include <vector>
#include <algorithm>
#include "word.h"

using namespace std;

Word::Word(const string& w, const vector<string>& t) : 
    word{w}, trigrams{t}  {}

string Word::get_word() const {
	return word;
}

unsigned int Word::get_matches(const vector<string>& t) const {
	unsigned int matches = 0;
    for (unsigned int i = 0; i < t.size(); i++) {
        if (find(t.begin(), t.end(), trigrams.at(i)) != t.end()) {
            matches++;
        }
    }
    return matches;
}
