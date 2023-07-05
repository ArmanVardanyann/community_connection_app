package com.community.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {

    private static final String ERROR_MESSAGE = "resourceNotFound";

    public NotFoundException() {
        super(ERROR_MESSAGE);
    }
}
