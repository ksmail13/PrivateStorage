package org.androidtown.privatecloud.model;

import lombok.Data;

/**
 * Created by neverstop on 2016-12-13.
 */

@Data
public class FileResponseInfo {
    private String workId;
    private String directory;
    private FileRequestType type;
    private String error;
    private String message;
    private String filename;
}
