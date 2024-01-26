import Foundation

enum DeliveryState: Int, Codable
{
    case unsend = 0
    case sendRequest = 1
    case success = 2
    case fail = 3
}

struct MessageInfoResponse: Codable
{
    enum CodingKeys: String, CodingKey
    {
        case cid = "cId"
        case resultCode = "rc"
        case messageId = "mId"
        case alimTalkSentAt = "ast"
        case alimTalkState = "as"
        case munjaSentAt = "mst"
        case munjaType = "mt"
        case munjaState = "ms"
    }

    let cid: CommandType
    let resultCode: ResultCode
    let messageId: String
    let alimTalkSentAt: Int64
    let alimTalkState: DeliveryState
    let munjaSentAt: Int64
    let munjaType: MunjaType
    let munjaState: DeliveryState
}
