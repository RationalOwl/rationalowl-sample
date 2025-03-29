struct UmsProtocol {
    
    ////////////////////////////////////////////////
    // Push App
    ////////////////////////////////////////////////

    // App install
    static let APP_AUTH_NUMBER_CMD_ID = 301
    static let APP_VERITY_AUTH_NUMBER_CMD_ID = 302
    static let APP_INSTALL_CMD_ID = 303
    static let APP_UNREG_USER_CMD_ID = 305

    // Device type
    static let APP_TYPE_NOT_INSTALL = 0
    static let APP_TYPE_ANDROID = 1
    static let APP_TYPE_IOS = 2
}