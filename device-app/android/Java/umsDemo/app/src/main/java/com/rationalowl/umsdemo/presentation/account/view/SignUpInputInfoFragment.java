package com.rationalowl.umsdemo.presentation.account.view;

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
import com.rationalowl.bridgeUms.PushAppProto;
import com.rationalowl.bridgeUms.UmsProtocol;
import com.rationalowl.bridgeUms.UmsResult;
import com.rationalowl.minerva.client.android.DeviceRegisterResultListener;
import com.rationalowl.minerva.client.android.MinervaManager;
import com.rationalowl.minerva.client.android.Result;
import com.rationalowl.umsdemo.R;
import com.rationalowl.umsdemo.api.UmsService;
import com.rationalowl.umsdemo.data.Config;
import com.rationalowl.umsdemo.data.DataCallback;
import com.rationalowl.umsdemo.data.DataDef;
import com.rationalowl.umsdemo.data.datasource.ServerLocalDataSource;
import com.rationalowl.umsdemo.data.datasource.UserLocalDataSource;
import com.rationalowl.umsdemo.databinding.FragmentSignUpInputInfoBinding;
import com.rationalowl.umsdemo.presentation.account.viewmodel.SignUpViewModel;
import com.rationalowl.umsdemo.presentation.message.view.MessageListActivity;

import java.io.IOException;

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
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(SignUpViewModel.class);
        binding.setViewModel(viewModel);

        binding.buttonConfirmSignUp.setOnClickListener(v -> signUp());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        final MinervaManager minMgr = MinervaManager.getInstance();
        minMgr.clearRegisterResultListener();

        binding = null;
        viewModel = null;
    }

    private void signUp() {
        // sometimes, FCM onNewToken() callback not called,
        // So, before registering we need to call it explicitly.
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e(TAG, "An error occurred in FirebaseMessaging.getInstance().getToken()", task.getException());
                Toast.makeText(getActivity(), "가입 처리 중 문제가 발생했습니다.(fetch fcm token error)", Toast.LENGTH_SHORT).show();
                return;
            }

            // Get new FCM registration token
            final String token = task.getResult();
            registerDevice(token);
        });
    }

    private void registerDevice(String token) {
        final MinervaManager minMgr = MinervaManager.getInstance();
        minMgr.setDeviceToken(token);
        // set register listener
        minMgr.setRegisterResultListener(new DeviceRegisterResultListener() {
            @Override
            public void onRegisterResult(int resultCode, String resultMsg, String deviceRegId) {
                Log.d(TAG, "DeviceRegisterResultListener.onRegisterResult(resultCode: " + resultCode + ", resultMsg: " + resultMsg + ", deviceRegId: " + deviceRegId + ")");

                //registration error has occurred!
                if (resultCode != Result.RESULT_OK && resultCode != Result.RESULT_DEVICE_ALREADY_REGISTERED) {
                    Log.e(TAG, "DeviceRegisterResultListener.onRegisterResult failed: [" + resultCode + "] " + resultMsg);

                    requireActivity().runOnUiThread(() ->
                            new MaterialAlertDialogBuilder(requireActivity())
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
        minMgr.registerDevice(gateHost, serviceId, viewModel.getPhoneNumber().getValue());
    }

    private void saveUser(String deviceRegId) {
        final String userId = viewModel.getUserId().getValue();
        final String phoneCountryCode = viewModel.getPhoneCountryCode().getValue();
        final String phoneNumber = viewModel.getPhoneNumber().getValue();
        final String userName = viewModel.getUserName().getValue();

        final DataDef.User user = new DataDef.User(deviceRegId, phoneCountryCode, phoneNumber, userName, userId);
        UserLocalDataSource.getInstance().setUser(user);
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

        UmsService.getInstance().signUp(request, new DataCallback<PushAppProto.PushAppInstallRes>() {
            @Override
            public void onResponse(PushAppProto.PushAppInstallRes response) {
                final int resultCode = response.mResultCode;

                if (resultCode != UmsResult.RESULT_OK && resultCode != UmsResult.RESULT_ACCOUNT_ALREADY_REGISTERED) {
                    Log.e(TAG, "[" + resultCode + "] " + response.mComment);

                    final String message = getString(R.string.error_code_message, resultCode);
                    onSignUpError(message);
                    return;
                }

                // save ums server info
                final DataDef.Server server = new DataDef.Server(response.mUmsServerRegId);
                ServerLocalDataSource.getInstance().setServer(server);

                final int titleId, messageId;

                if (resultCode == UmsResult.RESULT_OK) {
                    titleId = R.string.new_user;
                    messageId = R.string.welcome_new_user;
                } else {
                    titleId = R.string.old_user;
                    messageId = R.string.welcome_old_user;
                }

                requireActivity().runOnUiThread(() ->
                        new MaterialAlertDialogBuilder(requireActivity())
                                .setTitle(titleId)
                                .setMessage(getString(messageId, viewModel.getUserName().getValue()))
                                .setPositiveButton(R.string.ok, null)
                                .setOnDismissListener(dialogInterface -> {
                                    final Intent intent = new Intent(getActivity(), MessageListActivity.class);
                                    startActivity(intent);
                                    requireActivity().finish();
                                })
                                .show()
                );
            }

            @Override
            public void onFailure(IOException e) {
                Log.e(TAG, e.getMessage(), e);
                onSignUpError(e.getMessage());
            }
        });
    }

    private void onSignUpError(String message) {
        assert getActivity() != null;

        getActivity().runOnUiThread(() ->
                new MaterialAlertDialogBuilder(getActivity())
                        .setTitle(R.string.sign_up_error)
                        .setMessage(message)
                        .setPositiveButton(R.string.ok, null)
                        .show()
        );
    }
}
