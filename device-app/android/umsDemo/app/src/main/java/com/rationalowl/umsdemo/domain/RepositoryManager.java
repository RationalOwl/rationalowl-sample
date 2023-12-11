package com.rationalowl.umsdemo.domain;

import java.io.File;

public class RepositoryManager {
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
