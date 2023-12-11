import Foundation

enum CommandType: Int, Codable
{
    case authNumber = 301
    case verifyAuthNumber = 302
    case signUp = 303
    case deleteUser = 305
    case messageRead = 311
    case messageInfo = 312
    case imageData = 313
}
