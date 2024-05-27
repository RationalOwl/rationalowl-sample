package com.rationalowl.umsdemo.data.repository;

import android.util.Base64;
import android.util.Log;

import com.rationalowl.bridgeUms.PushAppProto;
import com.rationalowl.bridgeUms.UmsResult;
import com.rationalowl.umsdemo.api.UmsService;
import com.rationalowl.umsdemo.data.Config;
import com.rationalowl.umsdemo.data.DataCallback;
import com.rationalowl.umsdemo.data.DataDef;
import com.rationalowl.umsdemo.data.datasource.MessageLocalDataSource;
import com.rationalowl.umsdemo.data.datasource.UserLocalDataSource;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class MessageRepository {
    private static final String TAG = "MessageRepository";

    private static MessageRepository instance;

    public static synchronized MessageRepository getInstance() {
        if (instance == null) {
            instance = new MessageRepository();
        }

        return instance;
    }

    public void getImage(DataDef.Message message, DataCallback<byte[]> callback) {
        final String imageId = message.getImageId();
        if (imageId == null) return;

        final PushAppProto.PushAppImgDataReq request = new PushAppProto.PushAppImgDataReq();
        request.mAccountId = Config.getInstance().getUmsAccountId();
        request.mImgId = imageId;
        request.mMsgId = message.getId();

        UmsService.getInstance().getImageData(request, new DataCallback<PushAppProto.PushAppImgDataRes>() {
            @Override
            public void onResponse(PushAppProto.PushAppImgDataRes response) {
                final int resultCode = response.mResultCode;

                if (resultCode != UmsResult.RESULT_OK) {
                    Log.e(TAG, "[" + resultCode + "] " + response.mComment);
                    return;
                }

                final byte[] imageData = Base64.decode(response.mImgData, Base64.DEFAULT);
                callback.onResponse(imageData);
            }

            @Override
            public void onFailure(IOException e) {
                callback.onFailure(e);
            }
        });
    }

    public void getDeliveryInfo(DataDef.Message message, DataCallback<DataDef.Message> callback) {
        final long diffInMills = Math.abs(message.getSentAt().getTime() - new Date().getTime());
        final long diffDays = TimeUnit.DAYS.convert(diffInMills, TimeUnit.MILLISECONDS);

        if (diffDays >= 7) {
            callback.onResponse(message);
            return;
        }

        final DataDef.User user = UserLocalDataSource.getInstance().getUser();

        final PushAppProto.PushAppMsgInfoReq request = new PushAppProto.PushAppMsgInfoReq();
        request.mAccountId = Config.getInstance().getUmsAccountId();
        request.mPhoneNum = user.getPhoneNumber();
        request.mDeviceRegId = user.getRegId();
        request.mMsgId = message.getId();

        UmsService.getInstance().getMessageInfo(request, new DataCallback<PushAppProto.PushAppMsgInfoRes>() {
            @Override
            public void onResponse(PushAppProto.PushAppMsgInfoRes response) {
                final int resultCode = response.mResultCode;

                if (resultCode != UmsResult.RESULT_OK) {
                    Log.e(TAG, "[" + resultCode + "] " + response.mComment);
                    return;
                }

                final DataDef.Message newMessage = MessageLocalDataSource.getInstance().updateDeliveryInfo(message.getId(), response.mAlimtalkSendTime, response.mMunjaSendTime, response.mMunjaType);
                callback.onResponse(newMessage);
            }

            @Override
            public void onFailure(IOException e) {
                MessageLocalDataSource.getInstance().save();
                callback.onResponse(message);
            }
        });
    }

    public void setAsRead(DataDef.Message message) {
        sendReadRequest(message);
        MessageLocalDataSource.getInstance().markAsRead(message.getId());
    }

    public void setAllAsRead() {
        for (final DataDef.Message message : MessageLocalDataSource.getInstance().getMessages()) {
            sendReadRequest(message);
        }

        MessageLocalDataSource.getInstance().markAllAsRead();
    }

    private void sendReadRequest(DataDef.Message message) {
        final PushAppProto.PushAppMsgReadNoti request = new PushAppProto.PushAppMsgReadNoti();
        final DataDef.User user = UserLocalDataSource.getInstance().getUser();
        request.mAccountId = Config.getInstance().getUmsAccountId();
        request.mPhoneNum = user.getPhoneNumber();
        request.mDeviceRegId = user.getRegId();
        request.mMsgId = message.getId();

        UmsService.getInstance().setMessageRead(request);
    }
}
