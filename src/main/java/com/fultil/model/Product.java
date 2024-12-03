package com.fultil.model;

import com.fultil.enums.ProductStatus;
import com.fultil.model.auditable.Auditable;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    @JoinColumn(
            name = "category_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_product_category")
    )
    private ProductCategoryEntity category;

    private String description;

    @ManyToOne
    @JoinColumn(
            name = "vendor_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_product_vendor")
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User vendor;

    private Integer quantity;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Review> reviews;

    public ProductStatus getProductStatus() {
        return quantity < 1 ? ProductStatus.OUT_OF_STOCK : ProductStatus.IN_STOCK;
    }
}
