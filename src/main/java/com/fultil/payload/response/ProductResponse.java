package com.fultil.payload.response;

import com.fultil.enums.ProductCategory;
import com.fultil.enums.ProductStatus;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String name;
    private BigDecimal price;
    private ProductCategory category;
    private int quantity;
    private ProductStatus status;
    private String description;
    private String owner;
}
