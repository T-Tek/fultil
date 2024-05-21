package com.fultil.service;

import com.fultil.entity.Role;
import com.fultil.entity.User;
import com.fultil.repository.RoleRepository;
import com.fultil.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @PostConstruct
    public void init() {
        createDefaultRole();
    }

    private void createDefaultRole() {
        if (roleRepository.findByName("USER").isEmpty()) {
            Role userRole = Role.builder()
                    .name("USER")
                    .build();
            roleRepository.save(userRole);
        }
        if (roleRepository.findByName("ADMIN").isEmpty()) {
            Role adminRole = Role.builder()
                    .name("ADMIN")
                    .build();
            roleRepository.save(adminRole);
        }
    }
    public void makeAdmin(Long userId) {
        Optional<Role> optionalRole = roleRepository.findByName("ADMIN");
        Role adminRole = optionalRole.orElseThrow(() -> new IllegalStateException("Admin role not found"));

        Optional<User> optionalUser = userRepository.findById(userId);
        User user = optionalUser.orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        user.getRoles().removeIf(role -> role.getName().equals("USER"));

        user.getRoles().add(adminRole);
        userRepository.save(user);
    }
}