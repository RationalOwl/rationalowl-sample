package com.rationalowl.umsdemo.data.datasource;

import android.util.Log;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public abstract class JsonLocalDataSource<T> {
    private static final String TAG = "JsonLocalDataSource";

    protected final ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    protected final Class<T> type;
    protected final File file;

    protected JsonLocalDataSource(Class<T> type, String filename) {
        this.type = type;
        final String directory = LocalDataSourceManager.getDirectory().getAbsolutePath();
        file = new File(directory, filename);
    }

    protected T getValue() {
        T value = null;

        try {
            if (!file.exists()) {
                return null;
            }

            value = objectMapper.readValue(file, type);
        } catch (IOException e) {
            Log.e(TAG, "An error occurred while reading " + file.getPath(), e);
        }

        return value;
    }

    protected void setValue(T value) {
        try {
            objectMapper.writeValue(file, value);
        } catch (IOException e) {
            Log.e(TAG, "An error occurred while writing " + file.getPath(), e);
        }
    }

    public boolean delete() {
        if (!file.exists()) {
            return false;
        }

        return file.delete();
    }
}
