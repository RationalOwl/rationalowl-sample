import Foundation
import RationalOwl

class RoMessageDelegate: NSObject, MessageDelegate
{
    
    private let repository = MessagesRepository.shared
    
    //realtime downstream received.
    func onDownstreamMsgRecieved(_ msgList: [Any]!) {
        // ums demo app don't treat realtime message
        print("onDownstreamMsgRecieved");
    }
    
    // realtime p2p received
    func onP2PMsgRecieved(_ msgList: [Any]!) {
        // ums demo app don't treat realtime message
        print("onP2PMsgRecieved");
    }
    
    // while app running this callback called when push arried,
    // If there's some un-delivered push message exist, this callback delivered un-delivered push list.
    func onPushMsgRecieved(_ msgSize: Int32, msgList: [Any]!, alarmIdx: Int32) {
        print("onPushMsgRecieved msg size = \(msgSize)")
        var latestToNofity: Message?
        
        for userInfo in msgList
        {
            let userInfo = userInfo as! [String: Any]
            let data = userInfo["data"]
            var map: [String: Any]?
            
            if data is [String: Any]
            {
                map = data as? [String: Any]
            }
            else if data is String
            {
                if let jsonData = (data as! String).data(using: .utf8)
                {
                    do
                    {
                        map = try JSONSerialization.jsonObject(with: jsonData, options: []) as? [String: Any]
                    }
                    catch
                    {
                        NSLog(error.localizedDescription)
                    }
                }
            }
            
            if map != nil
            {
                let message = Message(map!)
                
                if repository.hasMessage(message.id) { continue }
                NSLog("[onDownstreamMsgRecieved] new message (id: \(message.id), title: \(message.title))")
                
                repository.addMessage(message)
                
                if latestToNofity == nil || message.sentAt > latestToNofity!.sentAt
                {
                    latestToNofity = message
                }
            }
        }
        
        if latestToNofity != nil
        {
            repository.save()
            MessageSyncService.shared.showNotification(latestToNofity!)
        }
    }
    
    
    func onUpstreamMsgResult(_ resultCode: Int32, resultMsg: String!, msgId: String!) {
        print("onMsgRecieved msg upstream message id = " + msgId);
    }
    
    
    func onP2PMsgResult(_ resultCode: Int32, resultMsg: String!, msgId: String!) {
        print("onMsgRecieved msg p2p message id = " + msgId);
    }
}
