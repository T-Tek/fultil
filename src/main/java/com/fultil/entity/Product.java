package com.fultil.entity;

import com.fultil.enums.ProductCategory;
import com.fultil.enums.ProductStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product extends Auditable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private BigDecimal price;
    private String skuCode;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private ProductCategoryEntity category;

    private String description;

    @ManyToOne
    private User user;

    private Integer quantity;
//    private String image;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    @OneToMany
    private List<Review> reviews;

    public ProductStatus getProductStatus(){
        return quantity < 1 ? ProductStatus.OUT_OF_STOCK : ProductStatus.IN_STOCK;
    }
}
