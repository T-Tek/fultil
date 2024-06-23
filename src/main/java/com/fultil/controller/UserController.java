package com.fultil.controller;

import com.fultil.enums.ResponseCodeAndMessage;
import com.fultil.payload.response.Response;
import com.fultil.service.UserService;
import com.fultil.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/vendor")
    public Response becomeVendor(){
        String message = userService.becomeVendor();
        return UserUtils.generateResponse(ResponseCodeAndMessage.SUCCESS, message);
    }
}
