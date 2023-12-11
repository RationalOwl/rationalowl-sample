package com.rationalowl.umsdemo.representation.viewModel;

import static androidx.lifecycle.SavedStateHandleSupport.createSavedStateHandle;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import com.rationalowl.umsdemo.domain.Message;
import com.rationalowl.umsdemo.domain.MessagesRepository;
import com.rationalowl.umsdemo.representation.ui.message.MessageListViewType;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MessageListViewModel extends ViewModel {
    private static final String TAG = "MessageListViewModel";

    private static final String VIEW_TYPE_KEY = "view_type";
    private static final String ITEMS_KEY = "items";
    private static final String SELECTED_ITEM_IDS_KEY = "selected_item_ids";

    private final MessagesRepository repository;
    private final SavedStateHandle handle;
    private final CompositeDisposable disposeBag = new CompositeDisposable();

    private List<Message> items;
    private List<String> selectedItemIds = new ArrayList<>();

    public MessageListViewModel(SavedStateHandle state) {
        this.handle = state;
        repository = MessagesRepository.getInstance();

        items = new ArrayList<>(repository.getMessages().getValue());

        Disposable disposable = repository.getMessages()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(messages -> {
                    items = messages;
                    getItems().setValue(items);
                }, error -> {
                });

        disposeBag.add(disposable);

        handle.set(VIEW_TYPE_KEY, MessageListViewType.DEFAULT);
        handle.set(ITEMS_KEY, items);
        handle.set(SELECTED_ITEM_IDS_KEY, selectedItemIds);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposeBag.clear();
    }

    public MutableLiveData<MessageListViewType> getViewType() {
        return handle.getLiveData(VIEW_TYPE_KEY);
    }

    public MutableLiveData<List<Message>> getItems() {
        return handle.getLiveData(ITEMS_KEY);
    }

    public MutableLiveData<List<String>> getSelectedItemIds() {
        return handle.getLiveData(SELECTED_ITEM_IDS_KEY);
    }

    public int getUnreadCount() {
        int count = 0;

        for (Message message : items) {
            if (!message.isRead()) {
                count++;
            }
        }

        Log.d(TAG, "getUnreadCount(): " + count);
        return count;
    }

    public void setSelectedState(@NonNull String messageId, boolean selected) {
        Log.d(TAG, "setSelected(" + messageId + ", " + selected + ")");
        final List<String> newSelections = new ArrayList<>(selectedItemIds);

        if (selected) {
            if (newSelections.contains(messageId)) return;
            newSelections.add(messageId);
        } else {
            newSelections.remove(messageId);
        }

        setSelections(newSelections);
    }

    public void setViewType(MessageListViewType value) {
        Log.d(TAG, "setViewType(" + value.name() + ")");
        getViewType().setValue(value);

        setSelections(new ArrayList<>());
    }

    public void removeSelectedItems() {
        Log.d(TAG, "removeSelectedItems()");

        repository.removeMessages(selectedItemIds.toArray(new String[0]));
        setSelections(new ArrayList<>());
    }

    private void setSelections(List<String> messageIds) {
        selectedItemIds = messageIds;
        getSelectedItemIds().setValue(selectedItemIds);
    }

    public static final ViewModelInitializer<MessageListViewModel> initializer = new ViewModelInitializer<>(
            MessageListViewModel.class,
            creationExtras -> {
                final SavedStateHandle savedStateHandle = createSavedStateHandle(creationExtras);
                return new MessageListViewModel(savedStateHandle);
            }
    );
}
