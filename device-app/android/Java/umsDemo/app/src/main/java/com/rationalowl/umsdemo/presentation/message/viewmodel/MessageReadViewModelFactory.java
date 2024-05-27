package com.rationalowl.umsdemo.presentation.message.viewmodel;

import static androidx.lifecycle.SavedStateHandleSupport.createSavedStateHandle;

import androidx.annotation.NonNull;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.viewmodel.CreationExtras;

import com.rationalowl.umsdemo.data.DataDef;

public class MessageReadViewModelFactory implements ViewModelProvider.Factory {
    private final DataDef.Message message;

    public MessageReadViewModelFactory(DataDef.Message message) {
        this.message = message;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass, @NonNull CreationExtras extras) {
        final SavedStateHandle savedStateHandle = createSavedStateHandle(extras);
        return (T) new MessageReadViewModel(savedStateHandle, message);
    }
}
