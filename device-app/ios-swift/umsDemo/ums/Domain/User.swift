import Foundation

struct User: Codable
{
    let regId: String
    let phoneCountryCode, phoneNumber: String
    let name: String
    let userId: String?
    let joinedAt: Date
    
    init(regId: String, phoneCountryCode: String, phoneNumber: String, name: String, userId: String?)
    {
        self.regId = regId
        self.phoneCountryCode = phoneCountryCode
        self.phoneNumber = phoneNumber
        self.name = name
        self.userId = userId
        self.joinedAt = Date()
    }
}
