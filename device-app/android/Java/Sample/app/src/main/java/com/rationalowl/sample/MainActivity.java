package com.rationalowl.sample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.rationalowl.minerva.client.android.util.Logger;

public class MainActivity  extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button regBtn = (Button) findViewById(R.id.btn_reg);
        Button msgBtn = (Button) findViewById(R.id.btn_msg);
        regBtn.setOnClickListener(this);
        msgBtn.setOnClickListener(this);

        // check push permission
        checkPushPermission();
    }


    @Override
    public void onDestroy() {
        Logger.debug(TAG, "onDestroy() enter");
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        int resId = view.getId();

        if(resId == R.id.btn_reg) {
            Intent intent = new Intent(this, RegisterActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }
        else if(resId == R.id.btn_msg) {
            Intent intent = new Intent(this, MsgActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }
    }

    private void checkPushPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // permission not allowed
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // simply request push permission.
                Logger.error("Hi", "user rejected push permission in the past");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
            }
            // Already permission allowed
            else {
                // do nothing.
                Logger.debug("Hi", "push permission allowed");
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int resultCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(resultCode, permissions, grantResults);

        // do your logic.
    }
}