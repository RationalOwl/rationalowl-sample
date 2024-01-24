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
import com.rationalowl.umsdemo.databinding.FragmentSignUpVerifyPhoneBinding;
import com.rationalowl.umsdemo.domain.Config;
import com.rationalowl.umsdemo.protocol.PushAppProto;
import com.rationalowl.umsdemo.protocol.UmsClient;
import com.rationalowl.umsdemo.protocol.UmsProtocol;
import com.rationalowl.umsdemo.protocol.UmsResult;
import com.rationalowl.umsdemo.representation.viewModel.SignUpViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        viewModel = null;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        assert getActivity() != null;

        viewModel = new ViewModelProvider(getActivity()).get(SignUpViewModel.class);
        binding.setViewModel(viewModel);

        binding.buttonConfirmVerification.setOnClickListener(v -> verifyAuthNumber());
        binding.buttonEditPhoneNumber.setOnClickListener(v -> NavHostFragment.findNavController(this).navigate(R.id.action_VerifyPhoneFragment_to_InputPhoneFragment));
    }

    private void verifyAuthNumber() {
        final PushAppProto.PushAppVerifyAuthNumberReq request = new PushAppProto.PushAppVerifyAuthNumberReq();
        request.mAccountId = Config.getInstance().getUmsAccountId();
        request.mDeviceType = UmsProtocol.APP_TYPE_ANDROID;
        request.mPhoneNumber = viewModel.getPhoneNumber().getValue();
        request.mAuthNumber = viewModel.getPhoneAuthNumber().getValue();

        UmsClient.getService().verifyAuthNumber(request).enqueue(new Callback<PushAppProto.PushAppVerifyAuthNumberRes>() {
            @Override
            public void onResponse(@NonNull Call<PushAppProto.PushAppVerifyAuthNumberRes> call, @NonNull Response<PushAppProto.PushAppVerifyAuthNumberRes> response) {
                assert response.body() != null && getActivity() != null;

                final int resultCode = response.body().mResultCode;

                if (resultCode != UmsResult.RESULT_OK) {
                    getActivity().runOnUiThread(() ->
                            new MaterialAlertDialogBuilder(getActivity())
                                    .setTitle(R.string.auth_failed)
                                    .setMessage(response.body().mComment)
                                    .setPositiveButton(R.string.ok, null)
                                    .show()
                    );

                    return;
                }

                getActivity().runOnUiThread(() -> NavHostFragment.findNavController(SignUpVerifyPhoneFragment.this).navigate(R.id.action_VerifyPhoneFragment_to_AgreementsFragment));
            }

            @Override
            public void onFailure(@NonNull Call<PushAppProto.PushAppVerifyAuthNumberRes> call, @NonNull Throwable t) {
                Log.e(TAG, t.getMessage(), t);
                assert getActivity() != null;

                getActivity().runOnUiThread(() ->
                        new MaterialAlertDialogBuilder(getActivity())
                                .setTitle(R.string.auth_error)
                                .setMessage(t.getMessage())
                                .setPositiveButton(R.string.ok, null)
                                .show()
                );
            }
        });
    }
}