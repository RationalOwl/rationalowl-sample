package com.rationalowl.umsdemo.presentation.account.viewmodel;

import static androidx.lifecycle.SavedStateHandleSupport.createSavedStateHandle;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

public class SignUpViewModel extends ViewModel {
    private static final String PHONE_COUNTRY_CODE_KEY = "phone_country_code";
    private static final String PHONE_NUMBER_KEY = "phone_number";
    private static final String PHONE_AUTH_NUMBER_KEY = "phone_auth_number";
    private static final String TERMS_AGREEMENT_KEY = "terms_agreement";
    private static final String PRIVACY_POLICY_AGREEMENT_KEY = "privacy_policy_agreement";
    private static final String USER_NAME_KEY = "user_name";
    private static final String USER_ID_KEY = "user_id";

    private SavedStateHandle handle;

    public SignUpViewModel(SavedStateHandle state) {
        this.handle = state;
    }

    @Override
    protected void onCleared() {
        handle = null;
        super.onCleared();
    }

    public MutableLiveData<String> getPhoneCountryCode() {
        return handle.getLiveData(PHONE_COUNTRY_CODE_KEY);
    }

    public MutableLiveData<String> getPhoneNumber() {
        return handle.getLiveData(PHONE_NUMBER_KEY);
    }

    public MutableLiveData<String> getPhoneAuthNumber() {
        return handle.getLiveData(PHONE_AUTH_NUMBER_KEY);
    }

    public MutableLiveData<Boolean> getTermsAgreement() {
        return handle.getLiveData(TERMS_AGREEMENT_KEY);
    }

    public MutableLiveData<Boolean> getPrivacyPolicyAgreement() {
        return handle.getLiveData(PRIVACY_POLICY_AGREEMENT_KEY);
    }

    public MutableLiveData<String> getUserName() {
        return handle.getLiveData(USER_NAME_KEY);
    }

    public MutableLiveData<String> getUserId() {
        return handle.getLiveData(USER_ID_KEY);
    }

    public void setPhoneCountryCode(String value) {
        handle.set(PHONE_COUNTRY_CODE_KEY, value);
    }

    public void setPhoneNumber(String value) {
        handle.set(PHONE_NUMBER_KEY, value);
    }

    public void setPhoneAuthNumber(String value) {
        handle.set(PHONE_AUTH_NUMBER_KEY, value);
    }

    public void setTermsAgreement(boolean value) {
        handle.set(TERMS_AGREEMENT_KEY, value);
    }

    public void setPrivacyPolicyAgreement(boolean value) {
        handle.set(PRIVACY_POLICY_AGREEMENT_KEY, value);
    }

    public void setUserName(String value) {
        handle.set(USER_NAME_KEY, value);
    }

    public void setUserId(String value) {
        handle.set(USER_ID_KEY, value);
    }

    public static final ViewModelInitializer<SignUpViewModel> initializer = new ViewModelInitializer<>(
            SignUpViewModel.class,
            creationExtras -> {
                final SavedStateHandle savedStateHandle = createSavedStateHandle(creationExtras);
                return new SignUpViewModel(savedStateHandle);
            }
    );
}
