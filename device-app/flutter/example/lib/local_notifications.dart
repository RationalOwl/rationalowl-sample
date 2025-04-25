import 'dart:io';

import 'package:flutter_local_notifications/flutter_local_notifications.dart';

const channel = AndroidNotificationChannel(
  'rationalowl_flutter_example', // ID
  'RationalOwl Flutter Example', // Name
  importance: Importance.max,
);

Future<void> initializeNotification() async {
  final plugin = FlutterLocalNotificationsPlugin();

  await plugin.initialize(
    const InitializationSettings(
      android: AndroidInitializationSettings('@mipmap/ic_launcher'),
      iOS: DarwinInitializationSettings(
        requestAlertPermission: false,
        requestBadgePermission: false,
        requestSoundPermission: false,
      ),
    ),
  );

  if (Platform.isAndroid) {
    final androidImplementation =
        plugin
            .resolvePlatformSpecificImplementation<
              AndroidFlutterLocalNotificationsPlugin
            >();
    final bool? granted =
        await androidImplementation?.requestNotificationsPermission();

    if (granted == true) {
      androidImplementation?.createNotificationChannel(channel);
    }
  } else if (Platform.isIOS) {
    final iosImplementation =
        plugin
            .resolvePlatformSpecificImplementation<
              IOSFlutterLocalNotificationsPlugin
            >();

    await iosImplementation?.requestPermissions(
      alert: true,
      badge: true,
      sound: true,
    );
  }
}

void showNotification(Map<String, dynamic> data) {
  final String id = data['mId'];
  final String body = data['body'];
  final String? title = data['title'];

  final plugin = FlutterLocalNotificationsPlugin();

  plugin.show(
    id.hashCode,
    title,
    body,
    NotificationDetails(
      android: AndroidNotificationDetails(
        channel.id,
        channel.name,
        importance: Importance.max,
        priority: Priority.max,
      ),
    ),
  );
}
