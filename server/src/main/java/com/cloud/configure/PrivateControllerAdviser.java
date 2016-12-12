package com.cloud.configure;

import com.cloud.exception.*;
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

    public ErrorMessage errorHandler(Throwable e) {
        ErrorMessage msg = new ErrorMessage();
        log.error(e);
        msg.setError(e.getClass().getSimpleName());
        msg.setMessage(e.getMessage());
        return msg;
    }

    @ExceptionHandler(AccessViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorMessage handleAccessViolationException(AccessViolationException e) {
        return errorHandler(e);
    }

    @ExceptionHandler(FileAlreadyUseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorMessage handleFileAlreadyUseError(FileAlreadyUseException e) {
        return errorHandler(e);
    }

    @ExceptionHandler(FileExistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorMessage handleFileExistError(FileExistException e) {
        return errorHandler(e);
    }

    @ExceptionHandler(FileNotExistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorMessage handleFileNotExistError(FileNotExistException e) {
        return errorHandler(e);
    }

    @ExceptionHandler(LockException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleLockException(LockException e) {
        return errorHandler(e);
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }



}
@Data
class ErrorMessage {
    private String error;
    private String message;
}
