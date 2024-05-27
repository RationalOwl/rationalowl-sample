package com.rationalowl.umsdemo.presentation.message.view;

import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.rationalowl.umsdemo.R;
import com.rationalowl.umsdemo.data.DataDef;
import com.rationalowl.umsdemo.databinding.CheckableMessageItemBinding;
import com.rationalowl.umsdemo.databinding.MessageItemBinding;
import com.rationalowl.umsdemo.presentation.message.viewmodel.MessageListViewModel;
import com.rationalowl.umsdemo.presentation.message.viewmodel.MessageViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class MessageListAdapter extends ListAdapter<MessageViewModel, MessageListAdapter.ViewHolder> {
    public interface OnClickListener {
        void onClick(DataDef.Message message);
    }

    public interface OnLongClickListener {
        void onLongClick(DataDef.Message message);
    }

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

    private MessageListViewModel.ViewType viewType = MessageListViewModel.ViewType.DEFAULT;
    private final Map<DataDef.Message, Integer> itemIndexMappings = new HashMap<>();

    private OnClickListener onClickListener;
    private OnLongClickListener onLongClickListener;

    protected MessageListAdapter() {
        super(DIFF_CALLBACK);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        final MessageViewModel viewModel = getItem(position);
        viewHolder.setViewModel(viewModel);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        final int layoutId = viewType == MessageListViewModel.ViewType.DELETE.ordinal() ? R.layout.checkable_message_item : R.layout.message_item;
        final ViewBinding binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), layoutId, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(binding);

        viewHolder.itemView.setOnClickListener(v -> {
            if (onClickListener != null) {
                final DataDef.Message message = viewHolder.viewModel.getMessage();
                onClickListener.onClick(message);
            }
        });

        viewHolder.itemView.setOnLongClickListener(v -> {
            if (onLongClickListener != null) {
                final DataDef.Message message = viewHolder.viewModel.getMessage();
                onLongClickListener.onLongClick(message);
            }

            return true;
        });

        return viewHolder;
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        itemIndexMappings.clear();
        onClickListener = null;
        onLongClickListener = null;
    }

    @Override
    public int getItemViewType(int position) {
        return viewType.ordinal();
    }

    public void setViewType(MessageListViewModel.ViewType viewType) {
        this.viewType = viewType;
    }

    public void setItems(List<DataDef.Message> items) {
        itemIndexMappings.clear();

        final List<MessageViewModel> viewModels = new ArrayList<>();

        for (int i = 0; i < items.size(); ++i) {
            final DataDef.Message message = items.get(i);
            viewModels.add(new MessageViewModel(message));
            itemIndexMappings.put(message, i);
        }

        submitList(viewModels);
    }

    public void setSelectedItemIds(Set<String> itemIds) {
        for (Map.Entry<DataDef.Message, Integer> entry : itemIndexMappings.entrySet()) {
            final DataDef.Message message = entry.getKey();
            final int index = entry.getValue();

            final MessageViewModel viewModel = getItem(index);
            boolean checked = itemIds.contains(message.getId());
            if (viewModel.isChecked().getValue() == Boolean.valueOf(checked)) continue;

            viewModel.setChecked(checked);
            notifyItemChanged(index);
        }
    }

    public void setOnClickListener(OnClickListener listener) {
        onClickListener = listener;
    }

    public void setOnLongClickListener(OnLongClickListener listener) {
        onLongClickListener = listener;
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
