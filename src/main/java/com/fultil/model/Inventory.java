package com.fultil.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private int reserved;

    @Column(nullable = true)
    private String location;

    @Column(nullable = false)
    @LastModifiedDate
    private LocalDateTime lastUpdated;

    @Column(nullable = false)
    private int threshold;
}

