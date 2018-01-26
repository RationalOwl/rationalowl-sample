#ifndef APPSERVERREGISTERRESULTLISTENER_H
#define APPSERVERREGISTERRESULTLISTENER_H

#include <string>

using namespace std;

class AppServerRegisterResultListener {
 public:
    virtual const void onRegisterResult(int resultCode, string resultMsg, string appServerRegId) = 0;
    virtual const void onUnregisterResult(int resultCode, string resultMsg) = 0;
};

#endif