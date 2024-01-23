package com.rationalowl.hello

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import com.google.firebase.FirebaseApp
import com.rationalowl.minerva.client.android.MinervaManager
import com.rationalowl.minerva.client.android.util.Logger

class MyApp : Application() {
    override fun onCreate() {
        Logger.debug(TAG, "onCreate enter")
        super.onCreate()
        val context = applicationContext

        // initialize fcm
        FirebaseApp.initializeApp(context)

        // initialize rational owl
        MinervaManager.init(context)

        // set rationalowl message listener
        val minMgr = MinervaManager.getInstance()
        minMgr.setMsgListener(RoMessageListener())

        // register sample channel
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(
            "sampleChannelId",
            "sampleChannel",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)
    }

    companion object {
        private const val TAG = "MyApp"
    }
}