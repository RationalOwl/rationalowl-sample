package com.rationalowl.umsdemo.presentation.message.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.rationalowl.umsdemo.data.DataDef;

import java.util.Objects;

public class MessageViewModel extends ViewModel {
    private DataDef.Message message;
    public MutableLiveData<Boolean> isChecked = new MutableLiveData<>(false);

    public MessageViewModel(DataDef.Message message) {
        this.message = message;
    }

    @Override
    protected void onCleared() {
        message = null;
        isChecked = null;
        super.onCleared();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != getClass()) return false;

        final MessageViewModel other = (MessageViewModel) obj;
        return message.equals(other.message) && isChecked.getValue() == other.isChecked.getValue();
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, isChecked);
    }

    public DataDef.Message getMessage() {
        return message;
    }

    public MutableLiveData<Boolean> isChecked() {
        return isChecked;
    }

    public void setChecked(boolean value) {
        isChecked.setValue(value);
    }
}
