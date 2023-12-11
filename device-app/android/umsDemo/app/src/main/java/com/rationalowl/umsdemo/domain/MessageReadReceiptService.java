package com.rationalowl.umsdemo.domain;

import android.util.Log;

import androidx.annotation.NonNull;

import com.rationalowl.umsdemo.protocol.PushAppProto;
import com.rationalowl.umsdemo.protocol.UmsClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageReadReceiptService {
    private final static String TAG = "MessageReadReceiptService";

    private static MessageReadReceiptService instance;

    public static synchronized MessageReadReceiptService getInstance() {
        if (instance == null) {
            instance = new MessageReadReceiptService();
        }

        return instance;
    }

    private final MessagesRepository repository = MessagesRepository.getInstance();

    public void markAsRead(Message message) {
        Log.d(TAG, "markAsRead(" + message.getId() + ")");

        sendReadRequest(message);
        repository.setAsRead(message.getId());
    }

    private void sendReadRequest(Message message) {
        Log.d(TAG, "sendReadRequest(" + message.getId() + ")");

        final PushAppProto.PushAppMsgReadNoti request = new PushAppProto.PushAppMsgReadNoti();
        final User user = UserRepository.getInstance().getUser();
        request.mAccountId = Config.getInstance().getUmsAccountId();
        request.mPhoneNum = user.getPhoneNumber();
        request.mDeviceRegId = user.getRegId();
        request.mMsgId = message.getId();

        UmsClient.getService().setMessageRead(request).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
            }
        });
    }
}
