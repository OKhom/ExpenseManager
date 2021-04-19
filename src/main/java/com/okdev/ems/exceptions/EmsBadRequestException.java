package com.okdev.ems.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EmsBadRequestException extends RuntimeException {

    public EmsBadRequestException(String message) {
        super(message);
    }
}
