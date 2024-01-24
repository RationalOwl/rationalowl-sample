struct DeleteUserResponse: Decodable
{
    enum CodingKeys: String, CodingKey
    {
        case cid = "cId"
        case accountId = "aId"
        case resultCode = "rc"
        case phoneNumber = "pn"
    }

    let cid: CommandType
    let accountId: String
    let resultCode: ResultCode
    let phoneNumber: String
}
