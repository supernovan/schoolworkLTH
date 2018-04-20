#include "tagremover.h"
#include <iostream>
#include <regex>
#include <string>

using namespace std;
TagRemover::TagRemover(istream& stream) {
    string line;
    while (getline(stream, line)) {
       html += line + "\n";
    }
    int s1,  s2;

    while (html.find("<") != string::npos) {
        s1 = html.find("<");
        s2 = html.find(">");
        html.erase(s1, s2-s1+2);
    }
    while (html.find("&lt;") != string::npos) {
        html.replace(html.find("&lt;"), 4, "<");
    }
    
    while (html.find("&gt;") != string::npos) {
        html.replace(html.find("&gt;"), 4, ">");
    }
    
    while (html.find("&nbsp;") != string::npos) {
        html.replace(html.find("&nbsp;"), 6, " ");
    }

    while (html.find("&amp;") != string::npos) {
        html.replace(html.find("&amp;"), 5, "&");
    
    }
}
void TagRemover::print(ostream& out) {
    out << html << endl;
}

 
