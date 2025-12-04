package com.rationalowl.umsdemo;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.rationalowl.minerva.client.android.MinervaManager;

import java.util.Map;

public class NotificationService extends FirebaseMessagingService {
    private static final String TAG = "NotificationService";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(@NonNull String token) {
        Log.d(TAG, "onNewToken(" + token + ")");

        // just call setDeviceToken() API
        final MinervaManager minMgr = MinervaManager.getInstance();
        minMgr.setDeviceToken(token);

        // there's no need to do anything else
    }

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "onMessageReceived enter");
        Context context = MinervaManager.getContext();
        boolean pushEnable = NotificationManagerCompat.from(context).areNotificationsEnabled();

        // push disabled by settings.
        if(!pushEnable) {
            Log.e(TAG, "Push blocked by setting!!!");
        }
        else {
            Map<String, String> data = remoteMessage.getData();

            // set notification  delivery tracking
            MinervaManager minMgr = MinervaManager.getInstance();
            minMgr.enableNotificationTracking(data);

            // make your custom notification UI
            showCustomNotification(data);
        }
    }

    public void handleMessage(Map<String, String> data) {
        if (data.isEmpty()) return;

        // ro_todo_1: data {"mId":"dkdk","type":1,"title":"dkd","body":"dkdk","st":"11111","ii":"imgId"}
        // 메시지에 send time(st) imageId(ii) 메시지 모델 저장.

        //public static String APP_PUSH_MSG_ID_KEY = "mId";
        //public static String APP_PUSH_TYPE_KEY = "type"; 1(normal), 2(urgent)
        //public static String APP_PUSH_TITLE_KEY = "title";
        //public static String APP_PUSH_BODY_KEY = "body";
        //public static String APP_SEND_TIME_KEY = "st";
        //public static String APP_IMG_ID_KEY = "ii";
        final DataDef.Message message = new DataDef.Message(data);
        if (MessageLocalDataSource.getInstance().containsMessage(message.getId())) return;

        MessageLocalDataSource.getInstance().addMessage(message);
        showNotification(message);
    }

    public static void showNotification(DataDef.Message message) {
        final Context context = MinervaManager.getContext();

        final Intent intent = new Intent(context, MessageReadActivity.class);
        intent.putExtra(MessageReadActivity.BUNDLE_MESSAGE_KEY, message);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        final PendingIntent pendingIntent = PendingIntent.getActivity(context, message.hashCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        final int iconId = message.getType() == DataDef.MessageType.EMERGENCY ? R.drawable.notification_orange : R.drawable.notification_green;
        final Bitmap iconBitmap = BitmapFactory.decodeResource(context.getResources(), iconId);

        final int color = ContextCompat.getColor(context, message.getType() == DataDef.MessageType.EMERGENCY ? R.color.emergency : R.color.notice);
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
