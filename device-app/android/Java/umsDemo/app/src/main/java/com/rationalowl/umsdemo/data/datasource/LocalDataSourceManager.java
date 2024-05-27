package com.rationalowl.umsdemo.data.datasource;

import java.io.File;

public class LocalDataSourceManager {
    private static File directory;

    public static synchronized File getDirectory() {
        if (directory == null) {
            throw new IllegalStateException("Directory not set. Call setDirectory() before getDirectory().");
        }

        return directory;
    }

    public static synchronized void setDirectory(File dir) {
        directory = dir;
    }
}
