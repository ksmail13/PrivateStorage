package com.cloud.util.exception;

/**
 * Created by micky on 2016. 12. 11..
 */
public class AccessViolationException extends RuntimeException {

    public AccessViolationException(String filename) {
        super(String.format("%s is not allow access", filename));
    }
}
