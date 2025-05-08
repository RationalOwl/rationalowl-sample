package com.rationalowl.bridgeUms;

import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class UmsApi {

    private static final String UMS_REST_SERVER = "https://dev.rationalowl.com:36001";

    // 필수 API
    private static final String UMS_APP_REGISTER_URL = UMS_REST_SERVER + "/pushApp/installApp";
    private static final String UMS_APP_UNREGISTER_URL = UMS_REST_SERVER + "/pushApp/unregUser";

    // 옵션 API
    private static final String UMS_APP_REQ_AUTH_NUMBER_URL = UMS_REST_SERVER + "/pushApp/reqAuthNumber";
    private static final String UMS_APP_VERIFY_AUTH_NUMBER_URL = UMS_REST_SERVER + "/pushApp/verifyAuthNumber";
    private static final String UMS_APP_NOTI_READ_URL = UMS_REST_SERVER + "/pushApp/notiRead";
    private static final String UMS_APP_MSG_INFO_URL = UMS_REST_SERVER + "/pushApp/msgInfo";
    private static final String UMS_APP_IMG_DATA_URL = UMS_REST_SERVER + "/pushApp/imgData";


    private static final OkHttpClient httpClient = new OkHttpClient();
    private static final ObjectMapper mapper = new ObjectMapper();




    ////////////////////////////////////////////////////////////////////
    // RationalUms 연동시 반드시 호출해야 하는 2개의 rest api
    ////////////////////////////////////////////////////////////////////


    // hello ums 샘플앱에서는 필수 api 2개의 사용을 보여준다.
    // 옵션 api의 사용법도 동일하기 때문에 앱에서 원하는 기능을 추가하여 사용할 수 있다.

    /**
     * 단말앱을 등록한다.
     *
     * @param accountId (필수)앱 서비스에 부여되는 아이디
     * @param deviceRegId (필수)단말앱 아이디
     * @param phoneNum (옵션)사용자 폰번호(푸시 알림 미 전달시 문자, 알림톡으로 수신하고자 할때)
     * @param appUserId (옵션)앱 사용자 아이디
     * @param name (옵션)앱 사용자 이름
     * @param callback (필수)API 결과 콜백
     */
    public static void callInstallUmsApp(String accountId, String deviceRegId, String phoneNum, String appUserId, String name, Callback callback) {
        Log.d("UmsApi","callInstallUmsApp enter");

        try {
            PushAppProto.PushAppInstallReq req = new PushAppProto.PushAppInstallReq();
            /* mandatory fields */
            req.mDeviceType = UmsProtocol.APP_TYPE_ANDROID;
            req.mAccountId = accountId;
            req.mDeviceRegId = deviceRegId;

            /* optional fields */
            req.mPhoneNumber = phoneNum;
            req.mAppUserId = appUserId;
            req.mUserName = name;

            String postBody = mapper.writeValueAsString(req);
            post(UMS_APP_REGISTER_URL, postBody, callback);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 단말앱을 탈퇴한다.
     *
     * @param accountId (필수)앱 서비스에 부여되는 아이디
     * @param deviceRegId (필수)단말앱 아이디
     * @param callback (필수)API 결과 콜백
     */
    public static void callUnregisterUmsApp(String accountId, String deviceRegId, Callback callback) {
        Log.d("UmsApi","callUnregisterUmsApp enter");

        try {
            PushAppProto.PushAppUnregUserReq req = new PushAppProto.PushAppUnregUserReq();
            /* mandatory fields */
            req.mAccountId = accountId;
            req.mDeviceRegId = deviceRegId;
            String postBody = mapper.writeValueAsString(req);
            post(UMS_APP_UNREGISTER_URL, postBody, callback);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    ////////////////////////////////////////////////////////////////////
    // optional APIs
    ////////////////////////////////////////////////////////////////////


    /**
     * 사용자 전화번호 검증을 위한 보안번호를 요청한다.
     * 이후 SMS로 보안번호가 날아온다.
     * 앱 가입시 사용자가 폰번호를 잘못 기입하는 확률이 최소 1프로 이상으로 폰번호를 통한 문자, 알림톡 통합 이용시 반드시
     * 해당 API를 통해 정확한 폰번호 입력을 권고한다.
     *
     * @param accountId (필수)앱 서비스에 부여되는 아이디
     * @param countryCode (필수) 전화번호 국가코드 (대한민국 82)
     * @param phoneNum (필수)사용자 폰번호
     * @param callback (필수)API 결과 콜백
     */
    public static void callReqAuthNumber(String accountId, String countryCode, String phoneNum, Callback callback) {
        Log.d("UmsApi","callReqAuthNumber enter");

        try {
            PushAppProto.PushAppAuthNumberReq req = new PushAppProto.PushAppAuthNumberReq();
            /* mandatory fields */
            req.mAccountId = accountId;
            req.mCountryCode = countryCode;
            req.mPhoneNumber = phoneNum;

            String postBody = mapper.writeValueAsString(req);
            post(UMS_APP_REQ_AUTH_NUMBER_URL, postBody, callback);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * callReqAuthNumber 호출결과 SMS로 전달받은 인증번호를 검증한다.
     *
     * @param accountId (필수)앱 서비스에 부여되는 아이디
     * @param phoneNum (필수)사용자 폰번호
     * @param authNumber (필수)callReqAuthNumber 호출결과 SMS로 전달받은 보안번호
     * @param callback (필수)API 결과 콜백
     */
    public static void callVerifyAuthNumber(String accountId, String phoneNum, String authNumber, Callback callback) {
        Log.d("UmsApi","callVerifyAuthNumber enter");

        try {
            PushAppProto.PushAppVerifyAuthNumberReq req = new PushAppProto.PushAppVerifyAuthNumberReq();
            /* mandatory fields */
            req.mDeviceType = UmsProtocol.APP_TYPE_ANDROID;
            req.mAccountId = accountId;
            req.mPhoneNumber = phoneNum;
            req.mAuthNumber = authNumber;

            String postBody = mapper.writeValueAsString(req);
            post(UMS_APP_VERIFY_AUTH_NUMBER_URL, postBody, callback);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 사용자 수신확인시 수신확인했음을 알려준다.
     * 해당 API호출로 실시간 모니터링, 발신내역, 통계 등에 수신확인 정보가 실시간 반영된다.
     *
     * @param accountId (필수)앱 서비스에 부여되는 아이디
     * @param deviceRegId (필수)단말앱 아이디
     * @param msgId (필수) 푸시알림 수신시 mId 필드에 기입된 메시지 아이디
     * @param callback (필수)API 결과 콜백
     */
    public static void callNotifyRead(String accountId, String deviceRegId, String msgId, Callback callback) {
        Log.d("UmsApi","callNotifyRead enter");

        try {
            PushAppProto.PushAppMsgReadNoti noti = new PushAppProto.PushAppMsgReadNoti();
            /* mandatory fields */
            noti.mAccountId = accountId;
            noti.mDeviceRegId = deviceRegId;
            noti.mMsgId = msgId;

            String postBody = mapper.writeValueAsString(noti);
            post(UMS_APP_NOTI_READ_URL, postBody, callback);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 푸시알림 외 메시지 전달 정보를 요청한다.
     * 전달정보는 알림톡 발신시간, 알림톡 전달상태(전달성공, 실패), 문자 발신시간, 발신한 문자종류(sms/lms/mms), 문자전달 상태(전달성공, 실패)이다.
     *
     * @param accountId (필수)앱 서비스에 부여되는 아이디
     * @param deviceRegId (필수)단말앱 아이디
     * @param msgId (필수) 푸시알림 수신시 mId 필드에 기입된 메시지 아이디
     * @param callback (필수)API 결과 콜백
     */
    public static void callMsgInfo(String accountId, String deviceRegId, String msgId, Callback callback) {
        Log.d("UmsApi","callMsgInfo enter");

        try {
            PushAppProto.PushAppMsgInfoReq req = new PushAppProto.PushAppMsgInfoReq();
            /* mandatory fields */
            req.mAccountId = accountId;
            req.mDeviceRegId = deviceRegId;
            req.mMsgId = msgId;

            String postBody = mapper.writeValueAsString(req);
            post(UMS_APP_MSG_INFO_URL, postBody, callback);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 이미지를 첨부한 알림 발신시 푸시알림에 ii 필드에 이미지 아이디가 세팅되어 오는데 해당 아이디로 이미지 데이터를 요청한다.
     *
     * @param accountId (필수)앱 서비스에 부여되는 아이디
     * @param msgId (필수) 푸시알림 수신시 mId 필드에 기입된 메시지 아이디
     * @param imgId (필수) 푸시알림 수신시 ii 필드에 기입된 이미지 아이디
     * @param callback (필수)API 결과 콜백
     */
    public static void callImgData(String accountId, String msgId, String imgId, Callback callback) {
        Log.d("UmsApi","callImgData enter");

        try {
            PushAppProto.PushAppImgDataReq req = new PushAppProto.PushAppImgDataReq();
            /* mandatory fields */
            req.mAccountId = accountId;
            req.mMsgId = msgId;
            req.mImgId = imgId;

            String postBody = mapper.writeValueAsString(req);
            post(UMS_APP_IMG_DATA_URL, postBody, callback);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    ////////////////////////////////////////////////////////////////////
    //  private functions
    ////////////////////////////////////////////////////////////////////
    private static void post(String url, String postBody, Callback callback) throws Exception{
        Log.d("UmsApi", postBody);
        RequestBody requestBody = RequestBody.create(postBody, MediaType.parse("application/json; charset=utf-8"));
        Request.Builder builder = new Request.Builder().url(url).post(requestBody);
        Request request = builder.build();
        httpClient.newCall(request).enqueue(callback);
    }

}
