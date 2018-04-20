#include <vector>
#include <algorithm>
#include "mns.h"

using namespace std;

void MNS::insert(const HostName& h, const IPAddress& ip) {
    serverMap.insert({h, ip});
}

bool MNS::remove(const HostName& h) {
    return serverMap.erase(h);
}

IPAddress MNS::lookup(const HostName& h) const {
    auto it = serverMap.find(h);
    if (it != serverMap.end() ) {
        return it->second;
    } else {
        return NON_EXISTING_ADDRESS;
    }
}
