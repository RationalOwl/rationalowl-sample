package com.rationalowl.umsdemo.presentation.account.view;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.rationalowl.umsdemo.R;
import com.rationalowl.umsdemo.databinding.FragmentSignUpAgreementsBinding;
import com.rationalowl.umsdemo.presentation.account.viewmodel.SignUpViewModel;

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

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(SignUpViewModel.class);
        binding.setViewModel(viewModel);

        loadTextAsset("terms.txt", binding.textviewTerms);
        loadTextAsset("privacy_policy.txt", binding.textviewPrivacyPolicy);

        binding.buttonNext.setOnClickListener(v -> onButtonNextClick());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        viewModel = null;
    }

    private void loadTextAsset(String fileName, TextView textView) {
        try {
            final AssetManager assetManager = getResources().getAssets();

            try (InputStream stream = assetManager.open(fileName)) {
                byte[] buffer = new byte[stream.available()];
                stream.read(buffer);
                textView.setText(new String(buffer));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Event handlers
    private void onButtonNextClick() {
        NavHostFragment.findNavController(this).navigate(R.id.action_AgreementsFragment_to_InputInfoFragment);
    }
}
