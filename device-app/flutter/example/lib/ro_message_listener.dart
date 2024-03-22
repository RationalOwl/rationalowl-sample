import 'dart:developer';

import 'package:rationalowl_flutter/rationalowl_flutter.dart';

import 'local_notifications.dart';

class RoMessageListener implements MessageListener {
  @override
  void onDownstreamMsgReceived(List<Map<String, dynamic>> msgList) {
    log('onDownstreamMsgReceived(msgList: $msgList)', name: (RoMessageListener).toString());
  }

  @override
  void onP2PMsgReceived(List<Map<String, dynamic>> msgList) {
    log('onP2PMsgReceived(msgList: $msgList)', name: (RoMessageListener).toString());

    if (msgList.isNotEmpty) {
      final latestMessage = Map<String, dynamic>.from(msgList[0]['data']);
      showNotification(latestMessage);
    }
  }

  @override
  void onPushMsgReceived(List<Map<String, dynamic>> msgList) {
    log('onPushMsgReceived(msgList: $msgList)', name: (RoMessageListener).toString());

    if (msgList.isNotEmpty) {
      final latestMessage = Map<String, dynamic>.from(msgList[0]['data']);
      showNotification(latestMessage);
    }
  }

  @override
  void onSendUpstreamMsgResult(int resultCode, String? resultMsg, String? msgId) {
    log('onSendUpstreamMsgResult(resultCode: $resultCode, resultMsg: $resultMsg, msgId: $msgId)', name: (RoMessageListener).toString());
  }

  @override
  void onSendP2PMsgResult(int resultCode, String? resultMsg, String? msgId) {
    log('onSendP2PMsgResult(resultCode: $resultCode, resultMsg: $resultMsg, msgId: $msgId)', name: (RoMessageListener).toString());
  }
}
