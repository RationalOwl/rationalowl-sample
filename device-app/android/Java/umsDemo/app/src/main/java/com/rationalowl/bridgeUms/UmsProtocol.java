package com.rationalowl.bridgeUms;


public class UmsProtocol {

    ////////////////////////////////////////////////
    // common
    ////////////////////////////////////////////////

    // request type
    public static final int REQUEST_TYPE = 1;
    public static final int RESPONSE_TYPE = 2;


    ////////////////////////////////////////////////
    // Push App
    ////////////////////////////////////////////////

    // app install
    public static final int APP_AUTH_NUMBER_CMD_ID = 301;
    public static final int APP_VERITY_AUTH_NUMBER_CMD_ID = 302;
    public static final int APP_INSTALL_CMD_ID = 303;

    public static final int APP_UNREG_USER_CMD_ID = 305;

    // app job
    public static final int APP_MSG_READ_NOTI_CMD_ID = 311;
    public static final int APP_MSG_INFO_CMD_ID = 312;
    public static final int APP_IMG_DATA_CMD_ID = 313;

    // device type
    public static final int APP_TYPE_NOT_INSTALL = 0;
    public static final int APP_TYPE_ANDROID = 1;
    public static final int APP_TYPE_IOS = 2;

    // push msg data fields
    public static String APP_PUSH_MSG_ID_KEY = "mId";
    public static String APP_PUSH_TYPE_KEY = "type";
    public static String APP_PUSH_TITLE_KEY = "title";
    public static String APP_PUSH_BODY_KEY = "body";
    public static String APP_SEND_TIME_KEY = "st";
    public static String APP_IMG_ID_KEY = "ii";

}
