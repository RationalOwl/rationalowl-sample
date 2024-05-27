package com.rationalowl.umsdemo.presentation.account.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.rationalowl.umsdemo.databinding.ActivitySignupBinding;
import com.rationalowl.umsdemo.presentation.account.viewmodel.SignUpViewModel;

public class SignUpActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ActivitySignupBinding binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        new ViewModelProvider(this, ViewModelProvider.Factory.from(SignUpViewModel.initializer)).get(SignUpViewModel.class);
    }
}
