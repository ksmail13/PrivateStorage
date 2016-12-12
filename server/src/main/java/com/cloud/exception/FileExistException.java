package com.cloud.exception;

/**
 * Created by micky on 2016. 12. 13..
 */
public class FileExistException extends InvalidStateException {
    public FileExistException(String filePath) {
        super(String.format("%s is exist", filePath));
    }
}
