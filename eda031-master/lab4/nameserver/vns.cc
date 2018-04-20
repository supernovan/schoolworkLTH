#include <vector>
#include <algorithm>
#include "vns.h"

using namespace std;

void VNS::insert(const HostName& h, const IPAddress& ip) {
    vec.push_back(make_pair(h, ip));
}

bool VNS::remove(const HostName& h) {
    auto ban = [h](pair<HostName, IPAddress> p) { return h == p.first; };
    auto it = find_if(vec.begin(), vec.end(), ban);

    if (it != vec.end()) {
	vec.erase(it);
	return true;
    } else {
	return false;
    }
}

IPAddress VNS::lookup(const HostName& h) const {
    auto ban = [h](pair<HostName, IPAddress> p) { return h == p.first; };
    auto it = find_if(vec.begin(), vec.end(), ban);

    if (it != vec.end()) {
        return it->second;
    } else {
        return NON_EXISTING_ADDRESS;
    }
}
