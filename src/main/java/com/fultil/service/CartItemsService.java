package com.fultil.service;

import com.fultil.model.CartItems;
import com.fultil.model.Product;

public interface CartItemsService {
    Long getProductFromCartItems(Product product);
}
