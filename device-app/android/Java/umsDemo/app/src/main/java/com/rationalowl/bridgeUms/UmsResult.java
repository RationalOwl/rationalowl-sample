package com.rationalowl.bridgeUms;

public class UmsResult {
    /* result code which is related to common(device & app server)[1~ -99] */
    public static final int RESULT_OK = 1;
    public static final int RESULT_UNKNOWN_ERROR = -1;

    /* web admin & msg send client */
    // server registration related code
    public static final int RESULT_MSG_CLIENT_NOT_YET_REGISTERED = -101;
    public static final int RESULT_MSG_CLIENT_ALREADY_REGISTERED = -102;

    /* account related result */
    public static final int RESULT_ACCOUNT_NOT_YET_REGISTERED = -201;
    public static final int RESULT_ACCOUNT_ALREADY_REGISTERED = -202;
    public static final int RESULT_ACCOUNT_ID_NOT_MATCH = -203;

    // apns certificate registration related code
    public static final int RESULT_CERT_NOT_YET_REGISTERED = -231;
    public static final int RESULT_CERT_ALREADY_REGISTERED = -232;

    /* message setting */
    public static final int RESULT_MSG_SETTING_NAME_DUPLICATE = -241;

    /* template */
    public static final int RESULT_MSG_TEMPLATE_NOT_EXIST = -251;

    /* push app */
    public static final int RESULT_PUSH_APP_USER_ALREADY_REGISTER = -301;

    // result messages
    public static final String RESULT_OK_MSG = "작업이  성공 했습니다.";
    public static final String RESULT_UNKNOWN_ERROR_MSG = "알 수 없는 에러입니다.";

    // web admin & msg client */
    public static final String RESULT_MSG_CLIENT_NOT_YET_REGISTERED_MSG = " 메시지 발신 클라이언트가 아직 등록되지 않았습니다.";
    public static final String RESULT_MSG_CLIENT_ALREADY_REGISTERED_MSG = "이미 등록하였습니다.";

    /* user related result */
    public static final String RESULT_ACCOUNT_NOT_YET_REGISTERED_MSG = "계정이 아직 등록되지 않았습니다.";
    public static final String RESULT_ACCOUNT_ALREADY_REGISTERED_MSG = "이미 계정이 존재합니다";
    public static final String RESULT_ACCOUNT_ID_NOT_MATCH_MSG = "계정 ID와 비밀번호가 일치하지 않습니다";

    public static final String RESULT_APNS_CERT_NOT_YET_REGISTERED_MSG = "APNS인증서가 아직 등록되지 않았습니다.";
    public static final String RESULT_APNS_CERT_ALREADY_REGISTERED_MSG = "이미 APNS인증서가 등록되었습니다";

    /* message setting */
    public static final String RESULT_MSG_SETTING_NAME_DUPLICATE_MSG = "동일한 설정명이 존재합니다.";

    /* template */
    public static final String RESULT_MSG_TEMPLATE_NOT_EXIST_MSG = "템플릿이 존재하지 않습니다.";

    /* push app */
    public static final String RESULT_PUSH_APP_USER_ALREADY_REGISTER_MSG = "이미 가입한 앱 사용자입니다.";


    public static String getResultMessage(int resultCode) {
        String msg = "";

        switch (resultCode) {
            case RESULT_OK:
                msg = RESULT_OK_MSG;
                break;
            case RESULT_UNKNOWN_ERROR:
                msg = RESULT_UNKNOWN_ERROR_MSG;
                break;

            /* web admin & msg client */
            case RESULT_MSG_CLIENT_NOT_YET_REGISTERED:
                msg = RESULT_MSG_CLIENT_NOT_YET_REGISTERED_MSG;
                break;
            case RESULT_MSG_CLIENT_ALREADY_REGISTERED:
                msg = RESULT_MSG_CLIENT_ALREADY_REGISTERED_MSG;
                break;

            /* user related result */
            case RESULT_ACCOUNT_NOT_YET_REGISTERED:
                msg = RESULT_ACCOUNT_NOT_YET_REGISTERED_MSG;
                break;
            case RESULT_ACCOUNT_ALREADY_REGISTERED:
                msg = RESULT_ACCOUNT_ALREADY_REGISTERED_MSG;
                break;
            case RESULT_ACCOUNT_ID_NOT_MATCH:
                msg = RESULT_ACCOUNT_ID_NOT_MATCH_MSG;
                break;

            /* apns/fcm cert */
            case RESULT_CERT_NOT_YET_REGISTERED:
                msg = RESULT_APNS_CERT_NOT_YET_REGISTERED_MSG;
                break;
            case RESULT_CERT_ALREADY_REGISTERED:
                msg = RESULT_APNS_CERT_ALREADY_REGISTERED_MSG;
                break;

            /* message setting */
            case RESULT_MSG_SETTING_NAME_DUPLICATE:
                msg = RESULT_MSG_SETTING_NAME_DUPLICATE_MSG;
                break;

            /* push app */
            case RESULT_PUSH_APP_USER_ALREADY_REGISTER:
                msg = RESULT_PUSH_APP_USER_ALREADY_REGISTER_MSG;
                break;

            default:
                msg = RESULT_UNKNOWN_ERROR_MSG;
                break;
        }
        return msg;
    }
}
