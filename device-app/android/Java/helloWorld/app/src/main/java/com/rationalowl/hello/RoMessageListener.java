package com.rationalowl.hello;

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
    public void onMsgReceived(ArrayList<JSONObject> msgs) {
        Log.d(TAG, "onMsgReceived enter");

        int msgSize = msgs.size();

        Log.d(TAG, msgSize + " message received");

        try {
            JSONObject oneMsg = null;
            int msgType;
            String sender = null;
            long serverTime;
            String data = null;
            Map<String, String> customPush = null;

            // recent messages are ordered by message send time descending order [recentest, recent, old, older, ... oldest]
            for (JSONObject json : msgs) {
                // message type
                msgType = (int) json.get(MinervaManager.FIELD_MSG_TYPE);  // 1 (realtime: downstream), 2 (realtime: p2p), 3(custom push)
                // message sender (sender registration id)
                sender = (String) json.get(MinervaManager.FIELD_MSG_SENDER);
                // message send time
                serverTime = (Long) json.get(MinervaManager.FIELD_MSG_SERVER_TIME);
                // data
                data = (String) json.get(MinervaManager.FIELD_MSG_DATA);

                Log.d(TAG,  "message type:" + msgType);
                Log.d(TAG,  "message sender:" + sender);
                Log.d(TAG,  "message send time :" + serverTime);
                // custom data formatted json format
                Log.d(TAG,  "message :" + data);

                switch(msgType) {

                    case 1: // realtime downstream
                    case 2: {// realtime p2p
                        // this hello world don't handle realtime message
                        break;
                    }
                    // custom push received
                    case 3: {
                        // if multiple custom push received, we just notify recentest push only.
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
                        break;
                    }
                    default:
                        break;
                }
            }

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
