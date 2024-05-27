package com.rationalowl.umsdemo.presentation.message.view;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BindingAdapter;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.rationalowl.umsdemo.R;
import com.rationalowl.umsdemo.data.DataDef;
import com.rationalowl.umsdemo.data.repository.MessageRepository;
import com.rationalowl.umsdemo.databinding.ActivityMessageListBinding;
import com.rationalowl.umsdemo.presentation.account.view.UserInfoActivity;
import com.rationalowl.umsdemo.presentation.message.viewmodel.MessageListViewModel;

import java.util.List;
import java.util.Set;

public class MessageListActivity extends AppCompatActivity {
    private ActivityMessageListBinding binding;
    private MessageListAdapter adapter;
    private MessageListViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this, ViewModelProvider.Factory.from(MessageListViewModel.initializer)).get(MessageListViewModel.class);

        binding = ActivityMessageListBinding.inflate(getLayoutInflater());
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        initRecyclerView();

        viewModel.getViewType().observe(this, v -> invalidateMenu());

        binding.buttonDelete.setOnClickListener(v -> delete());

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (viewModel.getViewType().getValue() != MessageListViewModel.ViewType.DELETE) {
                    finish();
                    return;
                }

                viewModel.setViewType(MessageListViewModel.ViewType.DEFAULT);
            }
        });

        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
    }

    private void initRecyclerView() {
        adapter = new MessageListAdapter();
        adapter.setOnClickListener(this::showMessage);

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                binding.recyclerView.scrollToPosition(0);
            }
        });

        adapter.setOnClickListener(message -> {
            if (viewModel.getViewType().getValue() == MessageListViewModel.ViewType.DEFAULT) {
                showMessage(message);
            } else {
                viewModel.toggleSelection(message);
            }
        });

        adapter.setOnLongClickListener(message -> {
            if (viewModel.getViewType().getValue() == MessageListViewModel.ViewType.DEFAULT) {
                viewModel.setViewType(MessageListViewModel.ViewType.DELETE);
            } else {
                viewModel.toggleSelection(message);
            }
        });

        binding.recyclerView.setAdapter(adapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding.recyclerView.setAdapter(null);
        binding = null;
        adapter = null;
        viewModel = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (viewModel.getViewType().getValue() == MessageListViewModel.ViewType.DEFAULT) {
            getMenuInflater().inflate(R.menu.menu_message_list, menu);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_delete) {
            viewModel.setViewType(MessageListViewModel.ViewType.DELETE);
        } else if (item.getItemId() == R.id.menu_mark_all_as_read) {
            MessageRepository.getInstance().setAllAsRead();
            return true;
        } else if (item.getItemId() == R.id.menu_delete_account) {
            final Intent intent = new Intent(this, UserInfoActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @BindingAdapter("viewType")
    public static void setViewType(@NonNull RecyclerView recyclerView, @NonNull MutableLiveData<MessageListViewModel.ViewType> viewType) {
        final MessageListAdapter adapter = (MessageListAdapter) recyclerView.getAdapter();
        assert adapter != null && viewType.getValue() != null;

        adapter.setViewType(viewType.getValue());
    }

    @BindingAdapter("items")
    public static void setItems(@NonNull RecyclerView recyclerView, @NonNull MutableLiveData<List<DataDef.Message>> items) {
        final MessageListAdapter adapter = (MessageListAdapter) recyclerView.getAdapter();
        assert adapter != null && items.getValue() != null;

        adapter.setItems(items.getValue());
    }

    @BindingAdapter("selectedItemIds")
    public static void setSelectedItemIds(@NonNull RecyclerView recyclerView, @NonNull MutableLiveData<Set<String>> itemIds) {
        final MessageListAdapter adapter = (MessageListAdapter) recyclerView.getAdapter();
        assert adapter != null && itemIds.getValue() != null;

        adapter.setSelectedItemIds(itemIds.getValue());
    }

    @BindingAdapter("icon")
    public static void setIcon(Button button, Drawable drawable) {
        ((MaterialButton) button).setIcon(drawable);
    }

    private void delete() {
        final Set<String> selectedItemIds = viewModel.getSelectedItemIds().getValue();
        assert selectedItemIds != null;

        new MaterialAlertDialogBuilder(this)
                .setMessage(getString(R.string.delete_multiple_messages, selectedItemIds.size()))
                .setPositiveButton(R.string.delete, (d, i) -> viewModel.removeSelectedItems())
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private void showMessage(DataDef.Message message) {
        final Intent intent = new Intent(this, MessageReadActivity.class);
        intent.putExtra(MessageReadActivity.BUNDLE_MESSAGE_KEY, message);
        startActivity(intent);

        MessageRepository.getInstance().setAsRead(message);
    }
}
