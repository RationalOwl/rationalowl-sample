package com.customer.test;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.rationalowl.minerva.client.android.util.Logger;



public class MainActivity  extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final int MENU_ID_REGISTER = Menu.FIRST;
    private static final int MENU_ID_MSG_LIST = Menu.FIRST + 1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createNotificationChannel();

        handleIntent(getIntent());
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }


    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("myNotiChannelId", "myNotiChannelName", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("my notification channel description");
            NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
    }


    // fcm notification data delivered to the launcher activity when user tap on fcm notification
    private void handleIntent(Intent intent) {

        Bundle bundle = intent.getExtras();

        // launch by app icon click
        if(bundle == null) {
            Logger.debug(TAG, "handle bundle null");
            // remain main activity
            // do nothing.
        }
        // launch by push notification tap
        else {

            String target = bundle.getString("target_activity");

            // if you want to branch activity when user tap on notification,
            // set fcm data field.
            // fcm data format can be below
            // "data":{"target_activity":"A_Activity", "data_to_display":"hi"}
            if(target != null) {
                // yes, you can launch activity what you want to
                switch(target) {
                    case "A_Activity":
                        // launch A Activity
                        /*
                        Intent newIntent = new Intent(this, AActivity.class);
                        newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(newIntent);
                        */
                        break;
                    case "B_Activity":
                        // launch B Acvitity
                        break;
                    default:
                        // default remain main activity
                        break;
                }
            }
            // if fcm data don't have target_activity
            // default MsgActivity launches
            else {
                // this demo don't use activity selection.
                // If user tap fcm notification, simply, launches message activity.
                Logger.debug(TAG, "handle target null");
                Intent intent2 = new Intent(this, MsgActivity.class);
                intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent2);
            }
        }
    }


    @Override
    public void onDestroy() {
        Logger.debug(TAG, "onDestroy() enter");
        super.onDestroy();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, MENU_ID_REGISTER, 0, "register device app");
        menu.add(Menu.NONE, MENU_ID_MSG_LIST, 1, "message list");
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Logger.debug(TAG, "onOptionsItemSelected enter");
        switch(item.getItemId()) {
            case MENU_ID_REGISTER:
                Logger.debug(TAG, "onOptionsItemSelected 1");
                Intent intent = new Intent(this, RegisterActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                return true;
            case MENU_ID_MSG_LIST:
                Logger.debug(TAG, "onOptionsItemSelected 2");
                Intent intent2 = new Intent(this, MsgActivity.class);
                intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent2);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
