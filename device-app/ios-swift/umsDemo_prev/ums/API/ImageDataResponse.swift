import Foundation

struct ImageDataResponse: Decodable
{
    enum CodingKeys: String, CodingKey
    {
        case cid = "cId"
        case resultCode = "rc"
        case _imageData = "imgD"
    }

    let cid: CommandType
    let resultCode: ResultCode
    private let _imageData: String

    var imageData: Data? {
        return Data(base64Encoded: _imageData)
    }
}
