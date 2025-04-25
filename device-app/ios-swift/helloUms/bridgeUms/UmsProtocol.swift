class UmsProtocol {

    ////////////////////////////////////////////////
    // Push App
    ////////////////////////////////////////////////

    // App install
    static let APP_AUTH_NUMBER_CMD_ID = 301
    static let APP_VERITY_AUTH_NUMBER_CMD_ID = 302
    static let APP_INSTALL_CMD_ID = 303

    static let APP_UNREG_USER_CMD_ID = 305

    // App job
    static let APP_MSG_READ_NOTI_CMD_ID = 311
    static let APP_MSG_INFO_CMD_ID = 312
    static let APP_IMG_DATA_CMD_ID = 313

    // Device type
    static let APP_TYPE_ANDROID = 1
    static let APP_TYPE_IOS = 2

    // Push msg data fields
    static let APP_PUSH_MSG_ID_KEY = "mId"
    static let APP_PUSH_TYPE_KEY = "type"
    static let APP_PUSH_TITLE_KEY = "title"
    static let APP_PUSH_BODY_KEY = "body"
    static let APP_SEND_TIME_KEY = "st"
    static let APP_IMG_ID_KEY = "ii"
}
