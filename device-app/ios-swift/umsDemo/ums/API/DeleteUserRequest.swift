struct DeleteUserRequest: Encodable
{
    enum CodingKeys: String, CodingKey
    {
        case cid = "cId"
        case accountId = "aId"
        case phoneNumber = "pn"
    }

    let cid: CommandType = .deleteUser
    var phoneNumber: String
    let accountId: String = Config.shared.umsAccountId
}
