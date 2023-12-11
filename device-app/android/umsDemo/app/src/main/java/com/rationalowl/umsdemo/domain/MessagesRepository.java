package com.rationalowl.umsdemo.domain;

import android.util.Log;

import com.jakewharton.rxrelay3.BehaviorRelay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class MessagesRepository extends JsonRepository<Message[]> {
    private static final String TAG = "MessagesRepository";
    private static final String FILE_NAME = "messages.json";

    private static MessagesRepository instance;

    public static synchronized MessagesRepository getInstance() {
        if (instance == null) {
            instance = new MessagesRepository();
        }

        return instance;
    }

    private final BehaviorRelay<List<Message>> messages;
    private final Map<String, Message> messageIdMappings = new HashMap<>();

    protected MessagesRepository() {
        super(Message[].class, FILE_NAME);

        final Message[] array = getValue();
        final List<Message> list = new ArrayList<>();

        if (array != null) {
            final Date now = new Date();
            final int retainDay = Config.getInstance().getUmsMsgRetainDay();

            for (Message message : array) {
                long milliseconds = now.getTime() - message.getSentAt().getTime();
                long days = TimeUnit.DAYS.convert(milliseconds, TimeUnit.MILLISECONDS);

                if (days < retainDay) {
                    list.add(message);
                    messageIdMappings.put(message.getId(), message);
                }
            }

            list.sort((m1, m2) -> m2.getSentAt().compareTo(m1.getSentAt()));
        }

        messages = BehaviorRelay.createDefault(list);

        if (array != null && array.length != list.size()) {
            save();
        }
    }

    public void addMessage(Message item) {
        Log.d(TAG, "addMessage(" + item.getId() + ")");

        if (hasMessage(item.getId())) return;

        synchronized (this) {
            final List<Message> newItems = new ArrayList<>(messages.getValue());
            newItems.add(item);
            newItems.sort((m1, m2) -> m2.getSentAt().compareTo(m1.getSentAt()));
            messageIdMappings.put(item.getId(), item);

            messages.accept(newItems);
        }

        save();
    }

    public synchronized boolean hasMessage(String messageId) {
        return messageIdMappings.containsKey(messageId);
    }

    public synchronized Message getMessage(String messageId) {
        return messageIdMappings.get(messageId);
    }

    public BehaviorRelay<List<Message>> getMessages() {
        return messages;
    }

    public void removeMessage(String messageId) {
        Log.d(TAG, "removeMessage(" + messageId + ")");

        synchronized (this) {
            final List<Message> newItems = new ArrayList<>(messages.getValue());
            newItems.removeIf(message -> message.getId().equals(messageId));
            messageIdMappings.remove(messageId);

            messages.accept(newItems);
        }

        save();
    }

    public void removeMessages(String[] messageIds) {
        Log.d(TAG, "removeMessage([" + String.join(", ", messageIds) + "])");

        synchronized (this) {
            final List<Message> newItems = new ArrayList<>(messages.getValue());
            final Set<String> messageIdSet = new HashSet<>(Arrays.asList(messageIds));

            newItems.removeIf(message -> messageIdSet.contains(message.getId()));

            for (String messageId : messageIds) {
                messageIdMappings.remove(messageId);
            }

            messages.accept(newItems);
        }

        save();
    }

    public void setAsRead(String messageId) {
        Log.d(TAG, "setAsRead(" + messageId + ")");

        synchronized (this) {
            final List<Message> newItems = new ArrayList<>(messages.getValue());

            try {
                final Message oldItem = messageIdMappings.get(messageId);
                if (oldItem == null) return;

                final Message newItem = oldItem.clone();
                newItem.setAsRead();
                newItems.set(newItems.indexOf(oldItem), newItem);

                messageIdMappings.put(messageId, newItem);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }

            messages.accept(newItems);
        }

        save();
    }

    public void setAllAsRead() {
        Log.d(TAG, "setAllAsRead()");

        synchronized (this) {
            final List<Message> newItems = new ArrayList<>();

            for (Message item : messages.getValue()) {
                try {
                    final Message newItem = item.clone();
                    newItem.setAsRead();
                    newItems.add(newItem);

                    messageIdMappings.put(item.getId(), newItem);
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
            }

            messages.accept(newItems);
        }

        save();
    }

    public synchronized void save() {
        Log.d(TAG, "save()");
        setValue(messages.getValue().toArray(new Message[0]));
    }
}
