package com.fultil.payload.response;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Response {
    private int statusCode;
    private HttpStatus status;
    private LocalDateTime timestamp;
    private String path;
    private Object data;
}
