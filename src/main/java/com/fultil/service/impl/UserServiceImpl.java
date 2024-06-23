package com.fultil.service.impl;

import com.fultil.entity.Role;
import com.fultil.entity.User;
import com.fultil.enums.RoleType;
import com.fultil.exceptions.ResourceNotFoundException;
import com.fultil.repository.RoleRepository;
import com.fultil.repository.UserRepository;
import com.fultil.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public String becomeVendor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResourceNotFoundException("User is not authenticated, cannot create product");
        }

        User user = (User) authentication.getPrincipal();
        log.info("Request to become a Vendor by {}", user.getEmail());

        String roleName = RoleType.ROLE_VENDOR.name();
        String defaultRole = RoleType.ROLE_USER.name();
        Role vendorRole = roleRepository.findByName(roleName)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor Role not found"));

        Role userRole = roleRepository.findByName(defaultRole)
                .orElseThrow(() -> new ResourceNotFoundException("User Role not found"));

        if (user.getRoles().contains(vendorRole)) {
            return "User is already a Vendor";
        }

        List<Role> roles = new ArrayList<>(user.getRoles());

        if (user.getRoles().contains(userRole)) {
            roles.remove(userRole);
        }
        roles.add(vendorRole);
        user.setRoles(roles);

        userRepository.save(user);

        return "User has become a Vendor";
    }
}
