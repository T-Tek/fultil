package com.fultil.payload.request;


import java.util.List;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Vendor ID is required")
    private Long vendorId;

    @NotNull(message = "Order items are required")
    private List<OrderItemRequest> orderItems;
}
