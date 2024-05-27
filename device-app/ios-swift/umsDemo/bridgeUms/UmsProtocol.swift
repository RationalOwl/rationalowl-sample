public enum UmsProtocol {
    ////////////////////////////////////////////////
    // common
    ////////////////////////////////////////////////

    // request type
    public static let REQUEST_TYPE: Int = 1
    public static let RESPONSE_TYPE: Int = 2

    ////////////////////////////////////////////////
    // Push App
    ////////////////////////////////////////////////

    // app install
    public static let APP_AUTH_NUMBER_CMD_ID: Int = 301
    public static let APP_VERITY_AUTH_NUMBER_CMD_ID: Int = 302
    public static let APP_INSTALL_CMD_ID: Int = 303

    public static let APP_UNREG_USER_CMD_ID: Int = 305

    // app job
    public static let APP_MSG_READ_NOTI_CMD_ID: Int = 311
    public static let APP_MSG_INFO_CMD_ID: Int = 312
    public static let APP_IMG_DATA_CMD_ID: Int = 313

    // device type
    public static let APP_TYPE_NOT_INSTALL: Int = 0
    public static let APP_TYPE_ANDROID: Int = 1
    public static let APP_TYPE_IOS: Int = 2

    // push msg data fields
    public static let APP_PUSH_MSG_ID_KEY: String = "mId"
    public static let APP_PUSH_TYPE_KEY: String = "type"
    public static let APP_PUSH_TITLE_KEY: String = "title"
    public static let APP_PUSH_BODY_KEY: String = "body"
    public static let APP_SEND_TIME_KEY: String = "st"
    public static let APP_IMG_ID_KEY: String = "ii"
}
