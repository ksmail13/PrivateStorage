package com.cloud.file.model;

import lombok.Data;

/**
 * Created by micky on 2016. 12. 12..
 */
@Data
public class FileResponseInfo {
    private String workId;
    private String directory;
    private String filename;
    private FileRequestType type;
}
