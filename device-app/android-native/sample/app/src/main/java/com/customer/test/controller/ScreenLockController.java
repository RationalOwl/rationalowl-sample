/* 
 * Copyright (c) 2010 BeonePlus. All rights reserved.
 * PROPRIETARY/CONFIDENTIAL
 * Use is subject to license term.
 */
 
/*===========================================================================
DESCRIPTION  
  This file presents Screen lock control interface. And this class is controlled by
  CTLManager.  
===========================================================================*/


/*===========================================================================
SOURCE HISTORY

No       date                 who                          Descriptions
----------------------------------------------------------------------------
 1     2010/09/24         jungdo@beone.co.kr                  Created.                                                       
===========================================================================*/


package com.customer.test.controller;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.customer.test.data.MsgCache;

import java.util.List;


public class ScreenLockController extends HWController {
    private static final String THREAD_NAME = "LockThread";
    private static final String LOCK_SCREEN_NAME = "com.customer.test.controller.LockScreen";

    private Context mContext;

    private static int lockState = LockScreen.VAL_UNLOCK;


    public ScreenLockController(Context context) {
        super(THREAD_NAME);
        mContext = context;
    }



    public void run() {
        Log.d(THREAD_NAME, "run start");

        try {
            while(isInterrupted() == false) {

                String topActivity = getTopActivity();

                //if currently lock screen is displayed, 
                //lockRun doesn't need to run.
                if(topActivity.equals(LOCK_SCREEN_NAME) == true) {
                    Thread.sleep(10);
                }
                else {
                    Intent intent = new Intent(mContext, LockScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    lockState = LockScreen.VAL_LOCK;
                    intent.putExtra(LockScreen.REQ_LOCK, lockState);
                    mContext.startActivity(intent);
                }
            }
        }
        catch(InterruptedException ex) {
            Log.d(THREAD_NAME, "bye");
        }
        //This routine is called only unlock request is called.
        Intent intent = new Intent(mContext, LockScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        lockState = LockScreen.VAL_UNLOCK;
        intent.putExtra(LockScreen.REQ_LOCK, lockState);
        mContext.startActivity(intent);
    }



    private String getTopActivity() {
        ActivityManager am = (ActivityManager)mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> rtiList = am.getRunningTasks(1);
        ActivityManager.RunningTaskInfo rti = rtiList.get(0);
        ComponentName cn = rti.topActivity;
        String className = cn.getClassName();
        return className;
    }


    public void startCtl() {
        String lock = "on";
        MsgCache cache = MsgCache.getInstance();
        cache.setScreenLockData(lock);
        this.start();
    }


    public void stopCtl() {
        String lock = "off";
        MsgCache cache = MsgCache.getInstance();
        cache.setScreenLockData(lock);
        this.interrupt();
    }


    public boolean isControlled() {
        return isAlive();
    }
}