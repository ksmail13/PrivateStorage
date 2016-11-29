package com.cloud.configure;

import com.cloud.util.UserNotFoundException;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.XSlf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Created by micky on 11/22/16.
 */
@Log4j2
@RestControllerAdvice
public class CloudControllerAdviser {

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleUserNotFoundException(Exception e) {
        log.error(e);
        return e.getMessage();
    }
}
