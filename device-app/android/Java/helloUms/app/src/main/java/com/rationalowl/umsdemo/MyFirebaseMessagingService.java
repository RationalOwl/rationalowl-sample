package com.rationalowl.umsdemo;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.rationalowl.umsdemo.R;
import com.rationalowl.minerva.client.android.MinervaManager;
import com.rationalowl.minerva.client.android.util.Logger;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";


    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(String token) {
        Logger.debug(TAG, "onNewToken token: " + token);
        // just call setDeviceToken() API
        MinervaManager minMgr = MinervaManager.getInstance();
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
        Logger.debug(TAG, "onMessageReceived enter");
        Map<String, String> data = remoteMessage.getData();

        // set notification  delivery tracking
        MinervaManager minMgr = MinervaManager.getInstance();
        minMgr.enableNotificationTracking(data);

        // silent push received.
        if(data.containsKey("silent")) {
            // system push is sent by RationalOwl for device app lifecycle check.
            // system push is also silent push.
            // if system push has received, just return.
            if(data.containsKey("SystemPush")) {
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
            showCustomNotification(data);
        }
    }


    public static void showCustomNotification(Map<String, String> data) {
        // hello app custom data format can be any fields you want
        // this sample app assume below fields
        /*
        {
          "mId": "message id here",
          "title": "message title here", // (optional):
          "body": "message body here",
          "st": "(message) send time",
          "ii": "image id here"         // (optional) : 이미지 첨부시 이미지 아이디 세팅
        }
        */

        String msgId = null, body = null, title = null, imageId = null;
        // mandatory fields
        msgId = data.get("mId");
        body = data.get("body");

        // optional fields
        if(data.containsKey("title")) {
            title = data.get("title");
        }
        if(data.containsKey("ii")) {
            imageId = data.get("ii");
        }

        final Context context = MinervaManager.getContext();

        final Intent intent = new Intent(context, MainActivity.class);
        String[] extra = new String[4];
        extra[0] = msgId;
        extra[1] = body;
        extra[2] = title;
        extra[3] = imageId;
        intent.putExtra(MainActivity.MESSAGE_FROM_FIREBASE_SERVICE_KEY, extra);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        final PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        final int iconId = R.drawable.icon;
        final Bitmap iconBitmap = BitmapFactory.decodeResource(context.getResources(), iconId);

        //final String channelId = context.getString(R.string.notification_channel_id);
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "sampleChannelId")
                .setSmallIcon(R.drawable.icon)
                .setLargeIcon(iconBitmap)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        final NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }
}

