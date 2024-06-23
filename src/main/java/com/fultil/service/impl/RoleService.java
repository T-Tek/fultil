package com.fultil.service.impl;

import com.fultil.entity.Role;
import com.fultil.enums.RoleType;
import com.fultil.enums.permissions.AdminPermissions;
import com.fultil.enums.permissions.UserPermissions;
import com.fultil.enums.permissions.VendorPermissions;
import com.fultil.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.EnumSet;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    @PostConstruct
    public void initializeRoles() {
        Role userRole = Role.builder()
                .name("ROLE_USER")
                .roleType(RoleType.ROLE_USER)
                .userPermissions(new ArrayList<>(EnumSet.allOf(UserPermissions.class)))
                .build();

        Role vendorRole = Role.builder()
                .name("ROLE_VENDOR")
                .roleType(RoleType.ROLE_VENDOR)
                .vendorRolePermissions(new ArrayList<>(EnumSet.allOf(VendorPermissions.class)))
                .build();

        Role adminRole = Role.builder()
                .name("ROLE_ADMIN")
                .roleType(RoleType.ROLE_ADMIN)
                .adminPermissions(new ArrayList<>(EnumSet.allOf(AdminPermissions.class)))
                .build();

        createRole(userRole);
        createRole(vendorRole);
        createRole(adminRole);
    }

    private void createRole(Role role) {
        if (roleRepository.existsByName(role.getName())) {
            return;
        }
        roleRepository.save(role);
    }
}
