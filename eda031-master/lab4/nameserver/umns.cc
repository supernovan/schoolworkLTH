#include <vector>
#include <algorithm>
#include "umns.h"

using namespace std;

void UMNS::insert(const HostName& h, const IPAddress& ip) {
    serverMap.insert({h, ip});
}

bool UMNS::remove(const HostName& h) {
    return serverMap.erase(h);
}

IPAddress UMNS::lookup(const HostName& h) const {
    auto it = serverMap.find(h);
    if (it != serverMap.end()) {
        return it->second;
    } else {
        return NON_EXISTING_ADDRESS;
    }
}
