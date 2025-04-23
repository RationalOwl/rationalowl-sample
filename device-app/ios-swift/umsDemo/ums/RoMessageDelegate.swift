import Foundation

import RationalOwl

class RoMessageDelegate: NSObject, MessageDelegate {
    // while app running this callback called when push arried,
    // If there's some un-delivered push message exist, this callback delivered un-delivered push list.
    func onPushMsgRecieved(_ msgSize: Int32, msgList: [Any]!, alarmIdx: Int32) {
        NSLog("[\(type(of: self))] onDownstreamMsgRecieved(msgSize: \(msgSize), alarmIdx: \(alarmIdx))")

        var latestToNofity: DataDef.Message?

        for userInfo in msgList {
            let userInfo = userInfo as! [String: Any]

            let data = userInfo["data"]
            var map: [String: Any]?

            if data is [String: Any] {
                map = data as? [String: Any]
            } else if data is String {
                if let jsonData = (data as! String).data(using: .utf8) {
                    do {
                        map = try JSONSerialization.jsonObject(with: jsonData, options: []) as? [String: Any]
                    } catch {
                        NSLog(error.localizedDescription)
                    }
                }
            }

            if map != nil {
                let message = DataDef.Message(map!)

                if MessageLocalDataSource.shared.containsMessage(message.id) { continue }
                NSLog("[onDownstreamMsgRecieved] new message (id: \(message.id), title: \(message.title))")

                MessageLocalDataSource.shared.addMessage(message)

                if latestToNofity == nil || message.sentAt > latestToNofity!.sentAt {
                    latestToNofity = message
                }
            }
        }

        if latestToNofity != nil {
            MessageLocalDataSource.shared.save()
            MessageSyncService.shared.showNotification(latestToNofity!)
        }
    }

    func onDownstreamMsgRecieved(_: [Any]!) {
        // hello world app don't tream realtime message
        NSLog("[\(type(of: self))] onDownstreamMsgRecieved()")
    }

    // realtime p2p received
    func onP2PMsgRecieved(_: [Any]!) {
        // hello world app don't tream realtime message
        NSLog("[\(type(of: self))] onP2PMsgRecieved()")
    }

    func onUpstreamMsgResult(_ resultCode: Int32, resultMsg: String!, msgId: String!) {
        NSLog("[\(type(of: self))] onUpstreamMsgResult(resultCode: \(resultCode), resultMsg: \(resultMsg!), msgId: \(msgId!))")
    }

    func onP2PMsgResult(_ resultCode: Int32, resultMsg: String!, msgId: String!) {
        NSLog("[\(type(of: self))] onP2PMsgResult(resultCode: \(resultCode), resultMsg: \(resultMsg!), msgId: \(msgId!))")
    }
}
