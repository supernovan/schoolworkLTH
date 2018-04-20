#include "hns.h"
#include <algorithm>
#include <functional>
#include <iostream>

using namespace std;

HNS::HNS(const unsigned int size) : size_{size} {
    vec = vector<vector<pair<HostName, IPAddress>>> (size);
}

void HNS::insert(const HostName& h, const IPAddress& ip) {
    string::size_type temp = hash<string>{}(h);
    temp = temp%size_;
    vec[temp].push_back(make_pair(h, ip));
}

bool HNS::remove(const HostName& h) {
    string::size_type temp = hash<string>{}(h);
    temp=temp%size_;

    auto& l = vec[temp];
    auto ban = [h](pair<HostName, IPAddress> pa) {
        return h == pa.first; };
    const auto& car = find_if(l.begin(), l.end(), ban);

    if (car != l.end()) {
        l.erase(car);
        return true;
    }
    
    return false;
}

IPAddress HNS::lookup(const HostName& h) const {
    string::size_type temp = hash<string>{}(h);
    temp=temp%size_;

    auto& l = vec[temp];
    auto ban = [h](pair<HostName, IPAddress> pa) {
        return h == pa.first; };
    const auto& car = find_if(l.begin(), l.end(), ban);

    if (car != l.end()) {
        return car -> second;
    } else {
        return NON_EXISTING_ADDRESS;
    }
}
