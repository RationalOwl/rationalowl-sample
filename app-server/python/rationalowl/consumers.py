import json
import logging
import re

import channels
from channels import Group, Channel
from channels.generic.websockets import WebsocketConsumer
from channels.sessions import channel_session
from minerva.AppServerManager import AppServerManager
from minerva.AppServerRegisterResultListener import \
    AppServerRegisterResultListener
from minerva.DeviceGroupListener import DeviceGroupListener
from minerva.MessageListener import MessageListener

log = logging.getLogger(__name__)

class AppServerRegisterResultListenerImpl(AppServerRegisterResultListener):
    def onRegisterResult(self, resultCode, resultMsg, appServerRegId):
        msg = 'onRegisterResult {} {} {}'.format(resultCode, resultMsg, appServerRegId)
        Group('rational').send({'text': msg})

    def onUnregisterResult(self, resultCode, resultMsg):
        msg = 'onUnregisterResult {} {}'.format(
            resultCode, resultMsg)
        Group('rational').send({'text': msg})

class DeviceGroupListenerImpl(DeviceGroupListener):
    def onDeviceGroupCreateResult(self, resultCode,  resultMsg,  deviceGrpId,
                                    deviceGrpName,  deviceSize,  desc, failedDevices,  requestId):
        msg = 'onDeviceGroupCreateResult {} {} {} {} {} {} {} {}'.format(
            resultCode, resultMsg, deviceGrpId, deviceGrpName,  deviceSize,  desc, failedDevices,  requestId)
        Group('rational').send({'text': msg})

    def onDeviceGroupAddResult(self, resultCode,  resultMsg,  deviceGrpId,
                                totalDeviceSize,  addedDeviceSize, failedDevices,  requestId):
        msg = 'onDeviceGroupAddResult {} {} {} {} {} {} {}'.format(
            resultCode, resultMsg, deviceGrpId, totalDeviceSize,  addedDeviceSize, failedDevices,  requestId)
        Group('rational').send({'text': msg})

    def onDeviceGroupSubtractResult(self, resultCode,  resultMsg,  deviceGrpId,
                                    totalDeviceSize,  subtractDeviceSize, failedDevices,  requestId):
        msg = 'onDeviceGroupSubtractResult {} {} {} {} {} {} {}'.format(
            resultCode, resultMsg, deviceGrpId, totalDeviceSize,  subtractDeviceSize, failedDevices,  requestId)
        Group('rational').send({'text': msg})

    def onDeviceGroupDeleteResult(self, resultCode,  resultMsg,  deviceGrpId, requestId):
        msg = 'onDeviceGroupDeleteResult {} {} {} {}'.format(
            resultCode, resultMsg, deviceGrpId,  requestId)
        Group('rational').send({'text': msg})


class MessageListenerImpl(MessageListener):
    def onUpstreamMsgReceived(self, sender, sendTime, msg):
        msg = 'onUpstreamMsgReceived {} {} {}'.format(
            sender, sendTime, msg)
        Group('rational').send({'text': msg})


    def onSendUnicastMsgResult(self, resultCode, resultMsg, requestId):
        msg = 'onSendUnicastMsgResult {} {} {}'.format(
            resultCode, resultMsg, requestId)
        Group('rational').send({'text': msg})

    def onSendMulticastMsgResult(self, resultCode, resultMsg, requestId):
        msg = 'onSendMulticastMsgResult {} {} {}'.format(
                    resultCode, resultMsg, requestId)
        Group('rational').send({'text': msg})

    def onSendBroadcastMsgResult(self, resultCode, resultMsg, requestId):
        msg = 'onSendBroadcastMsgResult {} {} {}'.format(
            resultCode, resultMsg, requestId)
        Group('rational').send({'text': msg})

    def onSendGroupMsgResult(self, resultCode, resultMsg, requestId):
        msg = 'onSendGroupMsgResult {} {} {}'.format(
            resultCode, resultMsg, requestId)
        Group('rational').send({'text': msg})

AppServerManager().setRegisterResultListener(AppServerRegisterResultListenerImpl())
AppServerManager().setMsgListener(MessageListenerImpl())
AppServerManager().setDeviceGroupListener(DeviceGroupListenerImpl())

req_id = AppServerManager().registerAppServer(
    'ab03d23035e74e2fbd868a6f243c2dd5', 'sample-app-server-python', 'gate.rationalowl.com', 9081)


def ws_connect(message: channels.message.Message):
    Group('rational').add(message.reply_channel)
    message.reply_channel.send({"accept": True})

def ws_receive(message: channels.message.Message):
    msg = json.loads(message.content['text'])
    print(msg)

    if msg['action'] == 'createDeviceGroup':
        AppServerManager().createDeviceGroup(
            msg['body']['groupName'], msg['body']['groupDesc'], msg['body']['deviceList'])
    elif msg['action'] == 'addDeviceGroup':
        AppServerManager().addDeviceGroup(
            msg['body']['groupId'], msg['body']['deviceList'])
    elif msg['action'] == 'subtractDeviceGroup':
        AppServerManager().subtractDeviceGroup(
            msg['body']['groupId'], msg['body']['deviceList'])
    elif msg['action'] == 'deleteDeviceGroup':
        AppServerManager().deleteDeviceGroup(
            msg['body']['groupId'])
    elif msg['action'] == 'sendMulticastMsg':
        AppServerManager().sendMulticastMsg(
            msg['body']['message'], msg['body']['deviceList'])
    elif msg['action'] == 'sendBroadcastMsg':
        AppServerManager().sendBroadcastMsg(
            msg['body']['message'])
    elif msg['action'] == 'sendGroupMsg':
        AppServerManager().sendGroupMsg(
            msg['body']['message'], msg['body']['groupId'])

def ws_disconnect(message):
    Group('rational').discard(message.reply_channel)


