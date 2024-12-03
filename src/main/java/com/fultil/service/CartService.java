package com.fultil.service;


import com.fultil.model.CartItems;
import com.fultil.model.Product;
import com.fultil.payload.request.CartItemsRequest;
import com.fultil.payload.response.CartResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public interface CartService {
    CartResponse addToCart(CartItemsRequest cartItemRequest);
    CartResponse getCartByUser();
    void clearCart();
    //String removeItemFromCart(CartItems items);

    String removeItemFromCart(Product product);

    BigDecimal getTotalPriceOfCartItemsByCurrentUser();
}
