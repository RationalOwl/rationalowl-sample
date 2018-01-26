#ifndef DEVICEGROUPLISTENER_H
#define DEVICEGROUPLISTENER_H

#include <vector>
#include <string>

using namespace std;

class DeviceGroupListener {
 public:
  virtual const void onDeviceGroupCreateResult(
      int resultCode, string resultMsg, string deviceGrpId,
      string deviceGrpName, int deviceSize, string desc,
      vector<string> failedDevices, string requestId) = 0;
  virtual const void onDeviceGroupAddResult(int resultCode, string resultMsg,
                                            string deviceGrpId,
                                            int totalDeviceSize,
                                            int addedDeviceSize,
                                            vector<string> failedDevices,
                                            string requestId) = 0;
  virtual const void onDeviceGroupSubtractResult(
      int resultCode, string resultMsg, string deviceGrpId, int totalDeviceSize,
      int subtractDeviceSize, vector<string> failedDevices,
      string requestId) = 0;
  virtual const void onDeviceGroupDeleteResult(int resultCode, string resultMsg,
                                               string deviceGrpId,
                                               string requestId) = 0;
};

#endif