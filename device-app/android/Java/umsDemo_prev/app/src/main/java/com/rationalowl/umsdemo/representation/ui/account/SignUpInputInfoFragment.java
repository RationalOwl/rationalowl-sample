package com.rationalowl.umsdemo.representation.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.messaging.FirebaseMessaging;
import com.rationalowl.minerva.client.android.DeviceRegisterResultListener;
import com.rationalowl.minerva.client.android.MinervaManager;
import com.rationalowl.minerva.client.android.Result;
import com.rationalowl.umsdemo.R;
import com.rationalowl.umsdemo.databinding.FragmentSignUpInputInfoBinding;
import com.rationalowl.umsdemo.domain.Config;
import com.rationalowl.umsdemo.domain.Server;
import com.rationalowl.umsdemo.domain.ServerRepository;
import com.rationalowl.umsdemo.domain.User;
import com.rationalowl.umsdemo.domain.UserRepository;
import com.rationalowl.umsdemo.protocol.PushAppProto;
import com.rationalowl.umsdemo.protocol.UmsClient;
import com.rationalowl.umsdemo.protocol.UmsProtocol;
import com.rationalowl.umsdemo.protocol.UmsResult;
import com.rationalowl.umsdemo.representation.ui.message.MessageListActivity;
import com.rationalowl.umsdemo.representation.viewModel.SignUpViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpInputInfoFragment extends Fragment {
    private static final String TAG = "SignUpInputInfoFragment";

    private FragmentSignUpInputInfoBinding binding;
    private SignUpViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSignUpInputInfoBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        viewModel = null;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        assert getActivity() != null;

        viewModel = new ViewModelProvider(getActivity()).get(SignUpViewModel.class);
        binding.setViewModel(viewModel);

        binding.buttonConfirmSignUp.setOnClickListener(v -> {
            // sometimes, FCM onNewToken() callback not called,
            // So, before registering we need to call it explicitly.
            FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
                if (!task.isSuccessful()) {
                    Log.e(TAG, "Fetching FCM registration token failed", task.getException());
                    Toast.makeText(getActivity(), "가입 처리 중 문제가 발생했습니다.(fetch fcm token error)", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Get new FCM registration token
                final String token = task.getResult();
                registerDevice(token);
            });
        });
    }

    private void registerDevice(String token) {
        final MinervaManager manager = MinervaManager.getInstance();
        manager.setDeviceToken(token);
        // set register listener
        manager.setRegisterResultListener(new DeviceRegisterResultListener() {
            @Override
            public void onRegisterResult(int resultCode, String resultMsg, String deviceRegId) {
                assert getActivity() != null;

                Log.d(TAG, "onRegisterResult(resultCode: " + resultCode + ", deviceRegId: " + deviceRegId + ")");

                //registration error has occurred!
                if (resultCode != Result.RESULT_OK && resultCode != Result.RESULT_DEVICE_ALREADY_REGISTERED) {
                    Log.e(TAG, "registerDevice error: " + resultMsg);

                    getActivity().runOnUiThread(() ->
                            new MaterialAlertDialogBuilder(getActivity())
                                    .setTitle(R.string.register_device_error)
                                    .setMessage("[" + resultCode + "] " + resultMsg)
                                    .setPositiveButton(R.string.ok, null)
                                    .show()
                    );

                    return;
                }

                saveUser(deviceRegId);
                registerUser(deviceRegId);
            }

            @Override
            public void onUnregisterResult(int resultCode, String resultMsg) {
            }
        });

        final Config config = Config.getInstance();
        final String gateHost = config.getRoGateHost();
        final String serviceId = config.getRoSvcId();
        manager.registerDevice(gateHost, serviceId, viewModel.getPhoneNumber().getValue());
    }

    private void saveUser(String deviceRegId) {
        final String userId = viewModel.getUserId().getValue() != null ? viewModel.getUserId().getValue() : null;
        final User user = new User(deviceRegId, viewModel.getPhoneCountryCode().getValue(), viewModel.getPhoneNumber().getValue(), viewModel.getUserName().getValue(), userId);
        UserRepository.getInstance().setUser(user);
    }

    private void registerUser(String deviceRegId) {
        final PushAppProto.PushAppInstallReq request = new PushAppProto.PushAppInstallReq();
        request.mAccountId = Config.getInstance().getUmsAccountId();
        request.mDeviceType = UmsProtocol.APP_TYPE_ANDROID;
        request.mDeviceRegId = deviceRegId;
        request.mPhoneNumber = viewModel.getPhoneNumber().getValue();

        final String userId = viewModel.getUserId().getValue();
        request.mAppUserId = userId != null ? userId : "";
        request.mUserName = viewModel.getUserName().getValue();

        UmsClient.getService().signUp(request).enqueue(new Callback<PushAppProto.PushAppInstallRes>() {
            @Override
            public void onResponse(@NonNull Call<PushAppProto.PushAppInstallRes> call, @NonNull Response<PushAppProto.PushAppInstallRes> response) {
                assert response.body() != null && getActivity() != null;

                final int resultCode = response.body().mResultCode;

                if (resultCode != UmsResult.RESULT_OK && resultCode != UmsResult.RESULT_ACCOUNT_ALREADY_REGISTERED) {
                    Log.e(TAG, "Result code: " + resultCode);

                    getActivity().runOnUiThread(() ->
                            new MaterialAlertDialogBuilder(getActivity())
                                    .setTitle(R.string.sign_up_error)
                                    .setMessage(getString(R.string.error_code_message, resultCode))
                                    .setPositiveButton(R.string.ok, null)
                                    .show()
                    );

                    return;
                }

                // save ums server info
                final Server server = new Server(response.body().mUmsServerRegId);
                ServerRepository.getInstance().setServer(server);

                final int titleId, messageId;

                if (resultCode == UmsResult.RESULT_OK) {
                    titleId = R.string.new_user;
                    messageId = R.string.welcome_new_user;
                } else {
                    titleId = R.string.old_user;
                    messageId = R.string.welcome_old_user;
                }

                getActivity().runOnUiThread(() ->
                        new MaterialAlertDialogBuilder(getActivity())
                                .setTitle(getString(titleId))
                                .setMessage(getString(messageId, viewModel.getUserName().getValue()))
                                .setPositiveButton(getString(R.string.ok), null)
                                .setOnDismissListener(dialogInterface -> {
                                    final Intent intent = new Intent(getActivity(), MessageListActivity.class);
                                    startActivity(intent);
                                    getActivity().finish();
                                })
                                .show()
                );
            }

            @Override
            public void onFailure(@NonNull Call<PushAppProto.PushAppInstallRes> call, @NonNull Throwable t) {
                Log.e(TAG, t.getMessage(), t);
                assert getActivity() != null;

                getActivity().runOnUiThread(() ->
                        new MaterialAlertDialogBuilder(getActivity())
                                .setTitle(R.string.sign_up_error)
                                .setMessage(t.getMessage())
                                .setPositiveButton(R.string.ok, null)
                                .show()
                );
            }
        });
    }
}
