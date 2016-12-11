package com.cloud.file.service;

import com.cloud.file.model.FileInfo;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * File manage service
 * Created by micky on 2016. 12. 7..
 */
public interface FileService {


    /**
     * get file list that updated after timestamp
     * @return
     * @param subPath
     */
    List<FileInfo> getFileList(String subPath) throws FileNotFoundException;

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
