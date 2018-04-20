#include <string>
#include <vector>
#include <fstream>
#include <iostream>
#include <algorithm>
#include <utility>
#include "word.h"
#include "dictionary.h"
#include <sstream>

//#define DEBUG(X)
#define DEBUG(X) do { X; } while (0);
using namespace std;

Dictionary::Dictionary() {
	ifstream infile ("web2.txt");
    string word;
    if (infile.is_open()) {
        while (getline(infile, word)) {
            transform(word.begin(), word.end(),
                    word.begin(), ::tolower);
            
            vector<string> trigrams;
            unsigned int nbr_t = word.length()-2;
                if (word.length() >= 3) {
                for (unsigned int i = 0; i < nbr_t; i++) {
                    string sub_string = word.substr(i, 3);
                    trigrams.push_back(sub_string);
                }
            }
            if (word.length() < MAX_L) {
                dict.insert(word);
                words[word.length()].push_back(Word(word, trigrams));
            }
        }
    }
    else {
        cerr << "Could not open the file\n";
        exit(1);
    }
}
void Dictionary::add_trigram_suggestion(vector<string>& suggestions, const string& word) const {
     if (word.length() < 1 || word.length() > 25) {
         return;
     }
     
     unsigned int start = word.length() == 1 ? word.length() : word.length() - 1;
     unsigned int end = word.length() == MAX_L ? word.length() : word.length() + 1;
     for (unsigned int i = start; i<=end; i++) {
         for (Word w : words[i]) {
             if (contains_half(w, get_trigrams(word))) {
                suggestions.push_back(w.get_word());
             }
         }
     }
 }

unsigned int Dictionary::distance(const string& s1, const string& s2) const {
    const size_t len1 = s1.length();
    const size_t len2 = s2.length();
    
    int d[MAX_L+1][MAX_L+1];
    for (unsigned int i = 0; i < MAX_L + 1; i++) {
        d[i][0] = i;
        d[0][i] = i;
    }

    for (unsigned int i = 1; i<len1; i++) {
        for (unsigned int j = 1; j<len2; j++) {
            d[i][j] = min({
                    d[i-1][j] + 1,
                    d[i][j-1] + 1,
                    d[i-1][j-1] + (s1[i-1] == s2[j-1] ? 0 : 1)});
        }
    }
    return d[len1][len2];
}
void Dictionary::rank_suggestions(vector<string>& suggestions, const string& word) const {
    vector<pair<int,string>> pairs;
    for (const string& w : suggestions) {
        unsigned int dist = distance(w, word);
        pairs.push_back(make_pair(dist, w));
    }
    
    using P = pair<int,string>;
    auto compare = [](P p1, P p2) { return p1.first < p2.first; };
    sort(pairs.begin(), pairs.end(), compare);
    suggestions.clear();
    for (P p : pairs) {
        suggestions.push_back(p.second);
    }
   
}
void Dictionary::trim_suggestions(vector<string>& suggestions) const {
    if (suggestions.size() > TRIM_SIZE) {
        suggestions.resize(TRIM_SIZE);
    }
}

bool Dictionary::contains_half(const Word& word, const vector<string>& trigram) const  {
    return word.get_matches(trigram) >= (word.get_word().length()-2)/2;
}
bool Dictionary::contains(const string& word) const {
	return dict.find(word) != dict.end();
}

vector<string> Dictionary::get_trigrams(const string& word) const {
    unsigned int len = word.length();
    vector<string> tri;
    if ( len >= 3) {
        for (unsigned int i = 0; i<len-2; i++) {
            tri.push_back(word.substr(i, 3));
        }
    }
    sort(tri.begin(), tri.end());
    return tri;
}

vector<string> Dictionary::get_suggestions(const string& word) const {
	vector<string> suggestions;
    add_trigram_suggestion(suggestions, word);
    DEBUG(
                    cout << "Suggestions after add:" << endl;
                            for (const string& s : suggestions)
                                        cout << "  " << s << endl;
                                            );
    rank_suggestions(suggestions, word);
    trim_suggestions(suggestions);
	return suggestions;
}

