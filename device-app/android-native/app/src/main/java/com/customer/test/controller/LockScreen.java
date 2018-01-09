/* 
 * Copyright (c) 2010 BeonePlus. All rights reserved.
 * PROPRIETARY/CONFIDENTIAL
 * Use is subject to license term.
 */
 
/*===========================================================================
DESCRIPTION  
  It's screen lock which is controlled by ScreenLockCtlThread.
  When this screen occupies full screen, User can't interface with hand-set 
  device.
===========================================================================*/


/*===========================================================================
SOURCE HISTORY

No       date                 who                          Descriptions
----------------------------------------------------------------------------
 1      2010/09/24         jungdo@beone.co.kr              Created.                                                       
===========================================================================*/

package com.customer.test.controller;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.customer.test.R;
import com.customer.test.controller.CTLManager.LockInfoData;
import com.customer.test.data.MsgCache;

public class LockScreen extends Activity {
    private static final String TAG = "LockScreen";

    public static final String REQ_LOCK = "REQ_LOCK";
    public static final int VAL_UNLOCK = 0;
    public static final int VAL_LOCK = 1;

    // For safe event handling.
    private boolean isAlive = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lock);
        initView();
        isAlive = true;
    }


    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy() enter");
        isAlive = false;
        super.onDestroy();
    }


    @Override
    protected void onStart() {
        Log.d(TAG, "onStart() enter");
        super.onStart();
    }



    private void initView() {
        TextView tv = (TextView) findViewById(R.id.lockInfo);
        LockInfoData lid = new LockInfoData(this);
        String lockInfo = lid.mLockInfo;
        tv.setText(lockInfo);

        //jungdo_temp for hyundai      
        TextView tv2 = (TextView) findViewById(R.id.tel);
        String tel = lid.mTel;
        tv2.setText(tel);
    }



    @Override
    protected void onResume() {
        Log.d(TAG, "onResume() enter");
        super.onResume();

        MsgCache dataCache = MsgCache.getInstance();
        String val = dataCache.getScreenLockData();


        if (val.equals("off")) {
            Log.d(TAG, "onResume() 1");
            finish();
        }
        else {
            //do nothing.
        }
    }
}