package com.rationalowl.hello

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.rationalowl.minerva.client.android.MinervaManager
import com.rationalowl.minerva.client.android.util.Logger

class MyFirebaseMessagingService : FirebaseMessagingService() {
    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    override fun onNewToken(token: String) {
        Logger.debug(TAG, "onNewToken token: $token")
        // just call setDeviceToken() API
        val minMgr = MinervaManager.getInstance()
        minMgr.setDeviceToken(token)

        // there's no need to do anything else
    }

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Logger.debug(TAG, "onMessageReceived enter")
        val data = remoteMessage.data

        // set notification  delivery tracking
        val minMgr = MinervaManager.getInstance()
        minMgr.enableNotificationTracking(data)

        // make your custom notification UI
        showCustomNotification(data)
    }

    companion object {
        private const val TAG = "MyFirebaseMsgService"
        fun showCustomNotification(data: Map<String, String>) {
            // hello app custom data format can be any fields you want
            // this sample app assume below fields
            /*
        {
          "mId": "message id here",
          "title": "message title here",
          "body": "message body here",
          "ii": "image id here"
          "st": "(message) send time"
        }
        */
            var msgId: String? = null
            var body: String? = null
            var title: String? = null
            var imageId: String? = null
            // mandatory fields
            msgId = data["mId"]
            body = data["body"]

            // optional fields
            if (data.containsKey("title")) {
                title = data["title"]
            }
            if (data.containsKey("ii")) {
                imageId = data["ii"]
            }
            val context = MinervaManager.getContext()
            val intent = Intent(context, MainActivity::class.java)
            val extra = arrayOfNulls<String>(4)
            extra[0] = msgId
            extra[1] = body
            extra[2] = title
            extra[3] = imageId
            intent.putExtra(MainActivity.Companion.MESSAGE_FROM_FIREBASE_SERVICE_KEY, extra)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            val pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            val iconId = R.drawable.icon
            val iconBitmap = BitmapFactory.decodeResource(context.resources, iconId)

            //final String channelId = context.getString(R.string.notification_channel_id);
            val builder = NotificationCompat.Builder(context, "sampleChannelId")
                .setSmallIcon(R.drawable.icon)
                .setLargeIcon(iconBitmap)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
            val manager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            manager.notify(0, builder.build())
        }
    }
}