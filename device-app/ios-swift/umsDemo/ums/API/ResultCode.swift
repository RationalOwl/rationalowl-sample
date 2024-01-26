import Foundation

enum ResultCode: Int, Codable, CaseIterable
{
    case ok = 1
    case unknownError = -1
    case msgClientNotYetRegistered = -101
    case msgClientAlreadyRegistered = -102
    case accountNotYetRegistered = -201
    case accountAlreadyRegistered = -202
    case accountIdNotMatch = -203
    case certNotYetRegistered = -231
    case certAlreadyRegistered = -232
    case msgSettingNameDuplicate = -241
    case msgTemplateNotExist = -251
    case userAlreadyRegistered = -301

    var message: String
    {
        switch self
        {
            case .ok: return "작업이 성공했습니다."
            case .unknownError: return "알 수 없는 에러입니다."
            case .msgClientNotYetRegistered: return "메시지 발신 클라이언트가 아직 등록되지 않았습니다."
            case .msgClientAlreadyRegistered: return "이미 등록하였습니다."
            case .accountNotYetRegistered: return "계정이 아직 등록되지 않았습니다."
            case .accountAlreadyRegistered: return "이미 계정이 존재합니다"
            case .accountIdNotMatch: return "계정 ID와 비밀번호가 일치하지 않습니다"
            case .certNotYetRegistered: return "APNS 인증서가 아직 등록되지 않았습니다."
            case .certAlreadyRegistered: return "이미 APNS 인증서가 등록되었습니다"
            case .msgSettingNameDuplicate: return "동일한 설정명이 존재합니다."
            case .msgTemplateNotExist: return "템플릿이 존재하지 않습니다."
            case .userAlreadyRegistered: return "이미 가입한 앱 사용자입니다."
        }
    }
}
