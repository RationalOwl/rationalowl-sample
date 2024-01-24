import Foundation
import UIKit

struct Message : Codable, Equatable
{
    let id: String
    let type: MessageType
    let title, body: String
    let sentAt: Date
    let receivedAt: Date
    let imageId: String?
    
    var isRead: Bool = false
    var alimTalkSentAt, munjaSentAt: Date?
    var munjaType: MunjaType?
    
    init(_ map: [AnyHashable: Any], receivedAt: Date = Date())
    {
        self.id = map["mId"] as! String
        self.type = map["type"] as? String == "2" ? .emergency : .normal
        self.title = map["title"] as? String ?? ""
        self.body = map["body"] as? String ?? ""
        self.sentAt = Date(timeIntervalSince1970: (TimeInterval(map["st"] as? String ?? "") ?? 0) / 1000)
        self.receivedAt = receivedAt
        self.imageId = map["ii"] as? String
    }
    
    static func ==(lhs: Message, rhs: Message) -> Bool
    {
        return lhs.id == rhs.id
    }
}
