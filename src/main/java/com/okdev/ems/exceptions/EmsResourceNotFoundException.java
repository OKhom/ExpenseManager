package com.okdev.ems.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EmsResourceNotFoundException extends RuntimeException {

    public EmsResourceNotFoundException(String message) {
        super(message);
    }
}
