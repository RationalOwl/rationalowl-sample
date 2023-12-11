package com.rationalowl.umsdemo;

import android.app.Application;
import android.content.Context;

import com.google.firebase.FirebaseApp;
import com.rationalowl.minerva.client.android.MinervaManager;
import com.rationalowl.minerva.client.android.util.Logger;
import com.rationalowl.umsdemo.domain.RepositoryManager;

import java.lang.reflect.Field;

public class MyApp extends Application {
    private static final String TAG = "MyApp";

    public MyApp() {
        try {
            Field field = Logger.class.getDeclaredField("shouldLog");
            field.setAccessible(true);
            field.setBoolean(null, true);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void onCreate() {
        Logger.debug(TAG, "onCreate enter");
        super.onCreate();

        // 디렉토리 정보 초기 설정
        RepositoryManager.setDirectory(getFilesDir());

        final Context context = getApplicationContext();
        FirebaseApp.initializeApp(context);
        MinervaManager.init(context);

        // set rationalowl msg listener
        final MinervaManager mgr = MinervaManager.getInstance();
        mgr.setMsgListener(new RoMessageListener());
    }
}
