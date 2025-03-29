package com.rationalowl.umsdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import android.Manifest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.FirebaseMessaging;

import com.rationalowl.bridgeUms.PushAppProto.*;
import com.rationalowl.bridgeUms.UmsApi;
import com.rationalowl.bridgeUms.UmsProtocol;
import com.rationalowl.umsdemo.R;
import com.rationalowl.minerva.client.android.DeviceRegisterResultListener;
import com.rationalowl.minerva.client.android.MinervaManager;
import com.rationalowl.minerva.client.android.Result;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

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

    private static ObjectMapper mapper = new ObjectMapper();

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

        // check push permission
        checkPushPermission();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Broadcast 등록 해제
        unregisterReceiver(mReceiver);
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume() enter");
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
                mgr.registerDevice("dev.rationalowl.com", "adbce9791c2c46cca10aa527a86dd6e7","my helloUms app");
                //mgr.registerDevice("211.239.150.113", "SVC871d16c3-fe28-4f09-ac32-4870d171b067","my helloUms app");
            });
        }
        else if(resId == R.id.btn_unreg) {
            MinervaManager mgr = MinervaManager.getInstance();
            mgr.setRegisterResultListener(this);
            mgr.unregisterDevice("adbce9791c2c46cca10aa527a86dd6e7"); //aws dev gate
        }
    }

    @Override
    public void onRegisterResult(int resultCode, String resultMsg, String deviceRegId) {
        Log.d(TAG, "onRegisterResult " + resultCode);
        String msg = resultMsg + "registration id : " + deviceRegId;
        //yes registration has completed successfully!
        if(resultCode == Result.RESULT_OK || resultCode == Result.RESULT_DEVICE_ALREADY_REGISTERED) {
            Log.d(TAG, "rationalOwl register success!!! ");
            // save deviceRegId to local file
            // and send deviceRegId to app server
            final String fDeviceFegId = deviceRegId;

            // call rationalums rest api
            UmsApi.callInstallUmsApp("923a0aac-abf4-493d-9f9d-787569ac53bc", deviceRegId, null, null, null,
                new Callback() {

                    @Override
                    public void onFailure(Call call, IOException e) {
                        System.err.println("Error Occurred");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        ResponseBody body = response.body();
                        if (body != null) {
                            String resStr = body.string();
                            PushAppInstallRes res = mapper.readValue(resStr, PushAppInstallRes.class);

                            if (res.mResultCode == Result.RESULT_OK) {
                                mDeviceRegId = fDeviceFegId;
                                Log.d(TAG, "단말앱 등록 성공");
                            }
                            else {
                                Log.d(TAG, "resCode:" + res.mResultCode + " comment:" + res.mComment);
                            }
                        }
                    }
                }
            );
        }

        //registration error has occurred!
        else {
            Toast.makeText(this, "단말앱 등록 에러:" + resultMsg, Toast.LENGTH_LONG).show();
        }
        Log.d(TAG, msg);
    }

    @Override
    public void onUnregisterResult(int resultCode, String resultMsg) {

        //yes rationalowl unregistration has completed successfully!
        if(resultCode == Result.RESULT_OK) {
            // call rationalums rest api
            UmsApi.callUnregisterUmsApp("923a0aac-abf4-493d-9f9d-787569ac53bc", mDeviceRegId,
                new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        System.err.println("Error Occurred");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        ResponseBody body = response.body();
                        if (body != null) {
                            String resStr = body.string();
                            PushAppUnregUserRes res = mapper.readValue(resStr, PushAppUnregUserRes.class);

                            if (res.mResultCode == Result.RESULT_OK) {
                                mDeviceRegId = null;
                                Log.d(TAG, "단말앱 해제 성공");
                            }
                            else {
                                Log.d(TAG, "resCode:" + res.mResultCode + " comment:" + res.mComment);
                            }
                        }
                    }
                }
            );
        }
        //registration error has occurred!
        else {
            Toast.makeText(this, "단말앱 해제 에러:" + resultMsg, Toast.LENGTH_LONG).show();
        }
    }

    private void checkPushPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // permission not allowed
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // simply request push permission.
                Log.e("Hi", "user rejected push permission in the past");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
            }
            // Already permission allowed
            else {
                // do nothing.
                Log.d("Hi", "push permission allowed");
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int resultCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(resultCode, permissions, grantResults);

        // do your logic.
    }
}