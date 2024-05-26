package com.fultil.controller;

import com.fultil.enums.ResponseCodeAndMessage;
import com.fultil.payload.request.OrderRequest;
import com.fultil.payload.response.Response;
import com.fultil.service.OrderService;
import com.fultil.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order")
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/place")
    public Response makeOrder(@RequestBody OrderRequest orderRequest){
         orderService.placeOrder(orderRequest);
        return UserUtils.generateResponse(ResponseCodeAndMessage.SUCCESS, "Order placed successfully");
    }
}
