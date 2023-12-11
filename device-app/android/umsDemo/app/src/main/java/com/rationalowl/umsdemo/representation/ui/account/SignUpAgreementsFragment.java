package com.rationalowl.umsdemo.representation.ui.account;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.rationalowl.umsdemo.R;
import com.rationalowl.umsdemo.databinding.FragmentSignUpAgreementsBinding;
import com.rationalowl.umsdemo.representation.viewModel.SignUpViewModel;

import java.io.IOException;
import java.io.InputStream;

public class SignUpAgreementsFragment extends Fragment {
    private FragmentSignUpAgreementsBinding binding;
    private SignUpViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSignUpAgreementsBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        binding.textviewTerms.setMovementMethod(new ScrollingMovementMethod());
        binding.textviewPrivacyPolicy.setMovementMethod(new ScrollingMovementMethod());

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

        final AssetManager assetManager = getResources().getAssets();

        // 서비스 이용약관
        try {
            final InputStream stream = assetManager.open("terms.txt");

            byte[] buffer = new byte[stream.available()];
            //noinspection ResultOfMethodCallIgnored
            stream.read(buffer);
            stream.close();

            binding.textviewTerms.setText(new String(buffer));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 개인정보 수집 및 이용안내
        try {
            final InputStream stream = assetManager.open("privacy_policy.txt");

            byte[] buffer = new byte[stream.available()];
            //noinspection ResultOfMethodCallIgnored
            stream.read(buffer);
            stream.close();

            binding.textviewPrivacyPolicy.setText(new String(buffer));
        } catch (IOException e) {
            e.printStackTrace();
        }

        binding.buttonNext.setOnClickListener(view1 -> {
            NavHostFragment.findNavController(this).navigate(R.id.action_AgreementsFragment_to_InputInfoFragment);
        });
    }
}
