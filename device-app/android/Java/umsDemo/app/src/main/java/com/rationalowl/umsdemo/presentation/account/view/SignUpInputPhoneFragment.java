package com.rationalowl.umsdemo.presentation.account.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.rationalowl.bridgeUms.PushAppProto;
import com.rationalowl.bridgeUms.UmsResult;
import com.rationalowl.umsdemo.R;
import com.rationalowl.umsdemo.api.UmsService;
import com.rationalowl.umsdemo.data.Config;
import com.rationalowl.umsdemo.data.DataCallback;
import com.rationalowl.umsdemo.databinding.FragmentSignUpInputPhoneBinding;
import com.rationalowl.umsdemo.presentation.account.viewmodel.SignUpViewModel;

import java.io.IOException;

public class SignUpInputPhoneFragment extends Fragment {
    private static final String TAG = "SignUpInputPhoneFragment";

    private FragmentSignUpInputPhoneBinding binding;
    private SignUpViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSignUpInputPhoneBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        assert getActivity() != null;

        viewModel = new ViewModelProvider(getActivity()).get(SignUpViewModel.class);
        viewModel.setPhoneCountryCode(getString(R.string.country_code_korea));
        binding.setViewModel(viewModel);

        binding.buttonSendVerification.setOnClickListener(v ->
                new MaterialAlertDialogBuilder(getActivity())
                        .setTitle(R.string.phone_auth)
                        .setMessage(getString(R.string.send_auth_number, viewModel.getPhoneNumber().getValue()))
                        .setNegativeButton(R.string.cancel, null)
                        .setPositiveButton(R.string.ok, (d, i) -> sendVerificationCode())
                        .show()
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        viewModel = null;
    }

    private void sendVerificationCode() {
        final PushAppProto.PushAppAuthNumberReq request = new PushAppProto.PushAppAuthNumberReq();
        request.mAccountId = Config.getInstance().getUmsAccountId();
        request.mCountryCode = viewModel.getPhoneCountryCode().getValue();
        request.mPhoneNumber = viewModel.getPhoneNumber().getValue();

        UmsService.getInstance().requestAuthNumber(request, new DataCallback<PushAppProto.PushAppAuthNumberRes>() {
            @Override
            public void onResponse(PushAppProto.PushAppAuthNumberRes response) {
                final int resultCode = response.mResultCode;

                if (resultCode != UmsResult.RESULT_OK) {
                    Log.e(TAG, "[" + resultCode + "] " + response.mComment);
                    onPhoneAuthError(getString(R.string.error_code_message, resultCode));
                    return;
                }

                requireActivity().runOnUiThread(() ->
                        NavHostFragment.findNavController(SignUpInputPhoneFragment.this).navigate(R.id.action_InputPhoneFragment_to_VerifyPhoneFragment)
                );
            }

            @Override
            public void onFailure(IOException e) {
                onPhoneAuthError(e.getMessage());
            }
        });
    }

    private void onPhoneAuthError(String message) {
        assert getActivity() != null;

        getActivity().runOnUiThread(() ->
                new MaterialAlertDialogBuilder(getActivity())
                        .setTitle(R.string.phone_auth_error)
                        .setMessage(message)
                        .setPositiveButton(R.string.ok, null)
                        .show()
        );
    }
}