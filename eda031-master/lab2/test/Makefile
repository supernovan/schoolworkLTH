# Define the compiler and the linker. The linker must be defined since
# the implicit rule for linking uses CC as the linker. g++ can be
# changed to clang++.
CXX = g++
CC  = g++

# Define preprocessor, compiler, and linker flags. Uncomment the # lines
# if you use clang++ and wish to use libc++ instead of GNU's libstdc++.
# -g is for debugging.
CPPFLAGS =  -std=c++11
CXXFLAGS =  -O2  
# -Wall -Wextra -pedantic-errors -Wold-style-cast 
CXXFLAGS += -std=c++11 
CXXFLAGS += -g
#CPPFLAGS += -stdlib=libc++
#CXXFLAGS += -stdlib=libc++
#LDFLAGS +=  -stdlib=libc++

# Targets
PROGS = spell


all: $(PROGS)

# Targets rely on implicit rules for compiling and linking

spell: dictionary.o spell.o word.o

dictionary.o: dictionary.cc word.h dictionary.h 

spell.o: spell.cc dictionary.h 

word.o: word.cc word.h

# Phony targets
.PHONY: all clean

# Standard clean
clean:
	rm -f *.o


