package com.rationalowl.umsdemo;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.rationalowl.umsdemo.domain.User;
import com.rationalowl.umsdemo.domain.UserRepository;
import com.rationalowl.umsdemo.representation.ui.account.SignUpActivity;
import com.rationalowl.umsdemo.representation.ui.message.MessageListActivity;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private ActivityResultLauncher<String> requestPermissionLauncher =
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
        final User user = UserRepository.getInstance().getUser();
        final Intent intent;

        registerNotification();

        if (user == null) {
            Log.d(TAG, "no user");
            intent = new Intent(this, SignUpActivity.class);
        } else {
            intent = new Intent(this, MessageListActivity.class);
        }

        startActivity(intent);
    }

    private void registerNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            final String id = getString(R.string.notification_channel_id);
            final String name = getString(R.string.notification_channel_name);

            final NotificationChannel channel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_DEFAULT);
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
