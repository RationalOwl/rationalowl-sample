package com.customer.test;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.rationalowl.minerva.client.android.MinervaManager;


public class Service1App extends Application {
    private static final String TAG = "MyApp";


    public void onCreate(){
        Log.d(TAG, "onCreate enter");
        super.onCreate();
        Context context = getApplicationContext();
        MinervaManager.init(context);
    }

}