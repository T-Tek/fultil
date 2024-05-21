package com.fultil.payload.response;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Response {
    private String responseCode;
    private String message;
    private Object data;
}