package com.fultil.exceptions;

import com.fultil.enums.ResponseCodeAndMessage;

public class IncorrectPasswordException extends RuntimeException {

    public IncorrectPasswordException(ResponseCodeAndMessage responseCodeAndMessage, String message) {
        super(message);
    }
}