package com.rationalowl.umsdemo.data.datasource;

import android.util.Log;

import com.rationalowl.umsdemo.data.Config;
import com.rationalowl.umsdemo.data.DataDef;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class MessageLocalDataSource extends JsonLocalDataSource<DataDef.Message[]> {
    private static final String TAG = "MessagesRepository";
    private static final String FILE_NAME = "messages.json";

    public interface OnMessagesChangedListener {
        void onMessagesChange(List<DataDef.Message> messages);
    }

    private static MessageLocalDataSource instance;

    public static synchronized MessageLocalDataSource getInstance() {
        if (instance == null) {
            instance = new MessageLocalDataSource();
        }

        return instance;
    }

    private final List<DataDef.Message> messages = new ArrayList<>();
    private final Map<String, DataDef.Message> messageIdMappings = new HashMap<>();

    private OnMessagesChangedListener onMessagesChangeListener;

    public MessageLocalDataSource() {
        super(DataDef.Message[].class, FILE_NAME);
        loadRecentMessages();
    }

    public void setOnMessagesChangeListener(OnMessagesChangedListener listener) {
        onMessagesChangeListener = listener;
    }

    public void addMessage(DataDef.Message message) {
        Log.d(TAG, "addMessage(" + message.getId() + ")");

        if (containsMessage(message.getId())) return;

        synchronized (this) {
            messages.add(message);
            messages.sort((m1, m2) -> m2.getSentAt().compareTo(m1.getSentAt()));
            messageIdMappings.put(message.getId(), message);
        }

        onMessagesChange();
        save();
    }

    public synchronized boolean containsMessage(String messageId) {
        return messageIdMappings.containsKey(messageId);
    }

    public synchronized DataDef.Message getMessage(String messageId) {
        return messageIdMappings.get(messageId);
    }

    public List<DataDef.Message> getMessages() {
        return messages;
    }

    public void removeMessage(String messageId) {
        Log.d(TAG, "removeMessage(" + messageId + ")");

        synchronized (this) {
            messages.removeIf(message -> message.getId().equals(messageId));
            messageIdMappings.remove(messageId);
        }

        onMessagesChange();
        save();
    }

    public void removeMessages(Set<String> messageIds) {
        Log.d(TAG, "removeMessages([" + String.join(", ", messageIds) + "])");

        synchronized (this) {
            messages.removeIf(message -> messageIds.contains(message.getId()));

            for (String messageId : messageIds) {
                messageIdMappings.remove(messageId);
            }
        }

        onMessagesChange();
        save();
    }

    public void markAsRead(String messageId) {
        Log.d(TAG, "setAsRead(" + messageId + ")");

        synchronized (this) {
            try {
                final DataDef.Message oldItem = messageIdMappings.get(messageId);
                if (oldItem == null) return;

                final int index = messages.indexOf(oldItem);
                final DataDef.Message newItem = oldItem.clone();
                newItem.setAsRead();

                messages.set(index, newItem);
                messageIdMappings.put(messageId, newItem);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }

        onMessagesChange();
        save();
    }

    public void markAllAsRead() {
        Log.d(TAG, "setAllAsRead()");

        synchronized (this) {
            for (int i = 0; i < messages.size(); ++i) {
                final DataDef.Message message = messages.get(i);

                try {
                    final DataDef.Message newItem = message.clone();
                    newItem.setAsRead();

                    messages.set(i, newItem);
                    messageIdMappings.put(message.getId(), newItem);
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
            }
        }

        onMessagesChange();
        save();
    }

    public DataDef.Message updateDeliveryInfo(String messageId, long mAlimtalkSendTime, long mMunjaSendTime, int mMunjaType) {
        Log.d(TAG, "updateDeliveryInfo(" + messageId + ", mAlimtalkSendTime: " + mAlimtalkSendTime + ", mMunjaSendTime: " + mMunjaSendTime + ", mMunjaType: " + mMunjaType + ")");

        final DataDef.Message message = messageIdMappings.get(messageId);
        if (message == null) return null;

        final int index = messages.indexOf(message);

        if (mAlimtalkSendTime > 0) {
            message.setAlimTalkSentAt(new Date(mAlimtalkSendTime));
        }

        if (mMunjaSendTime > 0) {
            message.setMunjaSentAt(new Date(mMunjaSendTime));
        }

        DataDef.MunjaType munjaType = DataDef.MunjaType.valueOf(mMunjaType);

        if (munjaType != null) {
            message.setMunjaType(munjaType);
        }

        messages.set(index, message);

        save();
        return message;
    }

    public synchronized void save() {
        Log.d(TAG, "save()");
        setValue(messages.toArray(new DataDef.Message[0]));
    }

    @Override
    public boolean delete() {
        instance = null;
        return super.delete();
    }

    // Private members
    private void loadRecentMessages() {
        final DataDef.Message[] array = getValue();
        if (array == null) return;

        final Date now = new Date();
        final int retainDay = Config.getInstance().getUmsMsgRetainDay();

        for (final DataDef.Message message : array) {
            long milliseconds = now.getTime() - message.getSentAt().getTime();
            long days = TimeUnit.DAYS.convert(milliseconds, TimeUnit.MILLISECONDS);


            if (days < retainDay) {
                messages.add(message);
                messageIdMappings.put(message.getId(), message);
            }
        }

        messages.sort((m1, m2) -> m2.getSentAt().compareTo(m1.getSentAt()));

        if (array.length != messages.size()) {
            save();
        }
    }

    private synchronized void onMessagesChange() {
        if (onMessagesChangeListener != null) {
            onMessagesChangeListener.onMessagesChange(messages);
        }
    }
}
