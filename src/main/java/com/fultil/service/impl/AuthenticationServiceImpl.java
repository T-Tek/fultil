package com.fultil.service.impl;

import com.fultil.email.EmailService;
import com.fultil.entity.Role;
import com.fultil.entity.AccountActivationToken;
import com.fultil.entity.User;
import com.fultil.enums.EmailTemplateName;
import com.fultil.enums.ResponseCodeAndMessage;
import com.fultil.exceptions.DuplicateException;
import com.fultil.exceptions.IncorrectPasswordException;
import com.fultil.exceptions.ResourceNotFoundException;
import com.fultil.payload.request.ChangePasswordRequest;
import com.fultil.payload.request.LoginRequest;
import com.fultil.payload.request.UserRequest;
import com.fultil.payload.response.AuthenticationResponse;
import com.fultil.repository.RoleRepository;
import com.fultil.repository.TokenRepository;
import com.fultil.repository.UserRepository;
import com.fultil.security.JwtService;
import com.fultil.service.AuthenticationService;
import com.fultil.utils.UserUtils;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final EmailService emailService;

    @Value("${activationUrl}")
    private String activationUrl;

    @Override
    public String register(UserRequest userRequest) throws MessagingException {
        log.info("Request to create an account with email: " + userRequest.getEmail());
        validateUserInput(userRequest);
        try {
            Role role = roleRepository.findByName("USER")
                    .orElseThrow(() -> new ResourceNotFoundException("User not found for role "));
            User user = User.builder()
                    .firstName(userRequest.getFirstName())
                    .lastName(userRequest.getLastName())
                    .email(userRequest.getEmail())
                    .password(passwordEncoder.encode(userRequest.getPassword()))
                    .phoneNumber(userRequest.getPhoneNumber())
                    .roles(List.of(role))
                    .uniqueId(UserUtils.generateUniqueId())
                    .accountLocked(false)
                    .enabled(false)
                    .build();

            userRepository.save(user);
            sendValidationEmail(user);
            log.info("Account created with email: {}", userRequest.getEmail());
            return "Account created, activation email has been sent to " + userRequest.getEmail();
        } catch (Exception ex) {
            log.error("Error occurred while registering user: {}", ex.getMessage());
            throw ex;
        }
    }

    @Override
    public AuthenticationResponse login(LoginRequest loginRequest) {
        log.info("Request to login with email: {} ", loginRequest.getEmail());

        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(), loginRequest.getPassword()));
        Map<String, Object> claims = new HashMap<>();
        User user = (User) auth.getPrincipal();
        claims.put("fullName", user.getFirstName());
        String jwtToken = jwtService.generateToken(claims, user);
         SecurityContextHolder.getContext().setAuthentication(
         new UsernamePasswordAuthenticationToken(user, null,
         user.getAuthorities()));
        log.info("JWT token generated for user '{}':", user.getFirstName());
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
        // String email = loginRequest.getEmail();
        // String password = loginRequest.getPassword();
        // log.info("Login request with email: '{}' received ", email);
        // if (Objects.isNull(email) || Objects.isNull(password)) {
        // throw new IllegalArgumentException("Email or password cannot be null");
        // }

        // UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        // if (!passwordEncoder.matches(password, userDetails.getPassword())) {
        // log.warn("Incorrect password for user: {}", email);
        // throw new IncorrectPasswordException(ResponseCodeAndMessage.BAD_REQUEST,
        // "Incorrect password, try again");
        // }
        // String token = jwtService.generateToken(userDetails);
        // log.info("User logged in successfully: {}", email);
        // SecurityContextHolder.getContext().setAuthentication(
        // new UsernamePasswordAuthenticationToken(userDetails, null,
        // userDetails.getAuthorities()));
        // return AuthenticationResponse.builder()
        // .token(token)
        // .build();
    }

    @Override
    public void activateAccount(String token) throws MessagingException {
        log.info("Activating account........");
        AccountActivationToken savedToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid token"));
        if (LocalDateTime.now().isAfter(savedToken.getExpiresAt())) {
            sendValidationEmail(savedToken.getUser());
            throw new ResourceNotFoundException("Activation token expired, A new token has been sent to your email");
        }
        User user = userRepository.findById(savedToken.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setEnabled(true);
        userRepository.save(user);
        savedToken.setValidatedAt(LocalDateTime.now());
        tokenRepository.save(savedToken);
    }

    @Override
    public void changePassword(ChangePasswordRequest request) {
        log.info("Request to change password..........");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new ResourceNotFoundException("User is not authenticated");
        }
        User user = (User) authentication.getPrincipal();

        if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
            throw new IncorrectPasswordException(ResponseCodeAndMessage.BAD_REQUEST,
                    "New password and confirm password do not match");
        }
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new IncorrectPasswordException(ResponseCodeAndMessage.BAD_REQUEST,
                    "Current password is incorrect");
        }

        String encodedNewPassword = passwordEncoder.encode(request.getNewPassword());
        user.setPassword(encodedNewPassword);
        userRepository.save(user);
        log.info("Password changed successfully for user '{}'", user.getEmail());
    }

    private void sendValidationEmail(User user) throws MessagingException {
        String activationToken = generateAndSaveActivationToken(user);
        emailService.sendEmail(user.getEmail(), user.getFirstName(), EmailTemplateName.ACTIVATE_ACCOUNT, activationUrl,
                activationToken, "Account Validation");
        log.info("Validation email sent to {}", user.getEmail());
    }

    private String generateAndSaveActivationToken(User user) {
        String generatedToken = generateActivationToken();
        AccountActivationToken token = AccountActivationToken.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();

        tokenRepository.save(token);
        log.info("Activation token generated and saved for user {}", user.getId());
        return generatedToken;
    }

    private String generateActivationToken() {
        log.info("Generating activation token........");
        StringBuilder tokenBuilder = new StringBuilder();
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < 6; i++) {
            int digit = random.nextInt(10);
            tokenBuilder.append(digit);
        }
        String token = tokenBuilder.toString();
        log.info("Activation token generated with length {}", 6);
        return token;
    }

    private void validateUserInput(UserRequest userRequest) {
        log.info("Validating user input.......");
        if (userRequest == null) {
            throw new IllegalArgumentException("UserRequest cannot be null or empty.");
        }

        String email = userRequest.getEmail();
        if (isUserExists(email)) {
            log.info("Duplicate user detected with email: {}", email);
            throw new DuplicateException("User with email " + email + " already exists.");
        }
    }

    private boolean isUserExists(String email) {
        return userRepository.existsByEmail(email);
    }
}
