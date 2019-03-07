package com.customer.test;

import android.app.Application;
import android.content.Context;
import android.util.Log;

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
    }
}