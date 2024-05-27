package com.rationalowl.umsdemo.presentation.message.viewmodel;

import static androidx.lifecycle.SavedStateHandleSupport.createSavedStateHandle;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import com.rationalowl.umsdemo.data.DataDef;
import com.rationalowl.umsdemo.data.datasource.MessageLocalDataSource;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MessageListViewModel extends ViewModel {
    private static final String VIEW_TYPE_KEY = "view_type";
    private static final String ITEMS_KEY = "items";
    private static final String SELECTED_ITEM_IDS_KEY = "selected_item_ids";

    public enum ViewType {
        DEFAULT,
        DELETE
    }

    public static class Utils {
        public static int countUnread(List<DataDef.Message> messages) {
            int count = 0;

            for (final DataDef.Message message : messages) {
                if (!message.isRead()) {
                    ++count;
                }
            }

            return count;
        }
    }

    private SavedStateHandle savedState;

    public MessageListViewModel(SavedStateHandle savedState) {
        this.savedState = savedState;

        final MessageLocalDataSource dataSource = MessageLocalDataSource.getInstance();

        final List<DataDef.Message> items = new ArrayList<>(dataSource.getMessages());
        dataSource.setOnMessagesChangeListener(messages ->
                getItems().postValue(new ArrayList<>(messages))
        );

        this.savedState.set(VIEW_TYPE_KEY, ViewType.DEFAULT);
        this.savedState.set(ITEMS_KEY, items);
        this.savedState.set(SELECTED_ITEM_IDS_KEY, new HashSet<>());
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        MessageLocalDataSource.getInstance().setOnMessagesChangeListener(null);
        savedState = null;
    }

    public MutableLiveData<ViewType> getViewType() {
        return savedState.getLiveData(VIEW_TYPE_KEY);
    }

    public MutableLiveData<List<DataDef.Message>> getItems() {
        return savedState.getLiveData(ITEMS_KEY);
    }

    public MutableLiveData<Set<String>> getSelectedItemIds() {
        return savedState.getLiveData(SELECTED_ITEM_IDS_KEY);
    }

    public void setViewType(ViewType viewType) {
        getViewType().setValue(viewType);

        if (viewType == ViewType.DEFAULT) {
            deselectAll();
        }
    }

    public void toggleSelection(DataDef.Message message) {
        final Set<String> selectedItemIds = requireSelectedItemIds();

        if (selectedItemIds.contains(message.getId())) {
            selectedItemIds.remove(message.getId());
        } else {
            selectedItemIds.add(message.getId());
        }

        notifySelectionsChange();
    }

    public void selectAll() {
        final Set<String> itemIds = requireItems().stream().map(DataDef.Message::getId).collect(Collectors.toSet());
        getSelectedItemIds().setValue(itemIds);
    }

    public void deselectAll() {
        final Set<String> selectedItemIds = requireSelectedItemIds();

        if (!selectedItemIds.isEmpty()) {
            selectedItemIds.clear();
            notifySelectionsChange();
        }
    }

    public void removeSelectedItems() {
        final Set<String> selectedItemIds = requireSelectedItemIds();
        MessageLocalDataSource.getInstance().removeMessages(selectedItemIds);

        setViewType(ViewType.DEFAULT);
    }

    private void notifySelectionsChange() {
        final Set<String> selectedItemIds = requireSelectedItemIds();
        getSelectedItemIds().setValue(new HashSet<>(selectedItemIds));
    }

    private List<DataDef.Message> requireItems() {
        final List<DataDef.Message> items = getItems().getValue();
        assert items != null;
        return items;
    }

    private Set<String> requireSelectedItemIds() {
        final Set<String> selectedItemIds = getSelectedItemIds().getValue();
        assert selectedItemIds != null;
        return selectedItemIds;
    }

    public static final ViewModelInitializer<MessageListViewModel> initializer = new ViewModelInitializer<>(
            MessageListViewModel.class,
            creationExtras -> {
                final SavedStateHandle savedStateHandle = createSavedStateHandle(creationExtras);
                return new MessageListViewModel(savedStateHandle);
            }
    );
}
