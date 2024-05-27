import 'dart:developer';

import 'package:firebase_messaging/firebase_messaging.dart';
import 'package:rationalowl_flutter/rationalowl_flutter.dart';

import 'local_notifications.dart';

Future<void> handleTokenRefresh(String token) async {
  log('handleTokenRefresh(token: $token)');

  final MinervaManager minMgr = MinervaManager.getInstance();
  minMgr.setDeviceToken(token);
}

@pragma('vm:entry-point')
Future<void> handleMessage(RemoteMessage message) async {
  log('handleMessage(message.data: ${message.data})');

  final Map<String, dynamic> data = message.data;

  final MinervaManager minMgr = MinervaManager.getInstance();
  minMgr.enableNotificationTracking(data: data);

  if (!data.containsKey('silent')) {
    showNotification(data);
  }
}
