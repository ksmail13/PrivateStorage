package com.cloud.file.model;

import lombok.Data;

/**
 * Created by micky on 2016. 12. 12..
 */
@Data
public class FileRequestInfo {
    private String directory;
    private String filename;
    private FileRequestType type;
}
