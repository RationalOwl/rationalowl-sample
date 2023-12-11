package com.rationalowl.umsdemo.representation.viewModel;

import androidx.lifecycle.MutableLiveData;

import com.rationalowl.umsdemo.domain.Message;

import java.util.Objects;

public class MessageViewModel
{
    private final Message message;
    public final MutableLiveData<Boolean> isChecked = new MutableLiveData<>(false);

    public MessageViewModel(Message message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != getClass()) return false;

        final MessageViewModel other = (MessageViewModel) obj;
        return message.equals(other.message)
                && isChecked.getValue() == other.isChecked.getValue();
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, isChecked);
    }

    public Message getMessage()
    {
        return message;
    }

    public MutableLiveData<Boolean> isChecked()
    {
        return isChecked;
    }

    public void setChecked(boolean value)
    {
        isChecked.setValue(value);
    }
}
