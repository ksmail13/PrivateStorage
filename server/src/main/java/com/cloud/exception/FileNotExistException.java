package com.cloud.exception;

/**
 * Created by micky on 2016. 12. 12..
 */
public class FileNotExistException extends InvalidStateException {
    public FileNotExistException(String fileName) {
        super(String.format("%s is not exist.", fileName));
    }
}
