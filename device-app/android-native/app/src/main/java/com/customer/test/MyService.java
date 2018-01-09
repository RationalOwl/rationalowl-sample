/* 
 * Copyright (c) 2010 RationalOwl. All rights reserved.
 * PROPRIETARY/CONFIDENTIAL
 * Use is subject to license term.
 */

/*===========================================================================
 DESCRIPTION  
 DevicePushService makes IntentMessage and send it to the IntentHandler to proceed.         
 ===========================================================================*/


/*===========================================================================
 SOURCE HISTORY

 No       date                 who                          Descriptions
 ----------------------------------------------------------------------------
 1     2013/09/24         jungdo@rationalowl.com                  Created.                                                       
 ===========================================================================*/


package com.customer.test;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

import com.customer.test.data.MsgCache;
import com.customer.test.data.MsgCache.PushMsg;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rationalowl.minerva.client.android.MinervaManager;
import com.rationalowl.minerva.client.android.data.MinervaDataManager;
import com.rationalowl.minerva.client.android.util.Logger;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;


public class MyService extends Service {
    private static final String TAG = "MinervaService";

    // push service type
    public static final String SERVICE_TYPE = "SERVICE_TYPE";

    public static final int SERVICE_TYPE_PUSH_MSG_RECEIVED = 1;
    
    public static final int SERVICE_TYPE_P2P_MSG_RECEIVED = 2;


    public static final String SERVICE_ARGV = "PUSH_DATA";

    private static ObjectMapper mapper = new ObjectMapper();


    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public void onCreate() {
        Logger.debug(TAG, "onCreate enter");
        super.onCreate();

        // IntentFilter filter = new IntentFilter();
        // filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        // registerReceiver(mIntentReceiver, filter);
    }


    @Override
    public void onDestroy() {
        Logger.debug(TAG, "onDestroy enter");
        super.onDestroy();
        // release application related data.
        MinervaDataManager appDataManager = MinervaDataManager.getInstance();
        appDataManager.release();
        // jungdo_todo: release any other resources later.
    }


    @SuppressWarnings("deprecation")
    @Override
    public void onStart(Intent intent, int startid) {
        Logger.debug(TAG, "onStart enter");
        super.onStart(intent, startid);

        if (intent == null) {
            Logger.error(TAG, "intent is null");
            return;
        }
        Bundle extras = intent.getExtras();
        int serviceType = extras.getInt(SERVICE_TYPE);

        switch (serviceType) {

            case SERVICE_TYPE_PUSH_MSG_RECEIVED: {
                Logger.debug(TAG, "onStart SERVICE_TYPE_MSG_RECEIVED");
                Object[] argv = (Object[]) extras.get(SERVICE_ARGV);
                // currently, don't user package, but remain for future
                // extension.
                int msgSize = (Integer) argv[0];
                String jsonStr = (String) argv[1];

                try {
                    ArrayList<Map<String, Object>> testJson = mapper.readValue(jsonStr, new TypeReference<ArrayList<Map<String, Object>>>() {
                    });
                    
                    Map<String, Object> oneMsg = null;    
                    String data = null, notiTitle = null, notiBody = null;
                    long serverTime;
                    long curTime = System.currentTimeMillis();
                    long elapseTime;
                    Calendar cal = Calendar.getInstance();
                    String curTimeStr = cal.get(Calendar.YEAR) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND);                   
                    String serverTimeStr = null;
                    MsgCache cache = MsgCache.getInstance();
                    PushMsg pushMsg = new PushMsg();
                    
                    for (int i = 0; i < msgSize; i++) {
                        oneMsg = testJson.get(i);
                        data = (String) oneMsg.get(MinervaManager.FIELD_MSG_DATA);
                        serverTime = (Long) oneMsg.get(MinervaManager.FIELD_MSG_SERVER_TIME);

                        // optional fields
                        notiTitle = (String) oneMsg.get(MinervaManager.FIELD_MSG_NOTI_TITLE);
                        notiBody = (String) oneMsg.get(MinervaManager.FIELD_MSG_NOTI_BODY);

                        curTime = System.currentTimeMillis();
                        elapseTime = curTime - serverTime;
                        cal.setTimeInMillis(serverTime);
                        serverTimeStr = cal.get(Calendar.YEAR) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND);

                        pushMsg = new PushMsg();
                        pushMsg.mData = data;
                        pushMsg.mSrcTime = serverTimeStr;
                        pushMsg.mDestTime = curTimeStr;
                        pushMsg.mElapsedTime = elapseTime;
                        cache.addMsg(pushMsg);

                        if (getTopActivity().equals("com.customer.test.MsgActivity") == true) {
                            Intent intent2 = new Intent("demo");

                            if (data != null) {
                                intent.putExtra("msg", data);
                                LocalBroadcastManager.getInstance(this).sendBroadcast(intent2);
                            }
                        }
                        else {
                            String notiTitleStr = notiTitle == null ? "msg" : notiTitle;
                            String notiBodyStr = notiBody == null ? data : notiBody;

                            NotificationManager notiMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            Intent intent1 = new Intent(this, MsgActivity.class);
                            PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent1, PendingIntent.FLAG_CANCEL_CURRENT);

                            Notification.Builder builder = new Notification.Builder(this)
                                    .setContentIntent(contentIntent)
                                    .setSmallIcon(R.drawable.icon)
                                    .setContentTitle(notiTitleStr)
                                    .setContentText(notiBodyStr)
                                    .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                                    .setTicker("downstream msg!!");
                            Notification notification = builder.build();
                            notiMgr.notify(1234, notification);



                        }
                    }
                    System.out.println();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            case SERVICE_TYPE_P2P_MSG_RECEIVED: {
                Logger.debug(TAG, "onStart SERVICE_TYPE_P2P_MSG_RECEIVED");
                Object[] argv = (Object[]) extras.get(SERVICE_ARGV);
                // currently, don't user package, but remain for future
                // extension.
                int msgSize = (Integer) argv[0];
                String jsonStr = (String) argv[1];

                try {
                    ArrayList<Map<String, Object>> testJson = mapper.readValue(jsonStr, new TypeReference<ArrayList<Map<String, Object>>>() {
                    });

                    
                    Map<String, Object> oneMsg = null;       
                    String sender = null;
                    String data = null, notiTitle = null, notiBody = null;
                    long serverTime;
                    long curTime = System.currentTimeMillis();
                    long elapseTime;
                    Calendar cal = Calendar.getInstance();
                    String curTimeStr = cal.get(Calendar.YEAR) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND);                   
                    String serverTimeStr = null;       
                    MsgCache cache = MsgCache.getInstance();
                    PushMsg pushMsg = new PushMsg();
                    
                    for (int i = 0; i < msgSize; i++) {
                        oneMsg = testJson.get(i);       
                        sender = (String) oneMsg.get(MinervaManager.FIELD_MSG_SENDER);
                        data = (String) oneMsg.get(MinervaManager.FIELD_MSG_DATA);
                        serverTime = (Long) oneMsg.get(MinervaManager.FIELD_MSG_SERVER_TIME);

                        // optional fields
                        notiTitle = (String) oneMsg.get(MinervaManager.FIELD_MSG_NOTI_TITLE);
                        notiBody = (String) oneMsg.get(MinervaManager.FIELD_MSG_NOTI_BODY);

                        curTime = System.currentTimeMillis();
                        elapseTime = curTime - serverTime;
                        cal.setTimeInMillis(serverTime);
                        serverTimeStr = cal.get(Calendar.YEAR) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND);

                        pushMsg = new PushMsg();
                        pushMsg.mData = data;
                        pushMsg.mSrcTime = serverTimeStr;
                        pushMsg.mDestTime = curTimeStr;
                        pushMsg.mElapsedTime = elapseTime;
                        cache.addMsg(pushMsg);

                        // send data directly.
                        if (getTopActivity().equals("com.customer.test.MsgActivity") == true) {
                            Intent intent2 = new Intent("demo");

                            if (data != null) {
                                intent.putExtra("msg", data);
                                LocalBroadcastManager.getInstance(this).sendBroadcast(intent2);
                            }
                        }
                        // show notification title and body
                        else {
                            String notiTitleStr = notiTitle == null ? "p2p msg" : notiTitle;
                            String notiBodyStr = notiBody == null ? data : notiBody;
                            NotificationManager notiMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            Intent intent1 = new Intent(this, MsgActivity.class);
                            PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent1, PendingIntent.FLAG_CANCEL_CURRENT);

                            Notification.Builder builder = new Notification.Builder(this)
                                    .setContentIntent(contentIntent)
                                    .setSmallIcon(R.drawable.icon)
                                    .setContentTitle(notiTitleStr)
                                    .setContentText(notiBodyStr)
                                    .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                                    .setTicker("p2p msg!!");
                            Notification notification = builder.build();
                            notiMgr.notify(1234, notification);

                        }
                    }
                    System.out.println();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            default:
                break;
        }
    }


    private String getTopActivity() {
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> rtiList = am.getRunningTasks(1);
        ActivityManager.RunningTaskInfo rti = rtiList.get(0);
        ComponentName cn = rti.topActivity;
        String className = cn.getClassName();
        return className;
    }


    private boolean isForeground(String PackageName) {
        // Get the Activity Manager
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

        // Get a list of running tasks, we are only interested in the last one,
        // the top most so we give a 1 as parameter so we only get the topmost.
        List<ActivityManager.RunningTaskInfo> task = manager.getRunningTasks(1);

        // Get the info we need for comparison.
        ComponentName componentInfo = task.get(0).topActivity;

        // Check if it matches our package name.
        String pkg = componentInfo.getPackageName();

        if (pkg.equals(PackageName))
            return true;

        // If not then our app is not on the foreground.
        return false;
    }

}