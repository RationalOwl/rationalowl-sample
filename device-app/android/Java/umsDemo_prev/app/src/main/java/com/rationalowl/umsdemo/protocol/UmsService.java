package com.rationalowl.umsdemo.protocol;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UmsService {
    @POST("pushApp/reqAuthNumber")
    Call<PushAppProto.PushAppAuthNumberRes> requestAuthNumber(@Body PushAppProto.PushAppAuthNumberReq request);

    @POST("pushApp/verifyAuthNumber")
    Call<PushAppProto.PushAppVerifyAuthNumberRes> verifyAuthNumber(@Body PushAppProto.PushAppVerifyAuthNumberReq request);

    @POST("pushApp/installApp")
    Call<PushAppProto.PushAppInstallRes> signUp(@Body PushAppProto.PushAppInstallReq request);

    @POST("pushApp/msgInfo")
    Call<PushAppProto.PushAppMsgInfoRes> getMessageInfo(@Body PushAppProto.PushAppMsgInfoReq request);

    @POST("pushApp/imgData")
    Call<PushAppProto.PushAppImgDataRes> getImageData(@Body PushAppProto.PushAppImgDataReq request);

    @POST("pushApp/notiRead")
    Call<String> setMessageRead(@Body PushAppProto.PushAppMsgReadNoti request);

    @POST("pushApp/unregUser")
    Call<PushAppProto.PushAppUnregUserRes> deleteUser(@Body PushAppProto.PushAppUnregUserReq request);
}
