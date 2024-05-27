package com.rationalowl.umsdemo.presentation.message.view;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BindingAdapter;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.rationalowl.umsdemo.R;
import com.rationalowl.umsdemo.data.DataCallback;
import com.rationalowl.umsdemo.data.DataDef;
import com.rationalowl.umsdemo.data.datasource.MessageLocalDataSource;
import com.rationalowl.umsdemo.data.repository.MessageRepository;
import com.rationalowl.umsdemo.databinding.ActivityMessageReadBinding;
import com.rationalowl.umsdemo.databinding.DialogMessageDeliveryInfoBinding;
import com.rationalowl.umsdemo.presentation.message.viewmodel.MessageReadViewModel;
import com.rationalowl.umsdemo.presentation.message.viewmodel.MessageReadViewModelFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class MessageReadActivity extends AppCompatActivity {
    public static final String BUNDLE_MESSAGE_KEY = "message";
    private static final String MESSAGE_CONTENT_CLIP_LABEL = "message body";

    private DataDef.Message message;

    private ActivityMessageReadBinding binding;
    private MessageReadViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        message = unparcelMessage();
        viewModel = new ViewModelProvider(this, new MessageReadViewModelFactory(message)).get(MessageReadViewModel.class);

        binding = ActivityMessageReadBinding.inflate(getLayoutInflater());
        binding.setLifecycleOwner(this);

        final SimpleDateFormat dateFormat = new SimpleDateFormat(getString(R.string.message_long_datetime_format), Locale.KOREAN);
        binding.setDateFormat(dateFormat);
        binding.setViewModel(viewModel);

        binding.buttonCopy.setOnClickListener(v -> copyToClipboard());
        binding.buttonDelete.setOnClickListener(v -> delete());
        binding.buttonInfo.setOnClickListener(v -> showInfo());

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                returnToList();
            }
        });

        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(dateFormat.format(message.getReceivedAt()));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        MessageRepository.getInstance().setAsRead(message);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        message = null;
        binding = null;
        viewModel = null;
    }

    private DataDef.Message unparcelMessage() {
        final Bundle extras = getIntent().getExtras();
        assert extras != null;

        extras.setClassLoader(DataDef.Message.class.getClassLoader());
        return getIntent().getParcelableExtra(BUNDLE_MESSAGE_KEY);
    }

    @BindingAdapter("data")
    public static void setData(ImageView imageView, byte[] data) {
        if (data == null) return;
        final Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        imageView.setImageBitmap(bitmap);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            returnToList();
            return false;
        }

        return super.onOptionsItemSelected(item);
    }

    private void returnToList() {
        if (!isTaskRoot()) {
            finish();
            return;
        }

        final Intent intent = new Intent(this, MessageListActivity.class);
        startActivity(intent);
    }

    private void copyToClipboard() {
        final ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        final ClipData clip = ClipData.newPlainText(MESSAGE_CONTENT_CLIP_LABEL, message.getBody());
        clipboard.setPrimaryClip(clip);

        Toast.makeText(this, getString(R.string.copied_to_clipboard), Toast.LENGTH_SHORT).show();
    }

    private void delete() {
        new MaterialAlertDialogBuilder(this)
                .setMessage(R.string.delete_message)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.delete, (dialogInterface, i) -> {
                    MessageLocalDataSource.getInstance().removeMessage(message.getId());
                    returnToList();
                    finish();
                })
                .show();
    }

    private void showInfo() {
        MessageRepository.getInstance().getDeliveryInfo(message, new DataCallback<DataDef.Message>() {
            @Override
            public void onResponse(DataDef.Message message) {
                runOnUiThread(() -> {
                    final LayoutInflater inflater = getLayoutInflater();
                    final DialogMessageDeliveryInfoBinding binding = DialogMessageDeliveryInfoBinding.inflate(inflater);
                    binding.setDateFormat(new SimpleDateFormat(getString(R.string.message_long_datetime_format), Locale.KOREAN));
                    binding.setMessage(message);

                    final View dialog = binding.getRoot();

                    new MaterialAlertDialogBuilder(MessageReadActivity.this)
                            .setTitle(R.string.message_delivery_info)
                            .setView(dialog)
                            .setPositiveButton(R.string.ok, null)
                            .show();
                });
            }

            @Override
            public void onFailure(IOException e) {
                new MaterialAlertDialogBuilder(MessageReadActivity.this)
                        .setTitle(R.string.message_delivery_info)
                        .setMessage(e.getMessage())
                        .setPositiveButton(R.string.ok, null)
                        .show();
            }
        });
    }
}
