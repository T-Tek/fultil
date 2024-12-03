package com.fultil.service.impl;

import com.fultil.model.Role;
import com.fultil.model.User;
import com.fultil.enums.RoleType;
import com.fultil.exceptions.ResourceNotFoundException;
import com.fultil.repository.RoleRepository;
import com.fultil.repository.UserRepository;
import com.fultil.service.UserService;
import com.fultil.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        User currentUser = UserUtils.getAuthenticatedUser();
        log.info("Request to become a Vendor by {}", currentUser.getEmail());

        String ROLE_VENDOR = RoleType.ROLE_VENDOR.name();
        String ROLE_USER = RoleType.ROLE_USER.name();

        Role retrievedVendorRole = checkIfVendorRoleExists(ROLE_VENDOR);
        Role retrievedUserRole = checkIfUserRoleExists(ROLE_USER);

        if (currentUser.getRoles().contains(retrievedVendorRole)) {
            return "User is already a Vendor";
        }

        List<Role> roles = new ArrayList<>(currentUser.getRoles());

        if (currentUser.getRoles().contains(retrievedUserRole)) {
            roles.remove(retrievedUserRole);
        }
        roles.add(retrievedVendorRole);
        currentUser.setRoles(roles);

        userRepository.save(currentUser);

        return "User has become a Vendor";
    }

    private Role checkIfVendorRoleExists(String ROLE_VENDOR) {
        return roleRepository.findByName(ROLE_VENDOR)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor Role not found"));
    }

    private Role checkIfUserRoleExists(String ROLE_USER) {
        return roleRepository.findByName(ROLE_USER)
                .orElseThrow(() -> new ResourceNotFoundException("User Role not found"));

    }
}
