package com.rationalowl.umsdemo.presentation.message.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.rationalowl.umsdemo.data.DataCallback;
import com.rationalowl.umsdemo.data.DataDef;
import com.rationalowl.umsdemo.data.repository.MessageRepository;

import java.io.IOException;

public class MessageReadViewModel extends ViewModel {
    private static final String TAG = "MessageReadViewModel";

    private static final String IMAGE_KEY = "image";

    private SavedStateHandle savedState;
    private DataDef.Message message;

    public MessageReadViewModel(SavedStateHandle state, DataDef.Message message) {
        this.savedState = state;
        this.message = message;

        if (message.getImageId() != null) {
            fetchImage();
        }
    }

    @Override
    protected void onCleared() {
        savedState = null;
        message = null;
        super.onCleared();
    }

    public DataDef.Message getMessage() {
        return message;
    }

    public MutableLiveData<byte[]> getImage() {
        return savedState.getLiveData(IMAGE_KEY);
    }

    public void fetchImage() {
        MessageRepository.getInstance().getImage(message, new DataCallback<byte[]>() {
            @Override
            public void onResponse(byte[] response) {
                getImage().postValue(response);
            }

            @Override
            public void onFailure(IOException e) {
                Log.e(TAG, e.getMessage(), e);
            }
        });
    }
}
