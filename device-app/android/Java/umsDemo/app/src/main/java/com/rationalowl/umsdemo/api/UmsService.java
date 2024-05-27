package com.rationalowl.umsdemo.api;

import android.util.Log;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rationalowl.bridgeUms.PushAppProto;
import com.rationalowl.umsdemo.data.Config;
import com.rationalowl.umsdemo.data.DataCallback;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class UmsService {
    private static final String TAG = "UmsService";

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final int UMS_PORT = 36001;

    private static UmsService instance;

    public static synchronized UmsService getInstance() {
        if (instance == null) {
            final String host = Config.getInstance().getUmsHost();
            instance = new UmsService(host, UMS_PORT);
        }

        return instance;
    }

    private final String baseUrl;

    private final ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private final OkHttpClient httpClient = new OkHttpClient();

    private UmsService(String host, int port) {
        this.baseUrl = host + ":" + port + "/";
    }

    public void requestAuthNumber(PushAppProto.PushAppAuthNumberReq request, DataCallback<PushAppProto.PushAppAuthNumberRes> callback) {
        post(PushAppProto.PushAppAuthNumberRes.class, "pushApp/reqAuthNumber", request, callback);
    }

    public void verifyAuthNumber(PushAppProto.PushAppVerifyAuthNumberReq request, DataCallback<PushAppProto.PushAppVerifyAuthNumberRes> callback) {
        post(PushAppProto.PushAppVerifyAuthNumberRes.class, "pushApp/verifyAuthNumber", request, callback);
    }

    public void signUp(PushAppProto.PushAppInstallReq request, DataCallback<PushAppProto.PushAppInstallRes> callback) {
        post(PushAppProto.PushAppInstallRes.class, "pushApp/installApp", request, callback);
    }

    public void getMessageInfo(PushAppProto.PushAppMsgInfoReq request, DataCallback<PushAppProto.PushAppMsgInfoRes> callback) {
        post(PushAppProto.PushAppMsgInfoRes.class, "pushApp/msgInfo", request, callback);
    }

    public void getImageData(PushAppProto.PushAppImgDataReq request, DataCallback<PushAppProto.PushAppImgDataRes> callback) {
        post(PushAppProto.PushAppImgDataRes.class, "pushApp/imgData", request, callback);
    }

    public void setMessageRead(PushAppProto.PushAppMsgReadNoti request) {
        post(String.class, "pushApp/notiRead", request);
    }

    public void deleteUser(PushAppProto.PushAppUnregUserReq request, DataCallback<PushAppProto.PushAppUnregUserRes> callback) {
        post(PushAppProto.PushAppUnregUserRes.class, "pushApp/unregUser", request, callback);
    }

    private <TResponseType, TRequestType> void post(Class<TResponseType> responseType, String path, TRequestType request) {
        post(responseType, path, request, null);
    }

    private <TResponseType, TRequestType> void post(Class<TResponseType> responseType, String path, TRequestType request, DataCallback<TResponseType> callback) {
        try {
            final String jsonString = objectMapper.writeValueAsString(request);
            final RequestBody requestBody = RequestBody.create(jsonString, JSON);

            final Request httpRequest = new Request.Builder()
                    .url(baseUrl + path)
                    .post(requestBody)
                    .build();

            Log.d(TAG, "Request to " + baseUrl + path + ": " + jsonString);

            httpClient.newCall(httpRequest).enqueue(new okhttp3.Callback() {
                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (callback == null) {
                        response.close();
                        return;
                    }

                    final String payload;

                    try (final ResponseBody responseBody = response.body()) {
                        if (responseBody == null) return;
                        payload = responseBody.string();
                    }

                    Log.d(TAG, "Response from " + baseUrl + path + ": " + payload);

                    final TResponseType result;

                    if (responseType == String.class) {
                        result = (TResponseType) payload;
                    } else {
                        result = objectMapper.readValue(payload, responseType);
                    }

                    callback.onResponse(result);
                }

                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Log.e(TAG, "Request to " + baseUrl + path + " failed", e);

                    if (callback == null) return;
                    callback.onFailure(e);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
