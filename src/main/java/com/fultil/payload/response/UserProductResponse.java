package com.fultil.payload.response;

import com.fultil.entity.User;
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
