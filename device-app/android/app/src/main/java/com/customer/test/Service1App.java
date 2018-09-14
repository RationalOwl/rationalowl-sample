package com.customer.test;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.rationalowl.minerva.client.android.MinervaManager;
import com.rationalowl.minerva.client.android.util.Logger;


public class Service1App extends Application {
    private static final String TAG = "MyApp";


    public void onCreate(){
        Logger.debug(TAG, "onCreate enter");
        super.onCreate();
        Context context = getApplicationContext();
        MinervaManager.init(context);
    }

}