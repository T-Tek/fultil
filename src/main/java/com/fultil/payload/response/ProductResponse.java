package com.fultil.payload.response;

import com.fultil.enums.ProductCategory;
import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse {
    private String name;
    private BigDecimal price;
    private ProductCategory category;
    private String description;
}
