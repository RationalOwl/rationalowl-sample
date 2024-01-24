package com.rationalowl.umsdemo.representation.ui.account;

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
import com.rationalowl.umsdemo.R;
import com.rationalowl.umsdemo.databinding.FragmentSignUpInputPhoneBinding;
import com.rationalowl.umsdemo.domain.Config;
import com.rationalowl.umsdemo.protocol.PushAppProto;
import com.rationalowl.umsdemo.protocol.UmsClient;
import com.rationalowl.umsdemo.protocol.UmsResult;
import com.rationalowl.umsdemo.representation.viewModel.SignUpViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    private void sendVerificationCode() {
        final PushAppProto.PushAppAuthNumberReq request = new PushAppProto.PushAppAuthNumberReq();
        request.mAccountId = Config.getInstance().getUmsAccountId();
        request.mCountryCode = viewModel.getPhoneCountryCode().getValue();
        request.mPhoneNumber = viewModel.getPhoneNumber().getValue();

        UmsClient.getService().requestAuthNumber(request).enqueue(new Callback<PushAppProto.PushAppAuthNumberRes>() {
            @Override
            public void onResponse(@NonNull Call<PushAppProto.PushAppAuthNumberRes> call, @NonNull Response<PushAppProto.PushAppAuthNumberRes> response) {
                assert response.body() != null && getActivity() != null;

                final int resultCode = response.body().mResultCode;

                if (resultCode != UmsResult.RESULT_OK) {
                    Log.e(TAG, "Result code: " + resultCode);

                    getActivity().runOnUiThread(() ->
                            new MaterialAlertDialogBuilder(getActivity())
                                    .setTitle(R.string.phone_auth_error)
                                    .setMessage(getString(R.string.error_code_message, resultCode))
                                    .setPositiveButton(R.string.ok, null)
                                    .show()
                    );

                    return;
                }

                getActivity().runOnUiThread(() -> NavHostFragment.findNavController(SignUpInputPhoneFragment.this).navigate(R.id.action_InputPhoneFragment_to_VerifyPhoneFragment));
            }

            @Override
            public void onFailure(@NonNull Call<PushAppProto.PushAppAuthNumberRes> call, @NonNull Throwable t) {
                Log.e(TAG, t.getMessage(), t);
                assert getActivity() != null;

                getActivity().runOnUiThread(() ->
                        new MaterialAlertDialogBuilder(getActivity())
                                .setTitle(R.string.phone_auth_error)
                                .setMessage(t.getMessage())
                                .setPositiveButton(R.string.ok, null)
                                .show()
                );
            }
        });
    }
}