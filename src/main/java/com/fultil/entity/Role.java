package com.fultil.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fultil.enums.Permissions;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

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

    @ElementCollection(targetClass = Permissions.class)
    @Enumerated(EnumType.STRING)
    private Set<Permissions> permissions;

    @ManyToMany(mappedBy = "roles")
    @JsonIgnore
    List<User> users;
    @CreatedDate
    private LocalDate createdDate;
    @LastModifiedDate
    private LocalDate updateDate;


}
