package com.fultil.payload.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderLineItemsRequest {
    private String id;
    private String skuCode;
    private String price;
    private Integer quantity;
}
