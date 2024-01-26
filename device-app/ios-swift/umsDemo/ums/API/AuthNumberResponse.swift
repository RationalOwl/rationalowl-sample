import Foundation

struct AuthNumberResponse: Decodable
{
    enum CodingKeys: String, CodingKey
    {
        case cid = "cId"
        case accountId = "aId"
        case resultCode = "rc"
    }

    let cid: CommandType
    let accountId: String
    let resultCode: ResultCode
}
