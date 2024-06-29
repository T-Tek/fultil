package com.fultil.controller;


import com.fultil.enums.ResponseCodeAndMessage;
import com.fultil.payload.request.CartItemsRequest;
import com.fultil.payload.response.CartResponse;
import com.fultil.payload.response.Response;
import com.fultil.service.CartService;
import com.fultil.utils.UserUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Slf4j
@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_USER')")
@RequestMapping("/api/v1/cart")
public class CartController {

    private final CartService cartService;

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public Response addToCart(@RequestBody @Valid CartItemsRequest cartItemsRequest){
        CartResponse cartResponse = cartService.addToCart(cartItemsRequest);
        return UserUtils.generateResponse(ResponseCodeAndMessage.SUCCESS, cartResponse);
    }

    @GetMapping("/get-totalPrice")
    @ResponseStatus(HttpStatus.OK)
    public Response getTotalPrice(){
        BigDecimal totalPrice = cartService.getTotalPriceOfCartItemsByCurrentUser();
        return UserUtils.generateResponse(ResponseCodeAndMessage.SUCCESS, totalPrice);
    }


    @GetMapping("/get")
    @ResponseStatus(HttpStatus.OK)
    public Response getCartByUser(){
        CartResponse cartResponse = cartService.getCartByUser();
        return UserUtils.generateResponse(ResponseCodeAndMessage.SUCCESS, cartResponse);
    }

    @DeleteMapping("/clear")
    @ResponseStatus(HttpStatus.OK)
    public Response clearCart(){
        cartService.clearCart();
        return UserUtils.generateResponse(ResponseCodeAndMessage.SUCCESS, null);
    }

}
