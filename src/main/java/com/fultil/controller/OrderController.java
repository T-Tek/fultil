package com.fultil.controller;

import com.fultil.enums.ResponseCodeAndMessage;
import com.fultil.payload.request.OrderRequest;
import com.fultil.payload.response.Response;
import com.fultil.service.OrderService;
import com.fultil.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order")
public class OrderController {
    private final OrderService orderService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/place")
    @ResponseStatus(HttpStatus.CREATED)
    public Response placeOrder(@RequestBody OrderRequest orderRequest){
         orderService.placeOrder(orderRequest);
        return UserUtils.generateResponse(ResponseCodeAndMessage.SUCCESS, "Order placed successfully");
    }
}
