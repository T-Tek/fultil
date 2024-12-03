package com.fultil.service.impl;

import com.fultil.model.*;
import com.fultil.exceptions.ResourceNotFoundException;
import com.fultil.payload.request.CartItemsRequest;
import com.fultil.payload.response.CartItemResponse;
import com.fultil.payload.response.CartResponse;
import com.fultil.payload.response.InventoryResponse;
import com.fultil.repository.CartItemsRepository;
import com.fultil.repository.CartRepository;
import com.fultil.service.CartItemsService;
import com.fultil.service.CartService;
import com.fultil.service.InventoryService;
import com.fultil.service.ProductService;
import com.fultil.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductService productService;
    private final InventoryService inventoryService;
    private final CartItemsService cartItemsService;
    private final CartItemsRepository cartItemsRepository;

    @Override
    public CartResponse addToCart(CartItemsRequest cartItemRequest) {
        log.info("Request to add product to cart with payload {}:", cartItemRequest);
        User user = UserUtils.getAuthenticatedUser();
        Cart cart = cartRepository.findByUser(user).orElse(new Cart(user));
        Product product = productService.findProductById(cart.getId());
        InventoryResponse inventoryResponse = inventoryService.checkStock(product.getId(), cartItemRequest.getQuantity());
        if (!inventoryResponse.isInStock()) {
            throw new ResourceNotFoundException("Insufficient stock for product: ".concat(product.getName()));
        }

        CartItems cartItem = new CartItems();
        cartItem.setProduct(product);
        cartItem.setQuantity(cartItemRequest.getQuantity());

        cart.addCartItem(cartItem);
        cartRepository.save(cart);

        return mapToCartResponse(cart);
    }

    @Override
    public CartResponse getCartByUser() {
        User user = UserUtils.getAuthenticatedUser();
        log.info("Request to get cart by {}", user);
        Cart cart = cartRepository.findByUser(user).orElse(new Cart(user));

        return mapToCartResponse(cart);
    }

    @Override
    public void clearCart() {
        User user = UserUtils.getAuthenticatedUser();
        log.info("Request to clear cart by {}", user);

        Cart cart = cartRepository.findByUser(user).orElse(new Cart(user));

        cart.clearItems();
        cartRepository.save(cart);
    }

    public String removeItemFromCart(Product product) {
        return null;
    }

    @Override
    public BigDecimal getTotalPriceOfCartItemsByCurrentUser() {
        CartResponse cartResponse = getCartByUser();
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (CartItemResponse cartItemResponse : cartResponse.getCartItemResponseList()) {
            totalPrice = totalPrice.add(cartItemResponse.getPrice().multiply(BigDecimal.valueOf(cartItemResponse.getQuantity())));
        }
        log.info("Total price of cart items is ---------> {}", totalPrice);
        return totalPrice;
    }

    private CartResponse mapToCartResponse(Cart cart) {
        List<CartItemResponse> cartItemResponses = new ArrayList<>();
        for (CartItems cartItems : cart.getCartItems()){
            cartItemResponses.add(mapToCartItemResponse(cartItems));
        }
        return new CartResponse(cartItemResponses);
    }

    private CartItemResponse mapToCartItemResponse(CartItems cartItems){
        return new CartItemResponse(cartItems.getId(), cartItems.getProduct().getName(), cartItems.getQuantity(), cartItems.getProduct().getPrice());
    }
}
