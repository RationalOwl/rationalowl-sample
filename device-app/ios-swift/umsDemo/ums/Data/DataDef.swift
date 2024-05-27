import Foundation
import UIKit

class DataDef {
    class Message: Codable, Equatable {
        let id: String
        let type: MessageType
        let title, body: String
        let sentAt: Date
        let receivedAt: Date
        let imageId: String?

        var isRead: Bool = false
        var alimTalkSentAt, munjaSentAt: Date?
        var munjaType: MunjaType?

        init(_ map: [AnyHashable: Any], receivedAt: Date = Date()) {
            id = map["mId"] as! String
            type = map["type"] as? String == "2" ? .emergency : .normal
            title = map["title"] as? String ?? ""
            body = map["body"] as? String ?? ""
            sentAt = Date(timeIntervalSince1970: (TimeInterval(map["st"] as? String ?? "") ?? 0) / 1000)
            self.receivedAt = receivedAt
            imageId = map["ii"] as? String
        }

        static func == (lhs: Message, rhs: Message) -> Bool {
            return lhs.id == rhs.id
        }
    }

    enum MessageType: Int, Codable {
        case normal = 1
        case emergency = 2
    }

    enum MunjaType: Int, Codable {
        case none = 0
        case sms = 12
        case lms = 13
        case mms = 14
    }

    struct Server: Codable {
        let regId: String
    }

    struct User: Codable {
        let regId: String
        let phoneCountryCode, phoneNumber: String
        let name: String
        let userId: String?
        let joinedAt: Date

        init(regId: String, phoneCountryCode: String, phoneNumber: String, name: String, userId: String?) {
            self.regId = regId
            self.phoneCountryCode = phoneCountryCode
            self.phoneNumber = phoneNumber
            self.name = name
            self.userId = userId
            joinedAt = Date()
        }
    }
}

extension DataDef.MunjaType: CustomStringConvertible
{
    var description: String
    {
        switch self
        {
        case .sms: return "SMS"
        case .lms: return "LMS"
        case .mms: return "MMS"
        default:   return ""
        }
    }
}
