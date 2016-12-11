package com.cloud.file.model;

import com.cloud.file.FileType;
import lombok.NonNull;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by micky on 2016. 12. 11..
 */
public class FileInfoBuilder {
    private String fullPath;
    private String name;
    private FileType type;
    private String thumbnail;

    private static ConcurrentHashMap<String, FileInfo> fileInfoPool = new ConcurrentHashMap<>();


    public FileInfoBuilder setFullPath(String fullPath) {
        this.fullPath = fullPath;
        return this;
    }

    public FileInfoBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public FileInfoBuilder setType(FileType type) {
        this.type = type;
        return this;
    }

    public FileInfoBuilder setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
        return this;
    }

    public FileInfo build() {
        FileInfo info = null;
        if(fileInfoPool.containsKey(fullPath)) {
            info = fileInfoPool.get(fullPath);
        } else {
            FileInfo newFile = new FileInfo(fullPath, name, type, thumbnail);
            fileInfoPool.put(fullPath, newFile);
            info = newFile;
        }

        setThumbnail(null).setType(null).setFullPath(null).setType(null);
        return info;
    }
}
