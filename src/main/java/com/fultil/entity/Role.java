package com.fultil.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fultil.enums.RoleType;
import com.fultil.enums.permissions.AdminPermissions;
import com.fultil.enums.permissions.UserPermissions;
import com.fultil.enums.permissions.VendorPermissions;
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

    @ElementCollection(targetClass = VendorPermissions.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @Column(name = "vendor_permissions")
    private List<VendorPermissions> vendorRolePermissions;

    @ElementCollection(targetClass = UserPermissions.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @Column(name = "user_permissions")
    private List<UserPermissions> userPermissions;

    @ElementCollection(targetClass = AdminPermissions.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @Column(name = "admin_permissions")
    private List<AdminPermissions> adminPermissions;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private RoleType roleType;

    @ManyToMany(mappedBy = "roles")
    @JsonIgnore
    List<User> users;
}
