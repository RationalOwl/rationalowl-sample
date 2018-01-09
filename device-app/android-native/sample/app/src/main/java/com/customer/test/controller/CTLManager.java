/* 
 * Copyright (c) 2010 BeonePlus. All rights reserved.
 * PROPRIETARY/CONFIDENTIAL
 * Use is subject to license term.
 */
 
/*===========================================================================
DESCRIPTION  
  This file presents Hardware control & Screen lock control interface.
  Controlled hardwares are Blue-tooth, camera, WIFI, USB, 3G, WIFI tethering,
  USB tethering and screen capture.  
===========================================================================*/


/*===========================================================================
SOURCE HISTORY

No       date                 who                          Descriptions
----------------------------------------------------------------------------
 1     2010/09/24         jungdo@beone.co.kr                  Created.                                                       
===========================================================================*/


package com.customer.test.controller;

import android.content.Context;

public class CTLManager {
    private static final String TAG = "CTLManager";
    //hardware control

    public static final int CTL_BT_IDX = 0;
    public static final int CTL_WIFI_IDX = 1;
    public static final int HW_CTL_CNT = 2;
    // lock control
    public static final int CTL_LOCK_IDX = HW_CTL_CNT;
    public static final int CTL_METHOD_CNT = CTL_LOCK_IDX + 1;

    public static String DEFAULT_LOCK_INFO;

    private static CTLManager instance;

    Context mContext;
    private HWController mBtCtl;
    private HWController mWifiCtl;
    private HWController mLockCtl;


    public static CTLManager getInstance(Context mContext) {
        if (instance == null) {
            instance = new CTLManager(mContext);
        }
        return instance;
    }


    private CTLManager(Context mContext) {
        this.mContext = mContext;
        DEFAULT_LOCK_INFO = "보안위반으로 폰 사용이 제한됩니다.";//mContext.getString(R.string.lock_info_str);      
    }


    public void startBt() {
        if(mBtCtl== null) {
            mBtCtl = new BTController(mContext);
        }
        mBtCtl.startCtl();
    }


    public void stopBt() {
        if(mBtCtl != null) {
            mBtCtl.stopCtl();
            mBtCtl = null;
        }
    }


    public void startWifi() {
        if(mWifiCtl== null) {
            mWifiCtl = new WIFIController(mContext);
        }
        mWifiCtl.startCtl();
    }


    public void stopWifi() {
        if(mWifiCtl != null) {
            mWifiCtl.stopCtl();
            mWifiCtl = null;
        }
    }


    public void startLock() {
        if(mLockCtl== null) {
            mLockCtl = new  ScreenLockController(mContext);
        }
        mLockCtl.startCtl();
    }


    public void stopLock() {
        if(mLockCtl != null) {
            mLockCtl.stopCtl();
            mLockCtl = null;
        }
    }


    public static class LockInfoData {
        public String mLockInfo;
        public String mDepartment;
        public String mTel;


        public LockInfoData(Context context) {
            mLockInfo = "보안위반으로 폰 사용이 제한됩니다.";
            mDepartment = "보안부서";
            mTel = "02-7889-1010";
        }
    }
}