import 'dart:convert';
import 'dart:developer';
import 'dart:io';

import 'package:firebase_messaging/firebase_messaging.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:rationalowl_flutter/rationalowl_flutter.dart';

import 'text_field_dialog.dart';


// dev
const _gateHost = 'dev.rationalowl.com';
const _serviceId = 'SVCd6321331-289e-4337-aae6-333f3cc39d47';
const _deviceRegName = 'RationalOwl Flutter Example';
const _serverId = 'SVR74083a2a-48c6-46ea-b558-cedef12d10fa';
// cloud
/*
const _gateHost = 'gate.rationalowl.com';
const _serviceId = 'SVCf5db348a-069b-4a70-b5b0-05468a732241';
const _deviceRegName = 'RationalOwl Flutter Example';
const _serverId = 'SVR74083a2a-48c6-46ea-b558-cedef12d10fa';
 */

const _durationShort = Duration(seconds: 2);
const _durationLong = Duration(seconds: 4);

const _resultOk = 1;
const _resultDeviceAlreadyRegistered = -122;

class MainPage extends StatefulWidget {
  const MainPage({super.key});

  @override
  State<MainPage> createState() => _MainPageState();
}

class _MainPageState extends State<MainPage>
    implements DeviceRegisterResultListener {
  String? _deviceRegId, _lastRecipientDeviceRegId;

  @override
  Widget build(BuildContext context) {
    return Column(
      mainAxisAlignment: MainAxisAlignment.spaceBetween,
      children: <Widget>[
        Row(
          mainAxisAlignment: MainAxisAlignment.spaceEvenly,
          children: <Widget>[
            Tooltip(
              message: 'MinervaManager.registerDevice()',
              child: ElevatedButton(
                onPressed: _registerDevice,
                child: const Text('단말앱 등록'),
              ),
            ),
            Tooltip(
              message: 'MinervaManager.unregisterDevice()',
              child: ElevatedButton(
                onPressed: _deviceRegId != null ? _unregisterDevice : null,
                child: const Text('단말앱 등록 해제'),
              ),
            ),
          ],
        ),
        GestureDetector(
          onTap: () {
            if (_deviceRegId == null) return;

            Clipboard.setData(ClipboardData(text: _deviceRegId!));
            _showSnackBar(
              '단말앱 등록 아이디가 클립보드에 복사되었습니다.',
              duration: _durationShort,
            );
          },
          child: Text(
            '단말앱 등록 아이디: ${_deviceRegId ?? '미등록'}',
            textAlign: TextAlign.center,
          ),
        ),
        Row(
          mainAxisAlignment: MainAxisAlignment.spaceEvenly,
          children: <Widget>[
            Tooltip(
              message: 'MinervaManager.sendP2PMsg()',
              child: ElevatedButton(
                onPressed: _deviceRegId != null ? _sendP2PMessage : null,
                child: const Text('P2P 전송'),
              ),
            ),
            Tooltip(
              message: 'MinervaManager.sendUpstreamMsg()',
              child: ElevatedButton(
                onPressed: _deviceRegId != null ? _sendUpstreamMessage : null,
                child: const Text('Upstream 전송'),
              ),
            ),
          ],
        ),
      ],
    );
  }

  Future<void> _registerDevice() async {
    final MinervaManager minMgr = MinervaManager.getInstance();

    if (Platform.isAndroid) {
      final String? fcmToken = await FirebaseMessaging.instance.getToken();

      if (fcmToken == null) {
        _showSnackBar('FCM Token 조회 실패', duration: _durationShort);
        return;
      }

      log('fcmToken: $fcmToken', name: (MainPage).toString());
      await minMgr.setDeviceToken(fcmToken);
    }

    await minMgr.setRegisterResultListener(this);
    minMgr.registerDevice(
      gateHost: _gateHost,
      serviceId: _serviceId,
      deviceRegName: _deviceRegName,
    );
  }

  Future<void> _unregisterDevice() async {
    final MinervaManager minMgr = MinervaManager.getInstance();
    await minMgr.setRegisterResultListener(this);
    minMgr.unregisterDevice(serviceId: _serviceId);
  }

  String _buildMessage({required String title, required String body}) {
    return jsonEncode({
      'mId': DateTime.now().millisecondsSinceEpoch.toString(),
      'title': title,
      'body': body,
    });
  }

  Future<void> _sendP2PMessage() async {
    final String? recipientDeviceRegId = await showDialog(
      context: context,
      builder:
          (_) => TextFieldDialog(
            title: const Text('P2P 전송'),
            labelText: '상대 단말앱 등록 아이디',
            initialValue: _lastRecipientDeviceRegId,
          ),
    );

    if (recipientDeviceRegId == null) return;
    _lastRecipientDeviceRegId = recipientDeviceRegId;

    const String title = 'P2P Message';
    final String body = 'P2P Message from $_deviceRegId';

    final MinervaManager minMgr = MinervaManager.getInstance();
    minMgr.sendP2PMsg(
      data: _buildMessage(title: title, body: body),
      destDevices: [recipientDeviceRegId],
      notiTitle: title,
      notiBody: body,
    );
  }

  Future<void> _sendUpstreamMessage() async {
    final MinervaManager minMgr = MinervaManager.getInstance();
    minMgr.sendUpstreamMsg(
      data: 'Upstream Message from $_deviceRegId',
      serverRegId: _serverId,
    );
  }

  void _showSnackBar(String message, {Duration duration = _durationLong}) {
    final messenger = ScaffoldMessenger.of(context);

    messenger.clearSnackBars();
    messenger.showSnackBar(
      SnackBar(content: Text(message), duration: duration),
    );
  }

  @override
  void onRegisterResult(
    int resultCode,
    String? resultMsg,
    String? deviceRegId,
  ) {
    log(
      'onRegisterResult(resultCode: $resultCode, resultMsg: $resultMsg, deviceRegId: $deviceRegId)',
      name: (MainPage).toString(),
    );

    switch (resultCode) {
      case _resultOk:
        setState(() {
          _deviceRegId = deviceRegId;
        });

        _showSnackBar('단말앱 등록 성공');
        break;
      case _resultDeviceAlreadyRegistered:
        setState(() {
          _deviceRegId = deviceRegId;
        });

        _showSnackBar('이미 등록된 단말앱');
        break;
      default:
        _showSnackBar('단말앱 등록 오류: $resultMsg ($resultCode)');
        break;
    }
  }

  @override
  void onUnregisterResult(int resultCode, String? resultMsg) {
    log(
      'onUnregisterResult(resultCode: $resultCode, resultMsg: $resultMsg)',
      name: (MainPage).toString(),
    );

    if (resultCode == _resultOk) {
      setState(() {
        _deviceRegId = null;
      });

      _showSnackBar('단말앱 등록 해제 성공');
    } else {
      _showSnackBar('단말앱 등록 해제 오류: $resultMsg ($resultCode)');
    }
  }
}
