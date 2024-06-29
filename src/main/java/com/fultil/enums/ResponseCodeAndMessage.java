package com.fultil.enums;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum ResponseCodeAndMessage {
    SUCCESS(HttpStatus.OK),
    BAD_REQUEST( HttpStatus.BAD_REQUEST),
    NOT_FOUND( HttpStatus.NOT_FOUND);

    public final HttpStatus status;

}