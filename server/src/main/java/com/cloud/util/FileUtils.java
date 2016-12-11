package com.cloud.util;

import java.io.File;
import java.io.IOException;

/**
 * Created by micky on 2016. 12. 11..
 */
public class FileUtils {
    private FileUtils() {}

    public static String getCanonicalPath(File f) {
        String path = null;
        try {
            path = f.getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return path;
    }
}
