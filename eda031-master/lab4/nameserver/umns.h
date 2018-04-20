#ifndef UMNS_H
#define UMNS_H

#include <vector>
#include <unordered_map>

#include "nameserverinterface.h"

class UMNS : public NameServerInterface {
public :
    void insert(const HostName& h, const IPAddress& ip);
    
    bool remove(const HostName& h);

    IPAddress lookup(const HostName& h) const;

private:
    std::unordered_map<HostName, IPAddress> serverMap;
};

#endif
