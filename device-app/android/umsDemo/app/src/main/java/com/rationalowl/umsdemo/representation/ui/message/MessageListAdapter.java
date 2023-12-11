package com.rationalowl.umsdemo.representation.ui.message;

import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.rationalowl.umsdemo.R;
import com.rationalowl.umsdemo.databinding.CheckableMessageItemBinding;
import com.rationalowl.umsdemo.databinding.MessageItemBinding;
import com.rationalowl.umsdemo.domain.Message;
import com.rationalowl.umsdemo.representation.viewModel.MessageViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class MessageListAdapter extends ListAdapter<MessageViewModel, MessageListAdapter.ViewHolder> {
    private static final String TAG = "MessageListAdapter";

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ViewBinding binding;
        private MessageViewModel viewModel;

        public ViewHolder(ViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        protected void setViewModel(MessageViewModel viewModel) {
            this.viewModel = viewModel;

            final boolean isToday = DateUtils.isToday(viewModel.getMessage().getSentAt().getTime());
            final String dateTimeFormatString = itemView.getResources().getString(isToday ? R.string.message_short_time_format : R.string.message_short_date_format);
            final SimpleDateFormat dateTimeFormat = new SimpleDateFormat(dateTimeFormatString, Locale.KOREAN);

            if (binding instanceof MessageItemBinding) {
                ((MessageItemBinding) binding).setViewModel(viewModel);
                ((MessageItemBinding) binding).setDateFormat(dateTimeFormat);
            } else if (binding instanceof CheckableMessageItemBinding) {
                ((CheckableMessageItemBinding) binding).setViewModel(viewModel);
                ((CheckableMessageItemBinding) binding).setDateFormat(dateTimeFormat);
            }
        }
    }

    public interface OnViewTypeChangeListener {
        void onViewTypeChanged(MessageListViewType viewType);
    }

    public interface OnClickListener {
        void onClick(Message message);
    }

    public interface OnCheckedChangeListener {
        void onCheckedChanged(Message message, boolean isChecked);
    }

    protected MessageListAdapter() {
        super(DIFF_CALLBACK);
    }

    private MessageListViewType viewType = MessageListViewType.DEFAULT;

    private OnViewTypeChangeListener onViewTypeChangeListener;
    private OnClickListener onClickListener;
    private OnCheckedChangeListener onCheckedChangeListener;

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        final MessageViewModel viewModel = getItem(position);
        viewHolder.setViewModel(viewModel);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        final int layoutId = viewType == MessageListViewType.DELETE.ordinal() ? R.layout.checkable_message_item : R.layout.message_item;
        final ViewBinding binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), layoutId, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(binding);

        if (viewType == MessageListViewType.DELETE.ordinal()) {
            viewHolder.itemView.setOnClickListener(v -> {
                final int index = getItems().indexOf(viewHolder.viewModel);
                final boolean isChecked = !viewHolder.viewModel.isChecked().getValue();
                setItemChecked(index, isChecked);
            });

            viewHolder.itemView.setOnLongClickListener(v -> {
                final int index = getItems().indexOf(viewHolder.viewModel);
                final boolean isChecked = !viewHolder.viewModel.isChecked().getValue();
                setItemChecked(index, isChecked);
                return true;
            });
        } else {
            viewHolder.itemView.setOnClickListener(v -> {
                if (onClickListener != null) {
                    onClickListener.onClick(viewHolder.viewModel.getMessage());
                }
            });

            viewHolder.itemView.setOnLongClickListener(v -> {
                setViewType(MessageListViewType.DELETE);
                return true;
            });
        }

        return viewHolder;
    }

    public List<MessageViewModel> getItems() {
        return getCurrentList();
    }

    @Override
    public int getItemViewType(int position) {
        return viewType.ordinal();
    }

    public MessageListViewType getViewType() {
        return viewType;
    }

    public void setItemChecked(int position, boolean isChecked) {
        Log.d(TAG, "setItemChecked(" + position + ", " + isChecked + ")");

        final MessageViewModel viewModel = getItem(position);
        viewModel.setChecked(isChecked);

        if (onCheckedChangeListener != null) {
            onCheckedChangeListener.onCheckedChanged(viewModel.getMessage(), isChecked);
        }

        notifyItemChanged(position);
    }

    public void setViewType(MessageListViewType value) {
        Log.d(TAG, "setViewType(" + value.name() + ")");

        if (value == viewType) return;

        viewType = value;

        if (onViewTypeChangeListener != null) {
            onViewTypeChangeListener.onViewTypeChanged(viewType);
        }
    }

    public void setItems(@NonNull List<Message> items) {
        final String messageIds = items.stream().map(Message::getId).collect(Collectors.joining(", "));
        Log.d(TAG, "setItems([" + messageIds + "]");

        final List<MessageViewModel> viewModels = new ArrayList<>();

        for (final Message item : items) {
            viewModels.add(new MessageViewModel(item));
        }

        submitList(viewModels);
    }

    public void setOnClickListener(OnClickListener listener) {
        onClickListener = listener;
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        onCheckedChangeListener = listener;
    }

    public void setOnViewTypeChangeListener(OnViewTypeChangeListener listener) {
        onViewTypeChangeListener = listener;
    }

    public static final DiffUtil.ItemCallback<MessageViewModel> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<MessageViewModel>() {
                @Override
                public boolean areItemsTheSame(@NonNull MessageViewModel oldViewModel, @NonNull MessageViewModel newViewModel) {
                    return oldViewModel.getMessage().equals(newViewModel.getMessage());
                }

                @Override
                public boolean areContentsTheSame(@NonNull MessageViewModel oldViewModel, @NonNull MessageViewModel newViewModel) {
                    return oldViewModel.equals(newViewModel);
                }
            };
}
