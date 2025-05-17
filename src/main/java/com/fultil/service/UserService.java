package com.fultil.service;

import com.fultil.payload.request.LoginRequest;
import com.fultil.payload.request.UserRequest;
import com.fultil.payload.response.AuthenticationResponse;
import com.fultil.payload.response.UserResponse;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    String becomeVendor();
    UserResponse getUserById(Long id);

}
