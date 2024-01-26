import Foundation

struct SignUpResponse: Decodable
{
    enum CodingKeys: String, CodingKey
    {
        case cid = "cId"
        case accountId = "aId"
        case resultCode = "rc"
        case umsServerRegId = "usRid"
    }
    
    let cid: CommandType
    let accountId: String?
    let resultCode: ResultCode
    let umsServerRegId: String?
}
