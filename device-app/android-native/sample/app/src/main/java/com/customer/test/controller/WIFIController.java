/* 
 * Copyright (c) 2010 BeonePlus. All rights reserved.
 * PROPRIETARY/CONFIDENTIAL
 * Use is subject to license term.
 */
 
/*===========================================================================
DESCRIPTION  
  This file presents WIFI control interface. And this class is controlled by
  CTLManager.  
===========================================================================*/


/*===========================================================================
SOURCE HISTORY

No       date                 who                          Descriptions
----------------------------------------------------------------------------
 1     2010/09/24         jungdo@beone.co.kr                  Created.                                                       
===========================================================================*/


package com.customer.test.controller;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.customer.test.Service1App;

class WIFIController extends HWController {
    private static final String THREAD_NAME = "WifiThread";          
    
    private WifiManager mWifiManager;
    
    
    public WIFIController(Context _context) {
        super(THREAD_NAME);       
        mWifiManager = (WifiManager)_context.getSystemService(Context.WIFI_SERVICE);        
    }
    
    
    public static boolean isUsing() {
        Context context = Service1App.getContext();
        WifiManager mWifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);    
        return mWifiManager.isWifiEnabled();
    }
    
    
    public void run() {
        Log.d(THREAD_NAME, "run start");        
                 
        while(isInterrupted() == false) {
            try {               
                if(mWifiManager.isWifiEnabled() == true) {
                    mWifiManager.setWifiEnabled(false);                    
                }
                Thread.sleep(1000);
                Log.d(THREAD_NAME, "running");
            }
            catch(InterruptedException ex) {                
                break;
            }                       
        }      
        //mWifiManager.setWifiEnabled(true);
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
