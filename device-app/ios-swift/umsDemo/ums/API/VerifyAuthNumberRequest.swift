import Foundation

struct VerifyAuthNumberRequest: Encodable
{
    enum CodingKeys: String, CodingKey
    {
        case cid = "cId"
        case accountId = "aId"
        case deviceType = "dt"
        case phoneNumber = "pn"
        case authNumber = "an"
    }
    
    let cid: CommandType = .verifyAuthNumber
    let accountId: String = Config.shared.umsAccountId
    let deviceType: DeviceType = .iOS
    var phoneNumber: String
    var authNumber: String
}
