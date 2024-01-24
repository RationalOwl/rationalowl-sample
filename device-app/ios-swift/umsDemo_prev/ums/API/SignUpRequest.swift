import Foundation

struct SignUpRequest: Encodable
{
    enum CodingKeys: String, CodingKey
    {
        case cid = "cId"
        case accountId = "aId"
        case deviceType = "dt"
        case deviceRegId = "dRId"
        case phoneNumber = "pn"
        case userId = "auId"
        case userName = "n"
    }
    
    let cid: CommandType = .signUp
    let accountId: String = Config.shared.umsAccountId
    let deviceType: DeviceType = .iOS
    let deviceRegId, phoneNumber, userId, userName: String
}
