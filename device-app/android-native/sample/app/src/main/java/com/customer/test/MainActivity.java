package com.customer.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int MENU_ID_REGISTER = Menu.FIRST;
    private static final int MENU_ID_MSG = Menu.FIRST + 1;
    
    
      
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate enter");
        setContentView(R.layout.activity_main);
    }       
    
    
    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy enter");
        super.onDestroy();           
    }

    
    @Override
    protected void onStart() {
        Log.d(TAG, "onStart() enter");
        super.onStart();        
    }
    
    
    @Override
    protected void onResume() {
        Log.d(TAG, "onResume enter");
        super.onResume();              
    }
    
    
    @Override
    protected void onStop() {
        Log.d(TAG, "onStop enter");

        super.onStop();
    }
    
    
    @Override
    protected void onPause() {
        Log.d(TAG, "onPause enter");
        super.onPause();

    }  
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, MENU_ID_REGISTER, 0, "register/un-register");
        menu.add(Menu.NONE, MENU_ID_MSG, 0, "push message");
        return super.onCreateOptionsMenu(menu);
    }
    
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected enter");
        switch (item.getItemId()) {                    
            case MENU_ID_REGISTER: {
                Intent intent = new Intent(this, RegisterActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);                 
                break;       
            }            
            case MENU_ID_MSG: {
                Intent intent = new Intent(this, MsgActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);                 
                break;       
            }
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }           
}