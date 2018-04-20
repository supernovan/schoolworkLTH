#ifndef TAG_H
#define TAG_H
#include <iostream>
#include <regex>
#include <string>

class TagRemover {
    public:
        TagRemover(std::istream& stream);

        void print(std::ostream& out);
    private:
        std::string html;
};

#endif
