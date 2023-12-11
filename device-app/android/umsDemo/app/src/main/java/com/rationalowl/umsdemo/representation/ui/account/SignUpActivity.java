package com.rationalowl.umsdemo.representation.ui.account;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.rationalowl.umsdemo.databinding.ActivitySignupBinding;
import com.rationalowl.umsdemo.representation.viewModel.SignUpViewModel;

public class SignUpActivity extends AppCompatActivity {
    private SignUpViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ActivitySignupBinding binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this, ViewModelProvider.Factory.from(SignUpViewModel.initializer)).get(SignUpViewModel.class);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel = null;
    }
}
