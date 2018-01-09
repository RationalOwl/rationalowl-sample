package com.customer.test;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.rationalowl.minerva.client.android.MinervaManager;


public class Service1App extends Application {
    private static final String TAG = "MyApp";

    private static Context context;
    

    public void onCreate(){
        Log.d(TAG, "onCreate enter");
        super.onCreate();
        context = getApplicationContext();
        MinervaManager.init(context);
    }


    public static Context getContext() {
        return context;
    }
}