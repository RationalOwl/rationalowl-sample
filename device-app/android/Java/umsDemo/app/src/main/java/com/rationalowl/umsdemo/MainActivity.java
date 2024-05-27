package com.rationalowl.umsdemo;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.rationalowl.umsdemo.data.DataDef;
import com.rationalowl.umsdemo.data.datasource.UserLocalDataSource;
import com.rationalowl.umsdemo.presentation.account.view.SignUpActivity;
import com.rationalowl.umsdemo.presentation.message.view.MessageListActivity;

public class MainActivity extends AppCompatActivity {
    private static final String NOTIFICATION_ID = "notices";
    private static final String NOTIFICATION_NAME = "공지";

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // Permission is granted. Continue the action or workflow in your
                    // app.
                } else {
                    // Explain to the user that the feature is unavailable because the
                    // feature requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final DataDef.User user = UserLocalDataSource.getInstance().getUser();
        final Intent intent;

        registerNotification();

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            final int state = ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS);

            if (state != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }
}
