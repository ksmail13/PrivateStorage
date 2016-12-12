package com.cloud.file.model;

import lombok.Data;

/**
 * Created by micky on 2016. 12. 13..
 */
@Data
public class ErrorFileResponseInfo extends FileResponseInfo {

    private String error;
    private String message;
    private String filename;
}
