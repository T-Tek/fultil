package com.fultil.payload.request;

import com.fultil.entity.User;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRequest {
    private List<OrderLineItemsRequest> orderLineItemsRequests;
    private User user;
}
