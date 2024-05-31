package com.rationalowl.hello;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;

import com.google.firebase.FirebaseApp;
import com.rationalowl.minerva.client.android.MinervaManager;
import com.rationalowl.minerva.client.android.util.Logger;

public class MyApp extends Application {
    private static final String TAG = "MyApp";


    public void onCreate(){
        Logger.debug(TAG, "onCreate enter");
        super.onCreate();
        Context context = getApplicationContext();

        // initialize fcm
        FirebaseApp.initializeApp(context);

        // initialize rational owl
        MinervaManager.init(context);

        // set rationalowl message listener
        MinervaManager minMgr = MinervaManager.getInstance();
        minMgr.setMsgListener(new RoMessageListener());

        // register sample channel
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel("sampleChannelId","sample fcm Channel", NotificationManager.IMPORTANCE_DEFAULT);
        notificationManager.createNotificationChannel(channel);
    }
}
