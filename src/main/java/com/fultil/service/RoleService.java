package com.fultil.service;

import com.fultil.entity.Role;
import com.fultil.entity.User;
import com.fultil.enums.RoleType;
import com.fultil.enums.permissions.AdminPermissions;
import com.fultil.enums.permissions.UserRolePermissions;
import com.fultil.repository.RoleRepository;
import com.fultil.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @PostConstruct
    public void initializeRoles() {
        Role userRole = Role.builder()
                .name("ROLE_USER")
                .roleType(RoleType.ROLE_USER)
                .userRolePermissions(new ArrayList<>(EnumSet.allOf(UserRolePermissions.class)))
                .build();

        Role adminRole = Role.builder()
                .name("ROLE_ADMIN")
                .roleType(RoleType.ROLE_ADMIN)
                .adminPermissions(new ArrayList<>(EnumSet.allOf(AdminPermissions.class)))
                .build();

        createRole(adminRole);
        createRole(userRole);
    }
    private void createRole(Role role){
        if (roleRepository.existsByName(role.getName())){
            return;
        }
        roleRepository.save(role);
    }

//    public void makeAdmin(Long userId) {
//        Optional<Role> optionalRole = roleRepository.findByName("ROLE_ADMIN");
//        Role adminRole = optionalRole.orElseThrow(() -> new IllegalStateException("Admin role not found"));
//
//        Optional<User> optionalUser = userRepository.findById(userId);
//        User user = optionalUser.orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
//
//        user.getRoles().removeIf(role -> role.getName().equals("USER"));
//
//        user.getRoles().add(adminRole);
//        userRepository.save(user);
//    }
}