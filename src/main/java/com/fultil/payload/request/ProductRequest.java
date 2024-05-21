package com.fultil.payload.request;

import com.fultil.enums.ProductCategory;
import lombok.*;

import java.math.BigDecimal;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductRequest {
    private String name;
    private BigDecimal price;
    private ProductCategory category;
    private String description;
}
