package com.cloud.exception;

/**
 * Created by micky on 2016. 12. 13..
 */
public class LockException extends InvalidStateException {
    public LockException(String id) {
        super(String.format("%s is not valid", id));
    }
}
