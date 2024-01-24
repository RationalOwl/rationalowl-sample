import Foundation

struct MessageInfoRequest: Codable
{
    enum CodingKeys: String, CodingKey
    {
        case cid = "cId"
        case accountId = "aId"
        case phoneNumber = "pn"
        case deviceRegId = "dRId"
        case messageId = "mId"
    }

    let cid: CommandType = .messageInfo
    let accountId: String = Config.shared.umsAccountId
    var phoneNumber: String
    var deviceRegId: String
    var messageId: String
}
