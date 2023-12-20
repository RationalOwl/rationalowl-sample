package com.rationalowl.hello;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.rationalowl.minerva.client.android.DeviceRegisterResultListener;
import com.rationalowl.minerva.client.android.MinervaManager;
import com.rationalowl.minerva.client.android.Result;
import com.rationalowl.minerva.client.android.util.Logger;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, DeviceRegisterResultListener {


    class SimpleReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            mMsg = intent.getStringArrayExtra(MESSAGE_FROM_RATIONALOWL_MSG_LISTENER_KEY);

            if(mMsg != null) {
                String msgId = mMsg[0];
                String body = mMsg[1];
                String title = mMsg[2];
                String imgId = mMsg[3];

                StringBuilder strB = new StringBuilder();
                strB.append("push from Rationalowl message listener " + "\r\n");
                strB.append("message id: " + msgId + "\r\n");
                strB.append("message id: " + msgId + "\r\n");
                strB.append("title: " + title + "\r\n");
                strB.append("msg: " + body + "\r\n");
                strB.append("image id: " + imgId + "\r\n");
                msgTxt.setText(strB.toString());
            }
        }
    }


    private static final String TAG = "MainActivity";

    public static final String MESSAGE_FROM_FIREBASE_SERVICE_KEY = "messageFromFirebaseMessagingService";
    public static final String MESSAGE_FROM_RATIONALOWL_MSG_LISTENER_KEY = "messageFromRationalowlMsgListener";

    public static final String MESSAGE_FROM_RATIONALOWL_MSG_LISTENER_ACTION = "messageFromRationalowlMsgListenerAction";

    private TextView msgTxt;

    private String mDeviceRegId;

    private String[] mMsg;

    private SimpleReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button regBtn = (Button) findViewById(R.id.btn_reg);
        Button unreglBtn = (Button) findViewById(R.id.btn_unreg);
        msgTxt = (TextView) findViewById(R.id.txt_msg);
        regBtn.setOnClickListener(this);
        unreglBtn.setOnClickListener(this);

        // simply update message from rationalowl msg listener.
        mReceiver = new SimpleReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MESSAGE_FROM_RATIONALOWL_MSG_LISTENER_ACTION);
        registerReceiver(mReceiver, intentFilter);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Broadcast 등록 해제
        unregisterReceiver(mReceiver);
    }

    @Override
    protected void onResume() {
        Logger.debug(TAG, "onResume() enter");
        super.onResume();

        mMsg = getIntent().getStringArrayExtra(MESSAGE_FROM_FIREBASE_SERVICE_KEY);

        if(mMsg != null) {
            String msgId = mMsg[0];
            String body = mMsg[1];
            String title = mMsg[2];
            String imgId = mMsg[3];

            StringBuilder strB = new StringBuilder();
            strB.append("push from firebase messaging service " + "\r\n");
            strB.append("message id: " + msgId + "\r\n");
            strB.append("title: " + title + "\r\n");
            strB.append("msg: " + body + "\r\n");
            strB.append("image id: " + imgId + "\r\n");
            msgTxt.setText(strB.toString());
        }
    }


    public void onClick(View v) {
        int resId = v.getId();

        if(resId == R.id.btn_reg) {
            // sometimes, FCM onNewToken() callback not called,
            // So, before registering we need to call it explicitly.
            FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
                if (!task.isSuccessful()) {
                    Log.e(TAG, "Fetching FCM registration token failed", task.getException());
                    Toast.makeText(this, "FCM Token fetch fail", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Get recent FCM token
                final String fcmToken = task.getResult();
                MinervaManager mgr = MinervaManager.getInstance();
                mgr.setDeviceToken(fcmToken);

                // after setDeviceToken API, call register API.
                mgr.setRegisterResultListener(this);
                mgr.registerDevice("211.239.150.113", "SVC871d16c3-fe28-4f09-ac32-4870d171b067","hello world android");
            });
        }
        else if(resId == R.id.btn_unreg) {
            MinervaManager mgr = MinervaManager.getInstance();
            mgr.setRegisterResultListener(this);
            mgr.unregisterDevice("SVC871d16c3-fe28-4f09-ac32-4870d171b067"); //aws dev gate
        }
    }

    @Override
    public void onRegisterResult(int resultCode, String resultMsg, String deviceRegId) {
        Logger.debug(TAG, "onRegisterResult " + resultCode);
        String msg = resultMsg + "registration id : " + deviceRegId;
        //yes registration has completed successfully!
        if(resultCode == Result.RESULT_OK) {
            // save deviceRegId to local file
            // and send deviceRegId to app server
            mDeviceRegId = deviceRegId;
            Toast.makeText(this, "단말앱 등록 성공", Toast.LENGTH_LONG).show();
        }
        //already registered
        else if(resultCode == Result.RESULT_DEVICE_ALREADY_REGISTERED) {
            // already registered.
            mDeviceRegId = deviceRegId;
            Toast.makeText(this, "단말앱 기등록됨", Toast.LENGTH_LONG).show();
        }
        //registration error has occurred!
        else {
            Toast.makeText(this, "단말앱 등록 에러:" + resultMsg, Toast.LENGTH_LONG).show();
            //error occurred while registering device app.
        }
        Log.d(TAG, msg);
    }

    @Override
    public void onUnregisterResult(int resultCode, String resultMsg) {

        //yes unregistration has completed successfully!
        if(resultCode == Result.RESULT_OK) {
        }
        //registration error has occurred!
        else {
        }
    }
}