package com.fultil.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fultil.enums.RoleType;
import com.fultil.enums.permissions.AdminPermissions;
import com.fultil.enums.permissions.UserRolePermissions;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;


    @ElementCollection(targetClass = UserRolePermissions.class)
    @Enumerated(EnumType.STRING)
    private List<UserRolePermissions> userRolePermissions;

    @ElementCollection(targetClass = AdminPermissions.class)
    @Enumerated(EnumType.STRING)
    private List<AdminPermissions> adminPermissions;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private RoleType roleType;

    @ManyToMany(mappedBy = "roles")
    @JsonIgnore
    List<User> users;


}
