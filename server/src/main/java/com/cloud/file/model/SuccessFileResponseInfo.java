package com.cloud.file.model;

import lombok.Data;

/**
 * Created by micky on 2016. 12. 13..
 */
@Data
public class SuccessFileResponseInfo extends FileResponseInfo {

    private String workId;
    private String directory;
    private String filename;
    private FileRequestType type;
}
