#include <vector>
#include <string>
#include <utility>
#include "nameserverinterface.h"

class HNS : public NameServerInterface {
public:
    HNS(const unsigned int size);
    void insert(const HostName& h, const IPAddress& ip);
    bool remove(const HostName& h);
    IPAddress lookup(const HostName& h) const;

private: 
    size_t size_;
    std::vector<std::vector<std::pair<HostName, IPAddress>>> vec;
};
