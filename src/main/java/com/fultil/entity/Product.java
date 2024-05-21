package com.fultil.entity;

import com.fultil.enums.ProductCategory;
import com.fultil.enums.ProductStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private ProductCategory category;

    private String description;
    @ManyToOne
    private User user;
    @CreatedDate
    private LocalDate createdDate;
    @UpdateTimestamp
    private LocalDate updateDate;

    private Integer quantity;
    private String image;
    private Double weight;
    private String dimensions;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;
    private String sku;
    private String metaTitle;
    private String metaDescription;
    private Double rating;
    @OneToMany
    private List<Review> reviews;
}

