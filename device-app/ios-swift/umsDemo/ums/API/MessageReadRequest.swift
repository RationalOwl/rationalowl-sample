import Foundation

enum RequestType: Int, Codable
{
    case request = 1
    case response = 2
}

struct MessageReadRequest: Codable
{
    enum CodingKeys: String, CodingKey
    {
        case cid = "cId"
        case requestType = "r"
        case accountId = "aId"
        case phoneNumber = "pn"
        case deviceRegId = "dRId"
        case messageId = "mId"
    }

    let cid: CommandType = .messageRead
    let requestType: RequestType = .request
    let accountId: String = Config.shared.umsAccountId
    var phoneNumber: String
    var deviceRegId: String
    var messageId: String
}
