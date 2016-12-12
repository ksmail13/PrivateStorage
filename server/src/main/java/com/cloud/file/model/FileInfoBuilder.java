package com.cloud.file.model;

import com.cloud.file.FileType;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by micky on 2016. 12. 11..
 */
public class FileInfoBuilder {
    private String innerFullPath;
    private String name;
    private FileType type;
    private String fullPath;
    private static ConcurrentHashMap<String, FileInfo> fileInfoPool = new ConcurrentHashMap<>();

    public static FileInfo find(String fullPath) {
        return fileInfoPool.get(fullPath);
    }

    public FileInfoBuilder setInnerFullPath(String innerFullPath) {
        this.innerFullPath = innerFullPath;
        return this;
    }

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

    public FileInfo build() {
        FileInfo info = null;
        if(fileInfoPool.containsKey(fullPath)) {
            info = fileInfoPool.get(fullPath);
        } else {
            FileInfo newFile = FileInfo.createInstance(innerFullPath, fullPath, name, type);
            fileInfoPool.put(fullPath, newFile);
            info = newFile;
        }

        setType(null).setInnerFullPath(null).setType(null).setFullPath(null);
        return info;
    }
}
