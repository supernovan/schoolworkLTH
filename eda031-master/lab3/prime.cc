#include <iostream>
#include <string>
using namespace std;

string eratos(const unsigned long nbr) {
    string prim(nbr, 'P');
    prim[0] = 'C';
    prim[1] = 'C';
    
    for (unsigned long i = 2; i < nbr; i++) {
        if (prim[i] == 'P') {
            for (unsigned long j = 2; j*i< nbr; j++) {
                prim[i*j] = 'C';
            }
        }
    }
}

int main() {
    cout <<"Skriv in den första gränsen för att ta ut största primtalet i"<<endl;
    unsigned long nbr;
    cin >> nbr;
    cout<<"Största primtalet är: "<< eratos(nbr).find_last_of("P");<<endl;
    cout<<"Skriv den andra gränsen"<<endl;
    cin >> nbr;
    cout<<"Största primtalet är: "<< eratos(nbr).find_last_of("P");<<endl;
}

