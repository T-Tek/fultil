package com.fultil.payload.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventoryResponse {
    private Long productId;
    private boolean inStock;
    private int availableQuantity;
}

