package com.fultil.service.impl;

import com.fultil.exceptions.BadRequestException;
import com.fultil.model.CartItems;
import com.fultil.model.Product;
import com.fultil.repository.CartItemsRepository;
import com.fultil.service.CartItemsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartItemsServiceImpl implements CartItemsService {
    private final CartItemsRepository cartItemsRepository;

    @Override
    public Long getProductFromCartItems(Product product) {
        CartItems cartItems = cartItemsRepository.findByProduct(product)
                .orElseThrow(()-> new BadRequestException("Not found"));
        return cartItems.getProduct().getId();
    }
    public void deleteProductFromCartItems(Product product){
        cartItemsRepository.deleteCartItemsByProduct(product);
    }

}
