package org.androidtown.privatecloud.util;

/**
 * Created by neverstop on 2016-12-11.
 */
public class HttpResponseException extends RuntimeException {
    private int responseCode;

    public HttpResponseException(int responseCode, String message) {
        super(message);
        this.responseCode = responseCode;
    }
}
