#include <boost/locale.hpp>
#include <boost/thread/thread.hpp>
#include <iostream>
#include <locale>
#include <vector>
#include "AppServerManager.h"
#include "AppServerRegisterResultListener.h"
#include "DeviceGroupListener.h"
#include "MessageListener.h"
using namespace std;

string GroupId;

string serviceId("b4a2166853ad4f19acc77b193d89c497");

vector<string> deviceIdList = {
    "11459415de7e48d2a21c94773a8dfb4d", "1531f97adde6490fa63586dcdb3f6a42",
    "19dae261a57745fbb258fd343c4ef155", "20630a1d13f54c4db84bde2b9f0e4e51",
    "29f78e16a7284b5197b3a290c73a5186", "42ff2a6e51554d669e378454ef29fcd9",
    "4d5a55166e7543519fe6dc2f2c02966b", "597c2c77081544bb823a7a5e93f98932",
    "5d0d2849ceb1445fa1101530576b7967", "618850cf110244bc86fc9d4bbcc259f0",
    "667d1ded315344ccbc456fb689780fe9", "7db08edcb89d41bcbac6651e5d174679",
    "84ef65df012945e0ade2c7cfde3e498d", "b935b61284ac494cb1eb07b484edc0b4",
    "c14fba91eb49423c8a6101be45a8ac4d", "d1e54b7f56814be79617f83a4bc27891",
    "f0d04ffe80334ac0b50953d24c677532"

};

vector<string> subDeviceList1(&deviceIdList[0], &deviceIdList[5]);
vector<string> subDeviceList2(&deviceIdList[6], &deviceIdList[8]);

class AppServerRegisterResultImpl : public AppServerRegisterResultListener {
 public:
  const void onRegisterResult(int resultCode, string resultMsg,
                              string appServerRegId) {
    cout << "++onRegisterResult" << endl;
    string resultMessage =
        boost::locale::conv::from_utf<char>(resultMsg, "EUC-KR");
    cout << resultCode << " " << resultMessage << " " << appServerRegId << endl;
  }
  const void onUnregisterResult(int resultCode, string resultMsg) {
    cout << "++onUnregisterResult" << endl;
    string resultMessage =
        boost::locale::conv::from_utf<char>(resultMsg, "EUC-KR");
    cout << resultCode << " " << resultMessage << endl;
  }
};
class DeviceGroupImpl : public DeviceGroupListener {
 public:
  const void onDeviceGroupCreateResult(int resultCode, string resultMsg,
                                       string deviceGrpId, string deviceGrpName,
                                       int deviceSize, string desc,
                                       vector<string> failedDevices,
                                       string requestId) {
    GroupId = deviceGrpId;
    cout << "++onDeviceGroupCreateResult" << endl;
    cout << resultCode << " " << deviceGrpId << " " << deviceGrpName << " "
         << deviceSize << " " << desc << " " << requestId << endl;
  }
  const void onDeviceGroupAddResult(int resultCode, string resultMsg,
                                    string deviceGrpId, int totalDeviceSize,
                                    int addedDeviceSize,
                                    vector<string> failedDevices,
                                    string requestId) {
    cout << "++onDeviceGroupAddResult" << endl;
    cout << resultCode << " " << deviceGrpId << " " << totalDeviceSize << " "
         << addedDeviceSize << " " << requestId << endl;
  }
  const void onDeviceGroupSubtractResult(
      int resultCode, string resultMsg, string deviceGrpId, int totalDeviceSize,
      int subtractDeviceSize, vector<string> failedDevices, string requestId) {
    cout << "++onDeviceGroupSubtractResult" << endl;
    cout << resultCode << " " << deviceGrpId << " " << totalDeviceSize << " "
         << subtractDeviceSize << " " << requestId << endl;
  }
  const void onDeviceGroupDeleteResult(int resultCode, string resultMsg,
                                       string deviceGrpId, string requestId) {
    cout << "++onDeviceGroupDeleteResult" << endl;
    cout << resultCode << " " << deviceGrpId << " " << requestId << endl;
  }
};
class MessageImpl : public MessageListener {
 public:
  const void onUpstreamMsgReceived(string sender, long sendTime, string msg) {
    cout << "++onUpstreamMsgReceived" << endl;
    cout << sender << " " << sendTime << " " << msg << endl;
  }
  const void onSendUnicastMsgResult(int resultCode, string resultMsg,
                                    string requestId) {
    cout << "++onSendUnicastMsgResult" << endl;
    cout << resultCode << " " << requestId << endl;
  }
  const void onSendMulticastMsgResult(int resultCode, string resultMsg,
                                      string requestId) {
    cout << "++onSendMulticastMsgResult" << endl;
    cout << resultCode << " " << requestId << endl;
  }
  const void onSendBroadcastMsgResult(int resultCode, string resultMsg,
                                      string requestId) {
    cout << "++onSendBroadcastMsgResult" << endl;
    cout << resultCode << " " << requestId << endl;
  }
  const void onSendGroupMsgResult(int resultCode, string resultMsg,
                                  string requestId) {
    cout << "++onSendGroupMsgResult" << endl;
    cout << resultCode << " " << requestId << endl;
  }
};

int main() {
  AppServerManager::GetInstance()->setRegisterResultListener(
      new AppServerRegisterResultImpl());
  AppServerManager::GetInstance()->setDeviceGroupListener(
      new DeviceGroupImpl());
  AppServerManager::GetInstance()->setMsgListener(new MessageImpl());

  AppServerManager::GetInstance()->registerAppServer(
      serviceId, u8"Cpp Test Server", "gate.rationalowl.com", 9081);

  boost::this_thread::sleep(boost::posix_time::milliseconds(1000));

  AppServerManager::GetInstance()->createDeviceGroup("groupName", "groupDesc",
                                                     subDeviceList1);

  boost::this_thread::sleep(boost::posix_time::milliseconds(1000));

  AppServerManager::GetInstance()->addDeviceGroup(GroupId, subDeviceList2);

  boost::this_thread::sleep(boost::posix_time::milliseconds(1000));

  AppServerManager::GetInstance()->subtractDeviceGroup(GroupId, subDeviceList2);

  boost::this_thread::sleep(boost::posix_time::milliseconds(1000));

  AppServerManager::GetInstance()->deleteDeviceGroup(GroupId);

  boost::this_thread::sleep(boost::posix_time::milliseconds(1000));

  AppServerManager::GetInstance()->createDeviceGroup("groupName", "groupDesc",
                                                     subDeviceList1);

  boost::this_thread::sleep(boost::posix_time::milliseconds(1000));

  AppServerManager::GetInstance()->sendMulticastMsg(u8"멀티캐스트메시지",
                                                    subDeviceList1);
  boost::this_thread::sleep(boost::posix_time::milliseconds(1000));

  AppServerManager::GetInstance()->sendGroupMsg(u8"그룹메시지", GroupId);
  boost::this_thread::sleep(boost::posix_time::milliseconds(1000));

  AppServerManager::GetInstance()->sendBroadcastMsg(u8"브로드캐스트메시지");

  while (true) {
    boost::this_thread::sleep(boost::posix_time::milliseconds(500));
  }
  return 0;
}