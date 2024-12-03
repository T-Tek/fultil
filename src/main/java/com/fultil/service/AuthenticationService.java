package com.fultil.service;

import com.fultil.payload.request.ChangePasswordRequest;
import com.fultil.payload.request.LoginRequest;
import com.fultil.payload.request.UserRequest;
import com.fultil.payload.response.AuthenticationResponse;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public interface AuthenticationService {
    String register(UserRequest userRequest);
    AuthenticationResponse login(LoginRequest loginRequest);
    void activateAccount(String token) throws MessagingException;
    void changePassword(ChangePasswordRequest request);
}
