#ifndef MESSAGELISTENER_H
#define MESSAGELISTENER_H

#include <list>
#include <string>

using namespace std;

class MessageListener {
 public:
  virtual const void onUpstreamMsgReceived(string sender, long sendTime, string msg) = 0;
  virtual const void onSendUnicastMsgResult(int resultCode, string resultMsg, string requestId) = 0;
  virtual const void onSendMulticastMsgResult(int resultCode, string resultMsg, string requestId) = 0;
  virtual const void onSendBroadcastMsgResult(int resultCode, string resultMsg, string requestId) = 0;
  virtual const void onSendGroupMsgResult(int resultCode, string resultMsg, string requestId) = 0;
};

#endif