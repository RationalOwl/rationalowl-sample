package com.rationalowl.umsdemo;

import android.util.Log;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rationalowl.minerva.client.android.MessageListener;
import com.rationalowl.minerva.client.android.MinervaManager;
import com.rationalowl.umsdemo.data.DataDef;
import com.rationalowl.umsdemo.data.datasource.MessageLocalDataSource;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RoMessageListener implements MessageListener {
    private static final String TAG = "RoMessageListener";

    private final ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @Override
    public void onP2PMsgRecieved(ArrayList<JSONObject> msgList) {
        // hello app don't treat realtime data
        Log.d(TAG, "onP2PMsgRecieved(" + msgList.size() + ")");
    }

    @Override
    public void onDownstreamMsgRecieved(ArrayList<JSONObject> msgList) {
        // hello app don't treat realtime data
        Log.d(TAG, "onDownstreamMsgRecieved(" + msgList.size() + ")");
    }

    @Override
    public void onPushMsgRecieved(ArrayList<JSONObject> msgList) {
        Log.d(TAG, "onPushMsgRecieved(" + msgList.size() + ")");

        final List<DataDef.Message> messages = new ArrayList<>();

        // recent messages are ordered by message send time descending order [recentest, recent, old, older, ... oldest]
        for (final JSONObject json : msgList) {
            // this app just use custom push and user data only.
            try {
                final String data = (String) json.get(MinervaManager.FIELD_MSG_DATA);
                final Map<String, String> map = objectMapper.readValue(data, new TypeReference<Map<String, String>>() {
                });

                final DataDef.Message message = new DataDef.Message(map);
                if (MessageLocalDataSource.getInstance().containsMessage(message.getId())) continue;

                MessageLocalDataSource.getInstance().addMessage(message);
                messages.add(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // if multiple custom push received, we just notify recentest push only.
        if (!messages.isEmpty()) {
            messages.sort((m1, m2) -> m2.getSentAt().compareTo(m1.getSentAt()));
            NotificationService.showNotification(messages.get(0));
        }
    }

    @Override
    public void onSendUpstreamMsgResult(int resultCode, String resultMsg, String msgId) {
        Log.d(TAG, "onSendUpstreamMsgResult(resultCode: " + resultCode + ", resultMsg: " + resultMsg + ", msgId: " + msgId + ")");
    }

    @Override
    public void onSendP2PMsgResult(int resultCode, String resultMsg, String msgId) {
        Log.d(TAG, "onSendP2PMsgResult(resultCode: " + resultCode + ", resultMsg: " + resultMsg + ", msgId: " + msgId + ")");
    }
}
