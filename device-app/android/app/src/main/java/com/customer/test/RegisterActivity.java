package com.customer.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.rationalowl.minerva.client.android.MinervaManager;
import com.rationalowl.minerva.client.android.data.MinervaDataManager;

public class RegisterActivity extends Activity implements OnClickListener {
    
    private static final String TAG = "RegisterActivity";
    
    private static final int MENU_MAIN = Menu.FIRST;
    
    
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
        //Log.d(TAG, "onDestroy enter");
        super.onDestroy();
        
    }

    
    @Override
    protected void onStart() {
        //Log.d(TAG, "onStart() enter");
        super.onStart();         
        //mUrlEt.setText("gate.rationalowl.com");
        mUrlEt.setText("13.125.250.51"); //aws dev
    }
    
    
    @Override
    protected void onResume() {
        //Log.d(TAG, "onResume enter");       
        super.onResume();      
        //setTable();        
    }    
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, MENU_MAIN, 0, "main");
        return super.onCreateOptionsMenu(menu);
    }
    
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected enter");
        switch (item.getItemId()) {
            case MENU_MAIN:
                Log.d(TAG, "onOptionsItemSelected 1");
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);       
                return true;            
        }
        return super.onOptionsItemSelected(item);
    }        
   
    
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.regBtn: {
                String url = mUrlEt.getText().toString();
                MinervaManager mgr = MinervaManager.getInstance();
                //mgr.registerDevice(url, "faebcfe844d54d449136491fb253619d","단말등록이름2"); //hostway
                mgr.registerDevice(url, "5eba22cda7b747c0aa2e22bc7a833d24","My Android 1"); //aws dev gate
                //mgr.registerDevice(url, "faebcfe844d54d449136491fb253619d","단말등록이름"); //tta
                //mgr.registerDevice(url, "def829b853d046779e2227bdd091653c","경민테스트폰"); //hostway
                //mgr.registerDevice(url, "c8574b6882c34db0a6e6691987de1221"); //aws test
                break;

            }
            case R.id.unregBtn:
                MinervaManager mgr = MinervaManager.getInstance();
                mgr.unregisterDevice("5eba22cda7b747c0aa2e22bc7a833d24"); //aws dev gate
                //mgr.unregisterDevice("faebcfe844d54d449136491fb253619d"); //hostway
                //mgr.unregisterDevice("def829b853d046779e2227bdd091653c"); //hostway
                //mgr.unregisterDevice("c8574b6882c34db0a6e6691987de1221"); //aws test
                break;
            default:
                break;
        }        
    }      
}