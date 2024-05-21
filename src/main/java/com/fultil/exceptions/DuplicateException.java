package com.fultil.exceptions;


import com.fultil.enums.ResponseCodeAndMessage;

public class DuplicateException extends RuntimeException{
    public DuplicateException(String message) {
        super(message);
    }
}

