package com.rationalowl.umsdemo.domain;

import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public abstract class JsonRepository<T> {
    protected final ObjectMapper objectMapper = new ObjectMapper();
    protected final Class<T> type;
    protected final File file;

    protected JsonRepository(Class<T> type, String filename) {
        this.type = type;
        final String directory = RepositoryManager.getDirectory().getAbsolutePath();
        file = new File(directory, filename);
    }

    protected T getValue() {
        T value = null;

        try {
            if (!file.exists()) {
                Log.d("JsonRepository", "exists: " + file.exists());
                return null;
            }

            Log.d("JsonRepository", "exists2: " + new File(file.getPath()).exists());

            value = objectMapper.readValue(file, type);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return value;
    }

    protected void setValue(T value) {
        try {
            objectMapper.writeValue(file, value);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean delete() {
        return file.delete();
    }
}
