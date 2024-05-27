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
import com.rationalowl.bridgeUms.UmsProtocol;
import com.rationalowl.bridgeUms.UmsResult;
import com.rationalowl.umsdemo.R;
import com.rationalowl.umsdemo.api.UmsService;
import com.rationalowl.umsdemo.data.Config;
import com.rationalowl.umsdemo.data.DataCallback;
import com.rationalowl.umsdemo.databinding.FragmentSignUpVerifyPhoneBinding;
import com.rationalowl.umsdemo.presentation.account.viewmodel.SignUpViewModel;

import java.io.IOException;

public class SignUpVerifyPhoneFragment extends Fragment {
    private static final String TAG = "SignUpVerifyPhoneFragment";

    private FragmentSignUpVerifyPhoneBinding binding;
    private SignUpViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSignUpVerifyPhoneBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(SignUpViewModel.class);
        binding.setViewModel(viewModel);

        binding.buttonConfirmVerification.setOnClickListener(v -> verifyAuthNumber());
        binding.buttonEditPhoneNumber.setOnClickListener(v -> NavHostFragment.findNavController(this).navigate(R.id.action_VerifyPhoneFragment_to_InputPhoneFragment));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        viewModel = null;
    }

    private void verifyAuthNumber() {
        final PushAppProto.PushAppVerifyAuthNumberReq request = new PushAppProto.PushAppVerifyAuthNumberReq();
        request.mAccountId = Config.getInstance().getUmsAccountId();
        request.mDeviceType = UmsProtocol.APP_TYPE_ANDROID;
        request.mPhoneNumber = viewModel.getPhoneNumber().getValue();
        request.mAuthNumber = viewModel.getPhoneAuthNumber().getValue();

        UmsService.getInstance().verifyAuthNumber(request, new DataCallback<PushAppProto.PushAppVerifyAuthNumberRes>() {
            @Override
            public void onResponse(PushAppProto.PushAppVerifyAuthNumberRes response) {
                final int resultCode = response.mResultCode;

                if (resultCode != UmsResult.RESULT_OK) {
                    Log.e(TAG, "[" + resultCode + "] " + response.mComment);
                    onAuthFail(response.mComment, false);
                    return;
                }

                requireActivity().runOnUiThread(() -> NavHostFragment.findNavController(SignUpVerifyPhoneFragment.this).navigate(R.id.action_VerifyPhoneFragment_to_AgreementsFragment));
            }

            @Override
            public void onFailure(IOException e) {
                onAuthFail(e.getMessage(), true);
            }
        });
    }

    private void onAuthFail(String message, boolean isError) {
        requireActivity().runOnUiThread(() ->
                new MaterialAlertDialogBuilder(requireActivity())
                        .setTitle(isError ? R.string.auth_error : R.string.auth_failed)
                        .setMessage(message)
                        .setPositiveButton(R.string.ok, null)
                        .show()
        );
    }
}