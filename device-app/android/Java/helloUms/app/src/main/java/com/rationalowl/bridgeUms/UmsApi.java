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

    private static final OkHttpClient httpClient = new OkHttpClient();
    private static final ObjectMapper mapper = new ObjectMapper();


    ////////////////////////////////////////////////////////////////////
    // RationalUms 연동시 반드시 호출해야 하는 2개의 rest api
    ////////////////////////////////////////////////////////////////////

    public static void callInstallUmsApp(String accountId, String deviceRegId, String phoneNum, String appUserId, String name, Callback callback) {
        Log.d("UmsApi","callSendLms enter");

        try {
            String url = UMS_REST_SERVER + "/pushApp/installApp";
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
            Log.d("UmsApi", postBody);
            RequestBody requestBody = RequestBody.create(postBody, MediaType.parse("application/json; charset=utf-8"));
            Request.Builder builder = new Request.Builder().url(url).post(requestBody);
            Request request = builder.build();
            httpClient.newCall(request).enqueue(callback);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void callUnregisterUmsApp(String accountId, String deviceRegId, Callback callback) {
        Log.d("UmsApi","callUnregisterUmsApp enter");

        try {
            String url = UMS_REST_SERVER + "/pushApp/unregUser";
            PushAppProto.PushAppUnregUserReq req = new PushAppProto.PushAppUnregUserReq();
            /* mandatory fields */
            req.mAccountId = accountId;
            req.mDeviceRegId = deviceRegId;
            String postBody = mapper.writeValueAsString(req);
            RequestBody requestBody = RequestBody.create(postBody, MediaType.parse("application/json; charset=utf-8"));
            Request.Builder builder = new Request.Builder().url(url).post(requestBody);
            Request request = builder.build();
            httpClient.newCall(request).enqueue(callback);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
