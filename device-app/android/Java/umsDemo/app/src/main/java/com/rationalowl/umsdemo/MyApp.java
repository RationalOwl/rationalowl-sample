package com.rationalowl.umsdemo;

import android.app.Application;
import android.content.Context;

import com.google.firebase.FirebaseApp;
import com.rationalowl.minerva.client.android.MinervaManager;
import com.rationalowl.minerva.client.android.util.Logger;
import com.rationalowl.umsdemo.data.datasource.LocalDataSourceManager;

import java.lang.reflect.Field;

public class MyApp extends Application {
    public MyApp() {
        enableMinervaLog();
    }

    public void onCreate() {
        super.onCreate();

        LocalDataSourceManager.setDirectory(getFilesDir());
        initializeMinerva();
    }

    private void enableMinervaLog() {
        try {
            final Field field = Logger.class.getDeclaredField("shouldLog");
            field.setAccessible(true);
            field.setBoolean(null, true);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void initializeMinerva() {
        final Context context = getApplicationContext();

        // initialize fcm
        FirebaseApp.initializeApp(context);

        // initialize rational owl
        MinervaManager.init(context);

        // set rationalowl msg listener
        final MinervaManager minMgr = MinervaManager.getInstance();
        minMgr.setMsgListener(new RoMessageListener());
    }
}
