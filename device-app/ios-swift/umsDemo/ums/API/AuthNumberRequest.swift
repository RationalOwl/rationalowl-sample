struct AuthNumberRequest: Codable
{
    enum CodingKeys: String, CodingKey
    {
        case cid = "cId"
        case accountId = "aId"
        case countryCode = "cc"
        case phoneNumber = "pn"
    }
    
    let cid: CommandType = .authNumber
    let accountId: String = Config.shared!.umsAccountId
    let countryCode, phoneNumber: String
}
