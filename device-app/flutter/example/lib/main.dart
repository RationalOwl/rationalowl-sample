import 'dart:io';

import 'package:firebase_core/firebase_core.dart';
import 'package:firebase_messaging/firebase_messaging.dart';
import 'package:flutter/material.dart';
import 'package:rationalowl_flutter/rationalowl_flutter.dart';

import 'firebase_messaging_handlers.dart';
import 'firebase_options.dart';
import 'local_notifications.dart';
import 'main_page.dart';
import 'ro_message_listener.dart';

const iOSAppGroup = 'group.com.rationalowl.flutterexample';

Future<void> _initialize() async {
  if (Platform.isAndroid) {
    await Firebase.initializeApp(
      options: DefaultFirebaseOptions.currentPlatform,
    );
    FirebaseMessaging.onBackgroundMessage(handleMessage);
  }

  await initializeNotification();

  final MinervaManager minMgr = MinervaManager.getInstance();

  if (Platform.isIOS) {
    await minMgr.setAppGroup(iOSAppGroup);
  }
}

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  await _initialize();

  runApp(const Application());
}

class Application extends StatefulWidget {
  const Application({super.key});

  @override
  State<StatefulWidget> createState() => _ApplicationState();
}

class _ApplicationState extends State<Application> with WidgetsBindingObserver {
  late final MinervaAppLifecycleObserver _observer;

  @override
  void initState() {
    super.initState();

    if (Platform.isIOS) {
      _observer = MinervaAppLifecycleObserver();
      WidgetsBinding.instance.addObserver(_observer);
    } else {
      FirebaseMessaging.instance.onTokenRefresh.listen(handleTokenRefresh);
    }

    final MinervaManager minMgr = MinervaManager.getInstance();
    minMgr.setMsgListener(RoMessageListener());
  }

  @override
  void dispose() {
    if (Platform.isIOS) {
      WidgetsBinding.instance.removeObserver(_observer);
    }

    final MinervaManager minMgr = MinervaManager.getInstance();
    minMgr.clearMsgListener();

    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return const MaterialApp(home: Scaffold(body: SafeArea(child: MainPage())));
  }
}
