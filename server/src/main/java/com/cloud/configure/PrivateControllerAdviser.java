package com.cloud.configure;

import com.cloud.exception.*;
import com.cloud.file.model.ErrorFileResponseInfo;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Created by micky on 11/22/16.
 */
@Log4j2
@ControllerAdvice
@RestController
@RequestMapping("/error")
public class PrivateControllerAdviser implements ErrorController {

    public ErrorFileResponseInfo errorHandler(Throwable e) {
        ErrorFileResponseInfo msg = new ErrorFileResponseInfo();
        log.error(e);
        msg.setError(e.getClass().getSimpleName());
        msg.setMessage(e.getMessage());
        return msg;
    }

    @ExceptionHandler(AccessViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorFileResponseInfo handleAccessViolationException(AccessViolationException e) {
        return errorHandler(e);
    }

    @ExceptionHandler(FileAlreadyUseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorFileResponseInfo handleFileAlreadyUseError(FileAlreadyUseException e) {
        return errorHandler(e);
    }

    @ExceptionHandler(FileExistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorFileResponseInfo handleFileExistError(FileExistException e) {
        return errorHandler(e);
    }

    @ExceptionHandler(FileNotExistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorFileResponseInfo handleFileNotExistError(FileNotExistException e) {
        return errorHandler(e);
    }

    @ExceptionHandler(LockException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorFileResponseInfo handleLockException(LockException e) {
        return errorHandler(e);
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }



}
