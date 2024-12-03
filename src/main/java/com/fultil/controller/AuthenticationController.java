package com.fultil.controller;

import com.fultil.enums.ResponseCodeAndMessage;
import com.fultil.payload.request.ChangePasswordRequest;
import com.fultil.payload.request.LoginRequest;
import com.fultil.payload.request.UserRequest;
import com.fultil.payload.response.AuthenticationResponse;
import com.fultil.payload.response.Response;
import com.fultil.service.AuthenticationService;
import com.fultil.utils.UserUtils;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<Response> register(@RequestBody @Valid UserRequest userRequest){
        String data = authenticationService.register(userRequest);
        return UserUtils.getResponse(ResponseCodeAndMessage.SUCCESS, data);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public Response login(@RequestBody @Valid LoginRequest loginRequest) {
        AuthenticationResponse data = authenticationService.login(loginRequest);
        return UserUtils.generateResponse(ResponseCodeAndMessage.SUCCESS, data);
    }

    @GetMapping("/activate-account")
    @ResponseStatus(HttpStatus.OK)
    public Response confirm(@RequestParam String activationToken) throws MessagingException {
        authenticationService.activateAccount(activationToken);
        return UserUtils.generateResponse(ResponseCodeAndMessage.SUCCESS, "Account activated successfully");
    }

    @PatchMapping("/change-password")
    @ResponseStatus(HttpStatus.OK)
    public Response changePassword(@RequestBody @Valid ChangePasswordRequest request) {
        authenticationService.changePassword(request);
        return UserUtils.generateResponse(ResponseCodeAndMessage.SUCCESS, "Password changed successfully");

    }
}
