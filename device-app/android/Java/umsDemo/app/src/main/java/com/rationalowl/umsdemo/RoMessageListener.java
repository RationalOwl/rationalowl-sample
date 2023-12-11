package com.rationalowl.umsdemo;

import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.rationalowl.minerva.client.android.MessageListener;
import com.rationalowl.minerva.client.android.MinervaManager;
import com.rationalowl.umsdemo.domain.Message;
import com.rationalowl.umsdemo.domain.MessagesRepository;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoMessageListener implements MessageListener {
    private static final String TAG = "RoMsgListener";
    private static final int MESSAGE_TYPE_CUSTOM = 3;

    private final ObjectMapper mapper = new ObjectMapper();
    private final MapType mapType;
    private final MessagesRepository repository = MessagesRepository.getInstance();

    public RoMessageListener() {
        final TypeFactory factory = TypeFactory.defaultInstance();
        mapType = factory.constructMapType(HashMap.class, String.class, String.class);
    }

    @Override
    public void onMsgReceived(ArrayList<JSONObject> objects) {
        // receive un-delivered push msg in the queuing period.
        Log.d(TAG, "onMsgReceived enter");

        final List<Message> messages = new ArrayList<>();

        // recent messages are ordered by message send time descending order [recentest, recent, old, older, oldest...]
        // treat old message first.
        for (JSONObject json : objects) {
            // this app just use custom push and user data only.
            try {
                final int type = (int) json.get(MinervaManager.FIELD_MSG_TYPE);  // 1 (realtime: downstream), 2 (realtime: p2p), 3(custom push)
                final String data = (String) json.get(MinervaManager.FIELD_MSG_DATA);

                if (type == MESSAGE_TYPE_CUSTOM) {
                    final Map<String, String> map = mapper.readValue(data, mapType);

                    final Message message = new Message(map);
                    if (repository.hasMessage(message.getId())) continue;

                    MessagesRepository.getInstance().addMessage(message);
                    messages.add(message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // only the latest msg notification alert.
        if (!messages.isEmpty()) {
            messages.sort((m1, m2) -> m2.getSentAt().compareTo(m1.getSentAt()));
            NotificationService.showNotification(messages.get(0));
        }
    }

    @Override
    public void onSendUpstreamMsgResult(int i, String s, String s1) {
    }

    @Override
    public void onSendP2PMsgResult(int i, String s, String s1) {
    }
}
