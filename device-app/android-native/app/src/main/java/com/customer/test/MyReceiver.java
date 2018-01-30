package com.customer.test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.customer.test.data.MyData;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rationalowl.minerva.client.android.MinervaManager;
import com.rationalowl.minerva.client.android.Result;

public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "MyReceiver";   
    
    ObjectMapper mapper = new ObjectMapper();
    
    
    
    @SuppressWarnings("deprecation")
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d(TAG, "onReceive enter action = " + action);
        
        if(action.equals(MinervaManager.ACTION_MINERVA_DEVICE_REGISTER_RESULT)) {
            Log.d(TAG, "onReceive 1 " + action);       
            Bundle bundle = intent.getExtras();
            int resultCode = bundle.getInt("resultCode");    
            String msg = null;
            //yes registration has completed successfully!
            if(resultCode == Result.RESULT_OK) {
                String deviceRegId = bundle.getString("deviceRegId");
                MyData data = MyData.getInstance();
                data.setDeviceRegId(deviceRegId);
                msg = "registration success !!\n" + "registration id : " + deviceRegId;
            }
            else if(resultCode == Result.RESULT_DEVICE_ALREADY_REGISTERED) {
                String deviceRegId = bundle.getString("deviceRegId");
                MyData data = MyData.getInstance();
                data.setDeviceRegId(deviceRegId);
                msg = bundle.getString("resultMsg") + "!!\n" + "registration id : " + deviceRegId;
            }
            //registration error has occurred!
            else {
                msg = bundle.getString("resultMsg");
            }
            Log.d(TAG, msg);
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();           
        }                
        else if(action.equals(MinervaManager.ACTION_MINERVA_DEVICE_UNREGISTER_RESULT)) {
            Log.d(TAG, "onReceive 2 " + action);       
            Bundle bundle = intent.getExtras();
            int resultCode = bundle.getInt("resultCode");    
            String msg = null;
            //yes unregistration has completed successfully!
            if(resultCode == Result.RESULT_OK) {
                String deviceRegId = bundle.getString("deviceRegId");
                msg = "device un-registration success !!\n";
            }          
            //registration error has occurred!
            else {
                msg = bundle.getString("resultMsg");
            }
            Log.d(TAG, msg);
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();           
        }
        else if(action.equals(MinervaManager.ACTION_MINERVA_UPSTREAM_MSG_RESULT)) {
            Log.d(TAG, "onReceive" + action);
            Bundle bundle = intent.getExtras();
            int resultCode = bundle.getInt("resultCode");    
            String resultMsg = bundle.getString("resultMsg");
            //umi(upstream message id) 
            String umi = bundle.getString("umi");
            //yes upstream message success
            if(resultCode == Result.RESULT_OK) {
                //do something
                Log.d(TAG, "upstream success umi = " + umi);
            }          
            //upstream message fail
            else {
               //do something.
                Log.d(TAG, "upstream fail umi = " + umi);
            }       
        }                 
        else if(action.equals(MinervaManager.ACTION_MINERVA_P2P_MSG_RESULT)) {
            Log.d(TAG, "onReceive" + action);                        
            Bundle bundle = intent.getExtras();
            int resultCode = bundle.getInt("resultCode");    
            String resultMsg = bundle.getString("resultMsg");
            //pmi(P2P message id) 
            String pmi = bundle.getString("pmi");
            //yes upstream message success
            if(resultCode == Result.RESULT_OK) {
                //do something
                Log.d(TAG, "P2P send success pmi = " + pmi);
            }          
            //upstream message fail
            else {
               //do something.
                Log.d(TAG, "upstream fail pmi = " + pmi);
            }       
        }                 
        else if(action.equals(MinervaManager.ACTION_MINERVA_PUSH_MSG_RECEIVED)) {
            Log.d(TAG, "onReceive 2 " + action);            
            //jungdo_for_test
            //String serverURL = intent.getExtras().getString("serverURL");            
            Bundle bundle = intent.getExtras();
            int msgSize = bundle.getInt(MinervaManager.FIELD_MSG_SIZE);
            String jsonStr = bundle.getString(MinervaManager.FIELD_MSG_LIST);
            //example..
            /*
            try {
                ArrayList<Map<String, Object>> testJson = mapper.readValue(jsonStr, new TypeReference<ArrayList<Map<String, Object>>>() {});
                
                for(int i = 0; i < msgSize; i++) {
                    Map<String, Object> oneMsg = testJson.get(i);
                    String data = (String)oneMsg.get(MinervaWrapperProtocol.FIELD_PUSH_DATA);
                    long serverTime = (Long)oneMsg.get(MinervaWrapperProtocol.FIELD_PUSH_SERVER_TIME);                    
                }
                
                System.out.println();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
            //example end
             * */           
            Intent sendIntent = new Intent(context, MyService.class);
            sendIntent.putExtra(MyService.SERVICE_TYPE, MyService.SERVICE_TYPE_PUSH_MSG_RECEIVED);
            Object[] argv = new Object[2];
            argv[0] = msgSize;
            argv[1] = jsonStr;
            sendIntent.putExtra(MyService.SERVICE_ARGV, argv);      
            context.startService(sendIntent);         
        }  
        else if(action.equals(MinervaManager.ACTION_MINERVA_P2P_MSG_RECEIVED)) {
            Log.d(TAG, "onReceive 2 " + action);            
            //jungdo_for_test
            //String serverURL = intent.getExtras().getString("serverURL");            
            Bundle bundle = intent.getExtras();
            int msgSize = bundle.getInt(MinervaManager.FIELD_MSG_SIZE);
            String jsonStr = bundle.getString(MinervaManager.FIELD_MSG_LIST);
            //example..
            /*
            try {
                ArrayList<Map<String, Object>> testJson = mapper.readValue(jsonStr, new TypeReference<ArrayList<Map<String, Object>>>() {});
                
                for(int i = 0; i < msgSize; i++) {
                    Map<String, Object> oneMsg = testJson.get(i);
                    String data = (String)oneMsg.get(MinervaWrapperProtocol.FIELD_PUSH_DATA);
                    long serverTime = (Long)oneMsg.get(MinervaWrapperProtocol.FIELD_PUSH_SERVER_TIME);                    
                }
                
                System.out.println();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
            //example end
             * */           
            Intent sendIntent = new Intent(context, MyService.class);
            sendIntent.putExtra(MyService.SERVICE_TYPE, MyService.SERVICE_TYPE_P2P_MSG_RECEIVED);
            Object[] argv = new Object[2];
            argv[0] = msgSize;
            argv[1] = jsonStr;
            sendIntent.putExtra(MyService.SERVICE_ARGV, argv);      
            context.startService(sendIntent);         
        }                 
    }
}