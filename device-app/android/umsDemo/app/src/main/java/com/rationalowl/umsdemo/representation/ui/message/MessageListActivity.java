package com.rationalowl.umsdemo.representation.ui.message;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.rationalowl.umsdemo.R;
import com.rationalowl.umsdemo.databinding.ActivityMessageListBinding;
import com.rationalowl.umsdemo.domain.Message;
import com.rationalowl.umsdemo.domain.MessagesRepository;
import com.rationalowl.umsdemo.representation.ui.account.UserInfoActivity;
import com.rationalowl.umsdemo.representation.viewModel.MessageListViewModel;

import java.util.List;

import cn.nekocode.badge.BadgeDrawable;

public class MessageListActivity extends AppCompatActivity {
    private final static String TAG = "MessageListActivity";

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

        adapter = new MessageListAdapter();
        adapter.setOnViewTypeChangeListener(viewType -> {
            invalidateMenu();
            viewModel.setViewType(viewType);
        });
        adapter.setOnClickListener(item -> {
            final Intent intent = new Intent(this, MessageViewActivity.class);
            intent.putExtra(MessageViewActivity.BUNDLE_MESSAGE_KEY, item);
            startActivity(intent);
        });
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                binding.recyclerView.scrollToPosition(0);
            }
        });

        adapter.setOnCheckedChangeListener((message, isChecked) -> viewModel.setSelectedState(message.getId(), isChecked));

        binding.recyclerView.setAdapter(adapter);

        viewModel.getItems().observe(this, items -> {
            updateUnreadMessages();
            updateSelectAllButton();
        });

        viewModel.getSelectedItemIds().observe(this, checkedItems -> updateSelectAllButton());

        binding.buttonSelectAll.setOnClickListener(v -> selectAll());
        binding.buttonDelete.setOnClickListener(v -> delete());

        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
    }

    @Override
    public void onBackPressed() {
        if (adapter.getViewType() == MessageListViewType.DELETE) {
            adapter.setViewType(MessageListViewType.DEFAULT);
            return;
        }

        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (adapter.getViewType() == MessageListViewType.DEFAULT) {
            getMenuInflater().inflate(R.menu.menu_message_list, menu);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_delete) {
            adapter.setViewType(MessageListViewType.DELETE);
        } else if (item.getItemId() == R.id.menu_mark_all_as_read) {
            MessagesRepository.getInstance().setAllAsRead();
            return true;
        } else if (item.getItemId() == R.id.menu_delete_account) {
            final Intent intent = new Intent(this, UserInfoActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @BindingAdapter("items")
    public static void setItems(@NonNull RecyclerView recyclerView, @NonNull MutableLiveData<List<Message>> listItemViewModels) {
        Log.d(TAG, "setItems()");

        final MessageListAdapter adapter = (MessageListAdapter) recyclerView.getAdapter();
        final List<Message> newItems = listItemViewModels.getValue();
        assert adapter != null && newItems != null;

        adapter.setItems(newItems);
    }

    private void delete() {
        final List<String> selectedItemIds = viewModel.getSelectedItemIds().getValue();
        assert selectedItemIds != null;

        new MaterialAlertDialogBuilder(this)
                .setMessage(getString(R.string.delete_multiple_messages, selectedItemIds.size()))
                .setPositiveButton(R.string.delete, (dialogInterface, i) -> {
                    viewModel.removeSelectedItems();
                    adapter.setViewType(MessageListViewType.DEFAULT);
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private void selectAll() {
        final List<Message> items = viewModel.getItems().getValue();
        final List<String> selectedItemIds = viewModel.getSelectedItemIds().getValue();
        assert items != null && selectedItemIds != null;

        if (items.isEmpty()) return;

        final boolean select = selectedItemIds.size() < items.size();

        for (int i = 0; i < adapter.getItemCount(); i++) {
            adapter.setItemChecked(i, select);
        }
    }

    private void updateSelectAllButton() {
        final List<Message> items = viewModel.getItems().getValue();
        final List<String> selectedItemIds = viewModel.getSelectedItemIds().getValue();
        assert items != null && selectedItemIds != null;

        final boolean check = !items.isEmpty() && selectedItemIds.size() == items.size();
        ((MaterialButton) binding.buttonSelectAll).setIcon(AppCompatResources.getDrawable(this, check ? R.drawable.ic_baseline_check_circle_24 : R.drawable.ic_baseline_radio_button_unchecked_24));
    }

    private void updateUnreadMessages() {
        final int unreadCount = viewModel.getUnreadCount();

        if (unreadCount == 0) {
            binding.layoutUnread.setVisibility(View.GONE);
            return;
        }

        final int unreadColor = ContextCompat.getColor(this, R.color.unread);
        final BadgeDrawable badge;

        if (unreadCount < 100) {
            badge = new BadgeDrawable.Builder()
                    .type(BadgeDrawable.TYPE_NUMBER)
                    .badgeColor(unreadColor)
                    .number(unreadCount)
                    .build();
        } else {
            badge = new BadgeDrawable.Builder()
                    .type(BadgeDrawable.TYPE_ONLY_ONE_TEXT)
                    .badgeColor(unreadColor)
                    .text1("99+")
                    .build();
        }

        binding.imageviewBadge.setImageDrawable(badge);
        binding.layoutUnread.setVisibility(View.VISIBLE);
    }
}
