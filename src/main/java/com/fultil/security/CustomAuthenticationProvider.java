package com.fultil.security;

import com.fultil.exceptions.BadRequestException;
import com.fultil.model.Role;
import com.fultil.model.User;
import com.fultil.exceptions.ResourceNotFoundException;
import com.fultil.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        log.info("Authenticating user with email: {}", username);
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> {
                    log.warn("User with email '{}' not found", username);
                    return new ResourceNotFoundException("User not found");
                });

        if (!user.isEnabled()) {
            log.warn("User '{}' is disabled", username);
            throw new BadRequestException("User account is disabled");
        }

        if (passwordEncoder.matches(password, user.getPassword())) {
            log.info("User '{}' authenticated successfully", username);
            return new UsernamePasswordAuthenticationToken(user, password, getGrantedAuthorities(user.getRoles()));
        } else {
            log.warn("Incorrect password for user '{}'", username);
            throw new BadRequestException("Incorrect password");
        }
    }

    private List<GrantedAuthority> getGrantedAuthorities(List<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
