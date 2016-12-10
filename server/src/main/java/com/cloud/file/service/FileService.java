package com.cloud.file.service;

import com.cloud.file.model.FileInfo;

import java.util.List;

/**
 * File manage service
 * Created by micky on 2016. 12. 7..
 */
public interface FileService {


    /**
     * get file list that updated after timestamp
     * @return
     */
    List<FileInfo> getFileList();

    /**
     * create new file
     * @param file
     * @return
     */
    FileInfo uploadFile(FileInfo file);

    /**
     * update file or delete file
     * @param file
     * @return
     */
    FileInfo updateFile(FileInfo file);
}
