#ifndef DICTIONARY_H
#define DICTIONARY_H

#include <unordered_set>
#include <string>
#include <vector>

class Dictionary {
public:
	Dictionary();
	bool contains(const std::string& word) const;
	std::vector<std::string> get_suggestions(const std::string& word) const;
private:
    static const unsigned word_size = 25;
    std::unordered_set<std::string> dict;
    std::vector<Word> words[words_size];
};
#endif
