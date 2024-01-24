import Foundation

struct VerifyAuthNumberResponse: Decodable
{
    enum CodingKeys: String, CodingKey
    {
        case cid = "cId"
        case accountId = "aId"
        case resultCode = "rc"
        case comment = "c"
    }

    let cid: CommandType
    let accountId: String
    let resultCode: ResultCode
    let comment: String?
}
