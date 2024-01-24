import Foundation

enum MunjaType: Int, Codable
{
    case none = 0
    case sms = 12
    case lms = 13
    case mms = 14
}

extension MunjaType: CustomStringConvertible
{
    var description: String
    {
        switch self
        {
        case .none: return "None"
        case .sms: return "SMS"
        case .lms: return "LMS"
        case .mms: return "MMS"
        }
    }
}
