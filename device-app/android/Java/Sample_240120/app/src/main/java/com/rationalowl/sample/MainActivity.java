package com.rationalowl.sample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
}