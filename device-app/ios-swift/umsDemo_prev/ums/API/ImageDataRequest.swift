import Foundation

struct ImageDataRequest: Encodable
{
    enum CodingKeys: String, CodingKey
    {
        case cid = "cId"
        case accountId = "aId"
        case imageId = "iId"
        case messageId = "mId"
    }

    let cid: CommandType = .imageData
    var imageId: String
    var messageId: String
    let accountId: String = Config.shared.umsAccountId
}
