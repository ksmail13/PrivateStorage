package com.cloud.exception;

/**
 * Created by micky on 2016. 12. 12..
 */
public class FileAlreadyUseException extends InvalidStateException {
    public FileAlreadyUseException(String fileName) {
        super(String.format("%s already used", fileName));
    }
}
