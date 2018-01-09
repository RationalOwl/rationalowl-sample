/*
 * Copyright (c) 2010 BeonePlus. All rights reserved.
 * PROPRIETARY/CONFIDENTIAL
 * Use is subject to license term.
 */

/*===========================================================================
DESCRIPTION
  This file presents BT control interface. And this class is controlled by
  CTLManager.
===========================================================================*/


/*===========================================================================
SOURCE HISTORY

No       date                 who                          Descriptions
----------------------------------------------------------------------------
 1     2010/09/24         jungdo@beone.co.kr                  Created.
===========================================================================*/



package com.customer.test.controller;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.os.Looper;
import android.util.Log;

class BTController extends HWController {
    private static final String THREAD_NAME = "BTThread";

    BluetoothAdapter mBluetoothAdapter;
    boolean isSupportBT;

    public BTController(Context context) {
        super(THREAD_NAME);
    }


    public void run() {
        Log.d(THREAD_NAME, "run start");

        Looper.prepare();
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        //if this hand set doesn't support BT don't need to run BT thread.
        if(mBluetoothAdapter == null) {
            Looper.myLooper().quit();
            return;
        }
        try {
            while(isInterrupted() == false) {
                Thread.sleep(1000);
                checkBT();
                //Log.d(THREAD_NAME, "running");
            }
            //Looper.loop();
        }
        catch(InterruptedException ex) {
        }
        Looper.myLooper().quit();
    }


    public static boolean isUsing() {
        boolean ret = false;
        Looper looper = Looper.myLooper();

        if(looper == null) {
            Looper.prepare();
        }
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        //if this hand set doesn't support BT don't need to run BT thread.
        if(bluetoothAdapter == null) {
            Looper.myLooper().quit();
            return false;
        }
        int btState = bluetoothAdapter.getState();
        Looper.myLooper().quit();

        if(btState == BluetoothAdapter.STATE_ON || btState == BluetoothAdapter.STATE_TURNING_ON) {
            ret = true;
        }
        return ret;
    }


    public boolean isUsing_() {
        boolean ret = false;
        int btState = mBluetoothAdapter.getState();

        if(btState == BluetoothAdapter.STATE_ON || btState == BluetoothAdapter.STATE_TURNING_ON) {
            ret = true;
        }
        return ret;
    }


    private void checkBT() {
        int btState = mBluetoothAdapter.getState();

        if(btState == BluetoothAdapter.STATE_ON || btState == BluetoothAdapter.STATE_TURNING_ON) {
            mBluetoothAdapter.disable();
        }
    }


    public void startCtl() {
        this.start();
    }


    public void stopCtl() {
        this.interrupt();
    }


    public boolean isControlled() {
        return isAlive();
    }
}
