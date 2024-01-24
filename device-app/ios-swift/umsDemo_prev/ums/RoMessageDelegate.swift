import Foundation
import RationalOwl

class RoMessageDelegate: NSObject, MessageDelegate
{
    private let messageTypeCustom = 3
    private let repository = MessagesRepository.shared
    
    func onDownstreamMsgRecieved(_ msgSize: Int32, msgList: [Any]!, alarmIdx: Int32)
    {
        NSLog("[\(type(of: self))] onDownstreamMsgRecieved(msgSize: \(msgSize), alarmIdx: \(alarmIdx))")
        
        var latestToNofity: Message?
        
        for userInfo in msgList
        {
            let userInfo = userInfo as! [String: Any]
            
            if userInfo["SystemPush"] != nil
            {
                print("system push received!!")
                continue
            }
            
            let type = userInfo["type"] as? Int
            
            if type == messageTypeCustom
            {
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
        }
        
        if latestToNofity != nil
        {
            repository.save()
            MessageSyncService.shared.showNotification(latestToNofity!)
        }
    }
    
    func onP2PMsgRecieved(_ msgSize: Int32, msgList: [Any]!, alarmIdx: Int32) {}
    
    func onUpstreamMsgResult(_ resultCode: Int32, resultMsg: String!, msgId: String!) {}
    
    func onP2PMsgResult(_ resultCode: Int32, resultMsg: String!, msgId: String!) {}
}
