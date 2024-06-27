package com.fultil.payload.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProductResponse{
    private String userEmail;
    private Response response;
}
