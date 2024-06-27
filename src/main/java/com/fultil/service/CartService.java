package com.fultil.service;


import com.fultil.payload.request.CartItemsRequest;
import com.fultil.payload.response.CartResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public interface CartService {
    CartResponse addToCart(CartItemsRequest cartItemRequest);
    CartResponse getCartByUser();
    void clearCart();
    BigDecimal getTotalPriceOfCartItemsByCurrentUser();
}
