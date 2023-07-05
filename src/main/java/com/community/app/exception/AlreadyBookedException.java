package com.community.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AlreadyBookedException extends RuntimeException {
    private static final String ERROR_MESSAGE = "slotAlreadyBooked";

    public AlreadyBookedException() {
        super(ERROR_MESSAGE);
    }
}
