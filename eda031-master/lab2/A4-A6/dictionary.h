#ifndef DICTIONARY_H
#define DICTIONARY_H

#include <unordered_set>
#include <string>
#include <vector>
#include "word.h"
class Dictionary {
public:
	Dictionary();
	bool contains(const std::string& word) const;
	std::vector<std::string> get_suggestions(const std::string& word) const;
private:
    void add_trigram_suggestion(
            std::vector<std::string>& suggestions, 
            const std::string& word) const;
    
    void rank_suggestions(
            std::vector<std::string>& suggestions,
            const std::string& word) const;
    
    void trim_suggestions(
            std::vector<std::string>& suggestions) const;

    unsigned int distance(
            const std::string& s1,
            const std::string& s2) const;
    
    bool contains_half(
            const Word& word, const std::vector<std::string>& trigram) const;

    std::vector<std::string> get_trigrams( 
            const std::string& word) const;

    static const unsigned int MAX_L = 25;
    static const unsigned int TRIM_SIZE = 5;

    std::vector<Word> words[MAX_L + 1];
    std::unordered_set<std::string> dict;
};
#endif
