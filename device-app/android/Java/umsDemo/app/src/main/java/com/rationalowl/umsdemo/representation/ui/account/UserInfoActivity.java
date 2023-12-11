package com.rationalowl.umsdemo.representation.ui.account;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.rationalowl.minerva.client.android.MinervaManager;
import com.rationalowl.umsdemo.R;
import com.rationalowl.umsdemo.databinding.ActivityUserInfoBinding;
import com.rationalowl.umsdemo.domain.Config;
import com.rationalowl.umsdemo.domain.MessagesRepository;
import com.rationalowl.umsdemo.domain.UserRepository;
import com.rationalowl.umsdemo.protocol.PushAppProto;
import com.rationalowl.umsdemo.protocol.UmsClient;
import com.rationalowl.umsdemo.protocol.UmsResult;

import java.text.SimpleDateFormat;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserInfoActivity extends AppCompatActivity {
    private final static String TAG = "UserInfoActivity";

    private ActivityUserInfoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityUserInfoBinding.inflate(getLayoutInflater());
        binding.setUser(UserRepository.getInstance().getUser());
        binding.setDateFormat(new SimpleDateFormat(getString(R.string.joined_date_format), Locale.KOREAN));
        binding.setLifecycleOwner(this);

        binding.buttonDeleteAccount.setOnClickListener(v -> deleteAccount());

        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void deleteAccount() {
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.delete_account)
                .setMessage(R.string.delete_account_message)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.delete_account, (dialogInterface, i) -> {
                    final PushAppProto.PushAppUnregUserReq request = new PushAppProto.PushAppUnregUserReq();
                    request.mAccountId = Config.getInstance().getUmsAccountId();
                    request.mPhoneNumber = binding.getUser().getPhoneNumber();

                    UmsClient.getService().deleteUser(request).enqueue(new Callback<PushAppProto.PushAppUnregUserRes>() {
                        @Override
                        public void onResponse(@NonNull Call<PushAppProto.PushAppUnregUserRes> call, @NonNull Response<PushAppProto.PushAppUnregUserRes> response) {
                            assert response.body() != null;

                            final int resultCode = response.body().mResultCode;

                            if (resultCode != UmsResult.RESULT_OK) {
                                Log.e(TAG, "Result code: " + resultCode);

                                runOnUiThread(() ->
                                        new MaterialAlertDialogBuilder(UserInfoActivity.this)
                                                .setTitle(R.string.delete_account_error)
                                                .setMessage(getString(R.string.error_code_message, resultCode))
                                                .setPositiveButton(R.string.ok, null)
                                                .show()
                                );

                                return;
                            }

                            UserRepository.getInstance().delete();
                            MessagesRepository.getInstance().delete();

                            final Config config = Config.getInstance();
                            final String serviceId = config.getRoSvcId();
                            MinervaManager.getInstance().unregisterDevice(serviceId);

                            System.exit(0);
                        }

                        @Override
                        public void onFailure(@NonNull Call<PushAppProto.PushAppUnregUserRes> call, @NonNull Throwable t) {
                            Log.e(TAG, t.getMessage(), t);
                        }
                    });
                })
                .show();
    }
}