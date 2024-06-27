package com.fultil.model;

import com.fultil.enums.ProductStatus;
import jakarta.persistence.*;
import lombok.*;

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
    @JoinColumn(name = "vendor_id", nullable = false)
    private User vendor; // Vendor who owns the product

    private Integer quantity;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    @OneToMany(mappedBy = "product")
    private List<Review> reviews;

    public ProductStatus getProductStatus(){
        return quantity < 1 ? ProductStatus.OUT_OF_STOCK : ProductStatus.IN_STOCK;
    }
}
