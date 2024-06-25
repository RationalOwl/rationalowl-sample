package com.rationalowl.umsdemo.presentation.account.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.rationalowl.bridgeUms.PushAppProto;
import com.rationalowl.bridgeUms.UmsResult;
import com.rationalowl.minerva.client.android.MinervaManager;
import com.rationalowl.umsdemo.MainActivity;
import com.rationalowl.umsdemo.R;
import com.rationalowl.umsdemo.api.UmsService;
import com.rationalowl.umsdemo.data.Config;
import com.rationalowl.umsdemo.data.DataCallback;
import com.rationalowl.umsdemo.data.datasource.MessageLocalDataSource;
import com.rationalowl.umsdemo.data.datasource.ServerLocalDataSource;
import com.rationalowl.umsdemo.data.datasource.UserLocalDataSource;
import com.rationalowl.umsdemo.databinding.ActivityUserInfoBinding;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class UserInfoActivity extends AppCompatActivity {
    private static final String TAG = "UserInfoActivity";

    private ActivityUserInfoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityUserInfoBinding.inflate(getLayoutInflater());
        binding.setUser(UserLocalDataSource.getInstance().getUser());
        binding.setDateFormat(new SimpleDateFormat(getString(R.string.joined_date_format), Locale.KOREAN));
        binding.setLifecycleOwner(this);

        binding.buttonDeleteAccount.setOnClickListener(v ->
                new MaterialAlertDialogBuilder(this)
                        .setTitle(R.string.delete_account)
                        .setMessage(Html.fromHtml(getString(R.string.delete_account_message), HtmlCompat.FROM_HTML_MODE_LEGACY))
                        .setNegativeButton(R.string.cancel, null)
                        .setPositiveButton(R.string.delete_account, (dialogInterface, i) -> deleteAccount())
                        .show());

        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void deleteAccount() {
        final PushAppProto.PushAppUnregUserReq request = new PushAppProto.PushAppUnregUserReq();
        request.mAccountId = Config.getInstance().getUmsAccountId();
        request.mPhoneNumber = binding.getUser().getPhoneNumber();

        UmsService.getInstance().deleteUser(request, new DataCallback<PushAppProto.PushAppUnregUserRes>() {
            @Override
            public void onResponse(PushAppProto.PushAppUnregUserRes response) {
                final int resultCode = response.mResultCode;

                if (resultCode != UmsResult.RESULT_OK) {
                    Log.e(TAG, "[" + resultCode + "] " + response.mComment);

                    final String message = getString(R.string.error_code_message, resultCode);
                    onError(message);
                    return;
                }

                UserLocalDataSource.getInstance().delete();
                ServerLocalDataSource.getInstance().delete();
                MessageLocalDataSource.getInstance().delete();

                final Config config = Config.getInstance();
                final String serviceId = config.getRoSvcId();
                MinervaManager.getInstance().unregisterDevice(serviceId);

                finish();

                final Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }

            @Override
            public void onFailure(IOException e) {
                onError(e.getMessage());
            }
        });
    }

    private void onError(String message) {
        runOnUiThread(() ->
                new MaterialAlertDialogBuilder(this)
                        .setTitle(R.string.delete_account_error)
                        .setMessage(message)
                        .setPositiveButton(R.string.ok, null)
                        .show()
        );
    }
}