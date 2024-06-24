package com.fultil.payload.response;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderLineItemsResponse {
    private String skuCode;
    private BigDecimal price;
    private Integer quantity;
}
