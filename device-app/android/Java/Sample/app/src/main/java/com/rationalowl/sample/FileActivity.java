package com.rationalowl.sample;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.RequiresApi;

import com.rationalowl.minerva.client.android.MinervaManager;
import com.rationalowl.minerva.client.android.StorageTransferListener;
import com.rationalowl.minerva.client.android.util.Logger;

import java.io.File;
import java.nio.file.Path;

public class FileActivity extends Activity implements OnClickListener, StorageTransferListener {
    
    private static final String TAG = "FileActivity";

    private static final int MEDIA_PICKER_SELECT = 1;

    
    RadioGroup mSCRg;
    RadioButton mAcceptRb;
    RadioButton mRejectRb;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);
        Button uploadBtn = (Button) findViewById(R.id.uploadBtn);
        Button downloadBtn = (Button) findViewById(R.id.downloadBtn);
        uploadBtn.setOnClickListener(this);
        downloadBtn.setOnClickListener(this);
    }

    
    @Override
    protected void onDestroy() {
        //Logger.debug(TAG, "onDestroy enter");
        super.onDestroy();
        
    }


    @Override
    protected void onStart() {
        //Logger.debug(TAG, "onStart() enter");
        super.onStart();
    }
    
    
    @Override
    protected void onResume() {
        //Logger.debug(TAG, "onResume enter");
        super.onResume();

        //set register callback listener
        MinervaManager minMgr = MinervaManager.getInstance();
        minMgr.setStorageTransferListener(this);
    }    

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onClick(View v) {

        int resId = v.getId();

        if(resId == R.id.uploadBtn) {
            Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickIntent.setType("image/* video/*");
            // Provide read access to files and sub-directories in the user-selected directory.
            pickIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(pickIntent, MEDIA_PICKER_SELECT);
        }
        else if(resId == R.id.downloadBtn) {
            // file path which should download
            String stroageFilePath = "/my1.png";

            // local download dir path
            File downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            Path dirPath = downloadDir.toPath();

            // download file to new name in local
            // just same as storage file name
            String saveDownloadFileName = "my1.png";

            // call download API
            MinervaManager mgr = MinervaManager.getInstance();
            mgr.downloadFile("211.239.150.124", stroageFilePath, dirPath, saveDownloadFileName);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            // upload file path
            Uri androidUri = data.getData();
            String filePath = getFilePathFromContentUri(androidUri, getContentResolver());
            File file = new File(filePath);
            Path path = file.toPath();

            // upload storage directory path
            // sample app just upload to the storage root directory
            String storageDirPath = "/";

            // call upload API
            MinervaManager mgr = MinervaManager.getInstance();
            mgr.uploadFile("211.239.150.124", path, storageDirPath);
        }
    }

    private static String getFilePathFromContentUri(Uri contentUri, ContentResolver contentResolver) {
        String filePath;
        String[] filePathColumn = {MediaStore.MediaColumns.DATA};

        Cursor cursor = contentResolver.query(contentUri, filePathColumn, null, null, null);

        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        filePath = cursor.getString(columnIndex);
        cursor.close();
        return filePath;
    }

    @Override
    public void onUploadProgress(Path filePath, int percent, long uploadSize, long totalSize) {
        Logger.test(TAG, "onUploadProgress...");
        Logger.test(TAG, "filePath:" + filePath.toString() + " uploading(" + uploadSize + "/" + totalSize + ")" + percent + "%");
    }

    @Override
    public void onUploadResult(Path filePath, int resultCode, String resultMsg) {
        Logger.test(TAG, "onUploadResult");
        Logger.test(TAG, "filePath:" + filePath.toString() + " upload result:" + resultMsg);
    }

    @Override
    public void onDownloadProgress(Path filePath, int percent, long downloadSize, long totalSize) {
        Logger.test(TAG, "onDownloadProgress...");
        Logger.test(TAG, "filePath:" + filePath.toString() + " downloading(" + downloadSize + "/" + totalSize + ")" + percent + "%");
    }

    @Override
    public void onDownloadResult(Path filePath, int resultCode, String resultMsg) {
        Logger.test(TAG, "onDownloadResult");
        Logger.test(TAG, "filePath:" + filePath.toString() + " download result:" + resultMsg);
    }

}