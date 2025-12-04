package com.rationalowl.umsdemo;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rationalowl.minerva.client.android.MessageListener;
import com.rationalowl.minerva.client.android.MinervaManager;
import com.rationalowl.minerva.client.android.util.Logger;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class RoMessageListener implements MessageListener {
    private static final String TAG = "RoMsgListener";

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void onP2PMsgRecieved(ArrayList<JSONObject> msgList) {
        // hello app don't treat realtime data
        Log.d(TAG, "onP2PMsgRecieved enter");
    }

    @Override
    public void onDownstreamMsgRecieved(ArrayList<JSONObject> msgList) {
        // hello app don't treat realtime data
        Log.d(TAG, "onDownstreamMsgRecieved enter");
    }

    @Override
    public void onPushMsgRecieved(ArrayList<JSONObject> msgList) {
        Log.d(TAG, "onPushMsgRecieved enter");

        int msgSize = msgList.size();

        Log.d(TAG, msgSize + " message received");

        try {
            JSONObject oneMsg = null;
            int msgType;
            String sender = null;
            long serverTime;
            String data = null;
            Map<String, String> customPush = null;

            // recent messages are ordered by message send time descending order [recentest, recent, old, older, ... oldest]
            for (JSONObject json : msgList) {
                // message sender (sender registration id)
                sender = (String) json.get(MinervaManager.FIELD_MSG_SENDER);
                // data
                data = (String) json.get(MinervaManager.FIELD_MSG_DATA);

                Log.d(TAG,  "message sender:" + sender);
                // custom data formatted json format
                Log.d(TAG,  "message :" + data);
                // custom push format can be any fields app need.
                // RationalUms Demo format
                /*
                {
                  "mId": "message id here",
                  "title": "message title here", // (optional):
                  "body": "message body here",
                  "st": "(message) send time",
                  "ii": "image id here"         // (optional) : 이미지 첨부시 이미지 아이디 세팅
                }
                */

                if(customPush == null) {
                    customPush = mapper.readValue(data, new TypeReference<Map<String, String>>() {});

                    String msgId = null, body = null, title = null, imageId = null;
                    // mandatory fields
                    msgId = customPush.get("mId");
                    body = customPush.get("body");

                    // optional fields
                    if(customPush.containsKey("title")) {
                        title = customPush.get("title");
                    }
                    if(customPush.containsKey("ii")) {
                        imageId = customPush.get("ii");
                    }

                    String[] extra = new String[4];
                    extra[0] = msgId;
                    extra[1] = body;
                    extra[2] = title;
                    extra[3] = imageId;
                    Context context = MinervaManager.getContext();
                    Intent intent = new Intent(MainActivity.MESSAGE_FROM_RATIONALOWL_MSG_LISTENER_ACTION);
                    intent.putExtra(MainActivity.MESSAGE_FROM_RATIONALOWL_MSG_LISTENER_KEY, extra);
                    context.sendBroadcast(intent);
                }
            }
            // if multiple custom push received, we just notify recentest push only.
            if(customPush != null) {
                MyFirebaseMessagingService.showCustomNotification(customPush);
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSendUpstreamMsgResult(int resultCode, String resultMsg, String msgId) {
        Logger.debug(TAG, "onSendUpstreamMsgResult enter");
    }

    @Override
    public void onSendP2PMsgResult(int resultCode, String resultMsg, String msgId) {
        Logger.debug(TAG, "onSendP2PMsgResult enter");
    }
}
