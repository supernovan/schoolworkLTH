#ifndef MNS_H
#define MNS_H

#include <vector>
#include <map>

#include "nameserverinterface.h"

class MNS : public NameServerInterface {
public :
    void insert(const HostName& h, const IPAddress& ip);
    
    bool remove(const HostName& h);

    IPAddress lookup(const HostName& h) const;

private:
    std::map<HostName, IPAddress> serverMap;
};

#endif
