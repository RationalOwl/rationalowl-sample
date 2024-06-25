package com.rationalowl.umsdemo;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.rationalowl.umsdemo.data.DataDef;
import com.rationalowl.umsdemo.data.datasource.UserLocalDataSource;
import com.rationalowl.umsdemo.presentation.account.view.SignUpActivity;
import com.rationalowl.umsdemo.presentation.message.view.MessageListActivity;

public class MainActivity extends AppCompatActivity {
    private static final String NOTIFICATION_ID = "notices";
    private static final String NOTIFICATION_NAME = "공지";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerNotification();

        final DataDef.User user = UserLocalDataSource.getInstance().getUser();
        final Intent intent;

        if (user == null) {
            intent = new Intent(this, SignUpActivity.class);
        } else {
            intent = new Intent(this, MessageListActivity.class);
        }

        startActivity(intent);
    }

    private void registerNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            final NotificationChannel channel = new NotificationChannel(NOTIFICATION_ID, NOTIFICATION_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            final NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);
        }
    }
}
