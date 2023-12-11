package com.rationalowl.umsdemo;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.rationalowl.minerva.client.android.MinervaManager;
import com.rationalowl.minerva.client.android.util.Logger;
import com.rationalowl.umsdemo.domain.Message;
import com.rationalowl.umsdemo.domain.MessageType;
import com.rationalowl.umsdemo.domain.MessagesRepository;
import com.rationalowl.umsdemo.representation.ui.message.MessageViewActivity;

import java.util.Map;

public class NotificationService extends FirebaseMessagingService {
    private static final String TAG = "NotificationService";

    private final MinervaManager manager = MinervaManager.getInstance();
    private final MessagesRepository repository = MessagesRepository.getInstance();

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(@NonNull String token) {
        Logger.debug(TAG, "onNewToken token: " + token);
        // just call setDeviceToken() API

        manager.setDeviceToken(token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Logger.debug(TAG, "onMessageReceived enter");
        final Map<String, String> data = remoteMessage.getData();

        // set notification  delivery tracking
        manager.enableNotificationTracking(data);

        // silent push received.
        if (data.containsKey("silent")) {
            // system push is sent by RationalOwl for device app lifecycle check.
            // system push is also silent push.
            // if system push has received, just return.
            if (data.containsKey("SystemPush")) {
                Logger.debug(TAG, "System push received!");
                return;
            }
            // normal silent push which are sent by your app server.
            // do your logic
            else {
                Logger.debug(TAG, "your app server sent silent push");
                // do your logic
            }
        }
        // it is normal custom push not silent push.
        // do your logic here
        else {
            // make your custom notification UI
            handleMessage(data);
        }
    }

    public void handleMessage(Map<String, String> data) {
        if (data.size() == 0) return;

        // ro_todo_1: data {"mId":"dkdk","type":1,"title":"dkd","body":"dkdk","st":"11111","ii":"imgId"}
        // 메시지에 send time(st) imageId(ii) 메시지 모델 저장.

        //public static String APP_PUSH_MSG_ID_KEY = "mId";
        //public static String APP_PUSH_TYPE_KEY = "type"; 1(normal), 2(urgent)
        //public static String APP_PUSH_TITLE_KEY = "title";
        //public static String APP_PUSH_BODY_KEY = "body";
        //public static String APP_SEND_TIME_KEY = "st";
        //public static String APP_IMG_ID_KEY = "ii";
        final Message message = new Message(data);
        if (repository.hasMessage(message.getId())) return;

        MessagesRepository.getInstance().addMessage(message);
        showNotification(message);
    }

    public static void showNotification(Message message) {
        final Context context = MinervaManager.getContext();

        final Intent intent = new Intent(context, MessageViewActivity.class);
        intent.putExtra(MessageViewActivity.BUNDLE_MESSAGE_KEY, message);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        final PendingIntent pendingIntent = PendingIntent.getActivity(context, message.hashCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        final int iconId = message.getType() == MessageType.EMERGENCY ? R.drawable.notification_orange : R.drawable.notification_green;
        final Bitmap iconBitmap = BitmapFactory.decodeResource(context.getResources(), iconId);

        final int color = ContextCompat.getColor(context, message.getType() == MessageType.EMERGENCY ? R.color.emergency : R.color.notice);
        final String channelId = context.getString(R.string.notification_channel_id);
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_stat_notification)
                .setLargeIcon(iconBitmap)
                .setColor(color)
                .setContentTitle(message.getTitle())
                .setContentText(message.getBody())
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        final NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        manager.notify(message.hashCode(), builder.build());
    }
}
