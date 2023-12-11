package com.rationalowl.umsdemo.representation.ui.message;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.rationalowl.umsdemo.R;
import com.rationalowl.umsdemo.databinding.ActivityMessageViewBinding;
import com.rationalowl.umsdemo.databinding.DialogMessageDeliveryInfoBinding;
import com.rationalowl.umsdemo.domain.Config;
import com.rationalowl.umsdemo.domain.Message;
import com.rationalowl.umsdemo.domain.MessageReadReceiptService;
import com.rationalowl.umsdemo.domain.MessagesRepository;
import com.rationalowl.umsdemo.domain.User;
import com.rationalowl.umsdemo.domain.UserRepository;
import com.rationalowl.umsdemo.protocol.PushAppProto;
import com.rationalowl.umsdemo.protocol.UmsClient;
import com.rationalowl.umsdemo.protocol.UmsResult;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageViewActivity extends AppCompatActivity {
    private static final String TAG = "MessageViewActivity";

    public static final String BUNDLE_MESSAGE_KEY = "message";
    private static final String MESSAGE_CONTENT_CLIP_LABEL = "message body";

    private static final Map<String, byte[]> imageCache = new HashMap<>();

    private final MessagesRepository repository = MessagesRepository.getInstance();
    private Message message;

    private ActivityMessageViewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Bundle extras = getIntent().getExtras();
        assert extras != null;

        extras.setClassLoader(Message.class.getClassLoader());
        message = getIntent().getParcelableExtra(BUNDLE_MESSAGE_KEY);
        assert message != null;

        Log.d(TAG, "onCreate('" + message.getTitle() + "')");

        binding = ActivityMessageViewBinding.inflate(getLayoutInflater());
        binding.setLifecycleOwner(this);

        final SimpleDateFormat dateFormat = new SimpleDateFormat(getString(R.string.message_long_datetime_format), Locale.KOREAN);
        binding.setDateFormat(dateFormat);
        binding.setMessage(message);

        binding.buttonCopy.setOnClickListener(v -> copyToClipboard());
        binding.buttonDelete.setOnClickListener(v -> delete());
        binding.buttonInfo.setOnClickListener(v -> showInfo());

        if (message.getImageId() != null) {
            loadImage();
        }

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
        Log.d(TAG, "onStart('" + message.getTitle() + "')");
        MessageReadReceiptService.getInstance().markAsRead(message);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, "onNewIntent()");

        message = getIntent().getParcelableExtra(BUNDLE_MESSAGE_KEY);
        MessageReadReceiptService.getInstance().markAsRead(message);
    }

    private void loadImage() {
        final String imageId = message.getImageId();
        if (imageId == null) return;

        final byte[] cache = imageCache.get(imageId);

        if (cache != null) {
            showImage(cache);
            return;
        }

        final PushAppProto.PushAppImgDataReq request = new PushAppProto.PushAppImgDataReq();
        request.mAccountId = Config.getInstance().getUmsAccountId();
        request.mImgId = imageId;
        request.mMsgId = message.getId();

        UmsClient.getService().getImageData(request).enqueue(new Callback<PushAppProto.PushAppImgDataRes>() {
            @Override
            public void onResponse(@NonNull Call<PushAppProto.PushAppImgDataRes> call, @NonNull Response<PushAppProto.PushAppImgDataRes> response) {
                assert response.body() != null;

                final int resultCode = response.body().mResultCode;
                if (resultCode != UmsResult.RESULT_OK) return;

                final byte[] data = Base64.decode(response.body().mImgData, Base64.DEFAULT);
                imageCache.putIfAbsent(imageId, data);
                showImage(data);
            }

            @Override
            public void onFailure(@NonNull Call<PushAppProto.PushAppImgDataRes> call, @NonNull Throwable t) {
                Log.e(TAG, t.getMessage(), t);
            }
        });
    }

    private void showImage(byte[] data) {
        Glide.with(MessageViewActivity.this)
                .asBitmap()
                .load(data)
                .into(binding.imageview);

        binding.imageview.setVisibility(View.VISIBLE);
    }

    private void updateInfo(Runnable callback) {
        final long diffInMills = Math.abs(message.getSentAt().getTime() - new Date().getTime());
        final long diffDays = TimeUnit.DAYS.convert(diffInMills, TimeUnit.MILLISECONDS);

        if (diffDays >= 7) {
            callback.run();
            return;
        }

        final User user = UserRepository.getInstance().getUser();

        final PushAppProto.PushAppMsgInfoReq request = new PushAppProto.PushAppMsgInfoReq();
        request.mAccountId = Config.getInstance().getUmsAccountId();
        request.mPhoneNum = user.getPhoneNumber();
        request.mDeviceRegId = user.getRegId();
        request.mMsgId = message.getId();

        UmsClient.getService().getMessageInfo(request).enqueue(new Callback<PushAppProto.PushAppMsgInfoRes>() {
            @Override
            public void onResponse(@NonNull Call<PushAppProto.PushAppMsgInfoRes> call, @NonNull Response<PushAppProto.PushAppMsgInfoRes> response) {
                assert response.body() != null;

                final int resultCode = response.body().mResultCode;
                if (resultCode != UmsResult.RESULT_OK) return;

                final long alimTalkSentAtInMills = response.body().mAlimtalkSendTime;
                if (alimTalkSentAtInMills > 0) {
                    message.setAlimTalkSentAt(new Date(alimTalkSentAtInMills));
                }

                final long munjaSentAtInMills = response.body().mMunjaSendTime;
                if (munjaSentAtInMills > 0) {
                    message.setMunjaSentAt(new Date(munjaSentAtInMills));
                }

                message.setMunjaType(response.body().mMunjaType);

                repository.save();
                callback.run();
            }

            @Override
            public void onFailure(@NonNull Call<PushAppProto.PushAppMsgInfoRes> call, @NonNull Throwable t) {
                Log.e(TAG, t.getMessage(), t);

                repository.save();
                callback.run();
            }
        });
    }

    @Override
    public void onBackPressed() {
        openListActivity();
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            openListActivity();
            return false;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openListActivity() {
        if (!isTaskRoot()) return;

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
                    repository.removeMessage(message.getId());
                    openListActivity();
                    finish();
                })
                .show();
    }

    private void showInfo() {
        updateInfo(() -> {
            final LayoutInflater inflater = getLayoutInflater();
            final DialogMessageDeliveryInfoBinding binding = DialogMessageDeliveryInfoBinding.inflate(inflater);
            binding.setDateFormat(new SimpleDateFormat(getString(R.string.message_long_datetime_format), Locale.KOREAN));
            binding.setMessage(message);

            final View dialog = binding.getRoot();

            new MaterialAlertDialogBuilder(this)
                    .setTitle(R.string.message_delivery_info)
                    .setView(dialog)
                    .setPositiveButton(R.string.ok, null)
                    .show();
        });
    }
}
