package com.rationalowl.sample;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.rationalowl.minerva.client.android.DeviceRegisterResultListener;
import com.rationalowl.minerva.client.android.MinervaManager;
import com.rationalowl.minerva.client.android.Result;
import com.rationalowl.minerva.client.android.util.Logger;

public class RegisterActivity extends Activity implements View.OnClickListener, DeviceRegisterResultListener {

    private static final String TAG = "RegisterActivity";


    RadioGroup mSCRg;
    RadioButton mAcceptRb;
    RadioButton mRejectRb;

    EditText mUrlEt;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mUrlEt = (EditText) this.findViewById(R.id.url);
        Button regBtn = (Button) findViewById(R.id.regBtn);
        Button unreglBtn = (Button) findViewById(R.id.unregBtn);
        regBtn.setOnClickListener(this);
        unreglBtn.setOnClickListener(this);
    }


    @Override
    protected void onDestroy() {
        //Logger.debug(TAG, "onDestroy enter");
        super.onDestroy();

    }


    @Override
    protected void onStart() {
        //Logger.debug(TAG, "onStart() enter");
        super.onStart();
        mUrlEt.setText("gate.rationalowl.com");
        //mUrlEt.setText("211.239.150.124"); //aws dev
        //mUrlEt.setText("3.39.200.245"); //aws dev
        //mUrlEt.setText("117.52.153.229"); // NH network



    }


    @Override
    protected void onResume() {
        //Logger.debug(TAG, "onResume enter");
        super.onResume();

        //set register callback listener
        MinervaManager minMgr = MinervaManager.getInstance();
        minMgr.setRegisterResultListener(this);
    }

    public void onClick(View v) {
        int resId = v.getId();

        if(resId == R.id.regBtn) {
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
                String url = mUrlEt.getText().toString();
                mgr.setRegisterResultListener(this);
                mgr.registerDevice(url, "9bd4db31dbaa4897ad9aa81c3e7e183a","sample android");
            });
        }
        else if(resId == R.id.unregBtn) {
            MinervaManager mgr = MinervaManager.getInstance();
            //mgr.unregisterDevice("afab0b12c8f44c00860195446032933d"); //aws dev gate
            //mgr.unregisterDevice("def829b853d046779e2227bdd091653c"); //hostway
            //mgr.unregisterDevice("SVC59132746-625b-4773-b5db-aa8a0775e04d"); //aws test
            mgr.unregisterDevice("9bd4db31dbaa4897ad9aa81c3e7e183a"); //aws dev gate
        }
    }

    @Override
    public void onRegisterResult(int resultCode, String resultMsg, String deviceRegId) {
        Logger.debug(TAG, "onRegisterResult " + resultCode);
        String msg = resultMsg + "registration id : " + deviceRegId;
        //yes registration has completed successfully!
        if(resultCode == Result.RESULT_OK) {
            // save deviceRegId to local file
            // and send deviceRegId to app server using MinervaManager.sendUpstreamMsg()
            // MinervaManager minMgr = MinervaManager.getInstance();
            // minMgr.sendUpstreamMsg("data including deviceRegId", "your app server registration id");
        }
        //already registered
        else if(resultCode == Result.RESULT_DEVICE_ALREADY_REGISTERED) {
            // already registered.
        }
        //registration error has occurred!
        else {
            //error occurred while registering device app.
        }
        Logger.debug(TAG, msg);
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
