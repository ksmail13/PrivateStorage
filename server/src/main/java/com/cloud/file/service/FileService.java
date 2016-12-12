package com.cloud.file.service;

import com.cloud.file.model.FileRequestInfo;
import com.cloud.file.model.FileResponseInfo;
import com.cloud.file.model.PublicFileInfo;

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
    List<PublicFileInfo> getFileList(String subPath) throws FileNotFoundException;

    /**
     * create new file
     * @param file
     * @return
     */
    FileResponseInfo uploadFile(FileRequestInfo file);

    /**
     * update file or delete file
     * @param file
     * @return
     */
    FileResponseInfo updateFile(FileRequestInfo file);


    void complete(String id);

    FileResponseInfo downloadFile(String path);

    List<FileResponseInfo> multiUpdateFile(List<FileRequestInfo> request);
}
