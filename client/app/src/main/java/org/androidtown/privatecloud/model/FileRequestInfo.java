package org.androidtown.privatecloud.model;

import lombok.Data;

/**
 * Created by neverstop on 2016-12-13.
 */

@Data
public class FileRequestInfo {
    private String directory;
    private String filename;
    private FileRequestType type;
}
