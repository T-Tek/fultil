package com.fultil.payload.request;

import com.fultil.entity.User;
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
    private String category;
    private String description;
    private Integer quantity;
}
