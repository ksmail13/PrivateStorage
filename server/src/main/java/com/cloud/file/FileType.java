package com.cloud.file;

import java.io.File;

/**
 * Created by micky on 11/27/16.
 */
public enum FileType {
    DIRECTORY, FILE;

    public static FileType fileType(File f) {
        return f.isDirectory()?DIRECTORY:FILE;
    }
}
