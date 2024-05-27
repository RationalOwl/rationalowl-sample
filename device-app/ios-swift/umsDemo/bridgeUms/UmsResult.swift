public enum UmsResult {
    /* result code which is related to common(device & app server)[1~ -99] */
    public static let RESULT_OK: Int = 1
    public static let RESULT_UNKNOWN_ERROR: Int = -1

    /* web admin & msg send client */
    // server registration related code
    public static let RESULT_MSG_CLIENT_NOT_YET_REGISTERED: Int = -101
    public static let RESULT_MSG_CLIENT_ALREADY_REGISTERED: Int = -102

    /* account related result */
    public static let RESULT_ACCOUNT_NOT_YET_REGISTERED: Int = -201
    public static let RESULT_ACCOUNT_ALREADY_REGISTERED: Int = -202
    public static let RESULT_ACCOUNT_ID_NOT_MATCH: Int = -203

    // apns certificate registration related code
    public static let RESULT_CERT_NOT_YET_REGISTERED: Int = -231
    public static let RESULT_CERT_ALREADY_REGISTERED: Int = -232

    /* message setting */
    public static let RESULT_MSG_SETTING_NAME_DUPLICATE: Int = -241

    /* template */
    public static let RESULT_MSG_TEMPLATE_NOT_EXIST: Int = -251

    /* push app */
    public static let RESULT_PUSH_APP_USER_ALREADY_REGISTER: Int = -301

    // result messages
    public static let RESULT_OK_MSG: String = "작업이  성공 했습니다."
    public static let RESULT_UNKNOWN_ERROR_MSG: String = "알 수 없는 에러입니다."

    // web admin & msg client */
    public static let RESULT_MSG_CLIENT_NOT_YET_REGISTERED_MSG: String = " 메시지 발신 클라이언트가 아직 등록되지 않았습니다."
    public static let RESULT_MSG_CLIENT_ALREADY_REGISTERED_MSG: String = "이미 등록하였습니다."

    /* user related result */
    public static let RESULT_ACCOUNT_NOT_YET_REGISTERED_MSG: String = "계정이 아직 등록되지 않았습니다."
    public static let RESULT_ACCOUNT_ALREADY_REGISTERED_MSG: String = "이미 계정이 존재합니다"
    public static let RESULT_ACCOUNT_ID_NOT_MATCH_MSG: String = "계정 ID와 비밀번호가 일치하지 않습니다"

    public static let RESULT_APNS_CERT_NOT_YET_REGISTERED_MSG: String = "APNS인증서가 아직 등록되지 않았습니다."
    public static let RESULT_APNS_CERT_ALREADY_REGISTERED_MSG: String = "이미 APNS인증서가 등록되었습니다"

    /* message setting */
    public static let RESULT_MSG_SETTING_NAME_DUPLICATE_MSG: String = "동일한 설정명이 존재합니다."

    /* template */
    public static let RESULT_MSG_TEMPLATE_NOT_EXIST_MSG: String = "템플릿이 존재하지 않습니다."

    /* push app */
    public static let RESULT_PUSH_APP_USER_ALREADY_REGISTER_MSG: String = "이미 가입한 앱 사용자입니다."

    public static func getResultMessage(_ resultCode: Int) -> String {
        var msg = ""

        switch resultCode {
        case RESULT_OK:
            msg = RESULT_OK_MSG
        case RESULT_UNKNOWN_ERROR:
            msg = RESULT_UNKNOWN_ERROR_MSG

        /* web admin & msg client */
        case RESULT_MSG_CLIENT_NOT_YET_REGISTERED:
            msg = RESULT_MSG_CLIENT_NOT_YET_REGISTERED_MSG
        case RESULT_MSG_CLIENT_ALREADY_REGISTERED:
            msg = RESULT_MSG_CLIENT_ALREADY_REGISTERED_MSG

        /* user related result */
        case RESULT_ACCOUNT_NOT_YET_REGISTERED:
            msg = RESULT_ACCOUNT_NOT_YET_REGISTERED_MSG
        case RESULT_ACCOUNT_ALREADY_REGISTERED:
            msg = RESULT_ACCOUNT_ALREADY_REGISTERED_MSG
        case RESULT_ACCOUNT_ID_NOT_MATCH:
            msg = RESULT_ACCOUNT_ID_NOT_MATCH_MSG

        /* apns/fcm cert */
        case RESULT_CERT_NOT_YET_REGISTERED:
            msg = RESULT_APNS_CERT_NOT_YET_REGISTERED_MSG
        case RESULT_CERT_ALREADY_REGISTERED:
            msg = RESULT_APNS_CERT_ALREADY_REGISTERED_MSG

        /* message setting */
        case RESULT_MSG_SETTING_NAME_DUPLICATE:
            msg = RESULT_MSG_SETTING_NAME_DUPLICATE_MSG

        /* push app */
        case RESULT_PUSH_APP_USER_ALREADY_REGISTER:
            msg = RESULT_PUSH_APP_USER_ALREADY_REGISTER_MSG

        default:
            msg = RESULT_UNKNOWN_ERROR_MSG
        }
        return msg
    }
}
