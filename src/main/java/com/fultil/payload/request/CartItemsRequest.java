package com.fultil.payload.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartItemsRequest {
    private Long productId;
    private Integer quantity;
}
