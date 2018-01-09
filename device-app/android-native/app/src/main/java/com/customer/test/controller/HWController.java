/* 
 * Copyright (c) 2011 Duzon C&T. All rights reserved.
 * PROPRIETARY/CONFIDENTIAL
 * Use is subject to license term.
 */
 
/*===========================================================================
DESCRIPTION  
  Controllable is interface which present controllable and it's controlled by
  CTLManager.  
===========================================================================*/


/*===========================================================================
SOURCE HISTORY

No       date                 who                          Descriptions
----------------------------------------------------------------------------
 1     2011/02/10         jungdo@duzon.com                  Created.                                                       
===========================================================================*/

package com.customer.test.controller;

public abstract class HWController extends Thread {
    
    public HWController() {        
    }
    
    public HWController(String name) {
        super(name);
    }

    public abstract void startCtl();    
    
    public abstract void stopCtl();    
    
    public abstract boolean isControlled();
    
    public static boolean isUsing() {
        return false;
    }    
}
