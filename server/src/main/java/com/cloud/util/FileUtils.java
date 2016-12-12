package com.cloud.util;

import com.cloud.file.model.FileInfoBuilder;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;

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

    public static File createDirectory(String syncDirectory) {
        File syncDir = new File(syncDirectory);
        if(!syncDir.exists()) {
            if (!syncDir.mkdirs()) {
                throw new IllegalStateException("Couldn't create sync directory "+syncDirectory);
            }
        }
        return syncDir;
    }

    public static void toHidden(File f) {
        try {
            Files.setAttribute(f.toPath(), "dos:hidden", true, LinkOption.NOFOLLOW_LINKS);
        } catch (IOException | UnsupportedOperationException e ) {
            e.printStackTrace();
        }
    }
}
