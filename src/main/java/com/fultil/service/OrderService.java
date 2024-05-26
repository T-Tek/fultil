package com.fultil.service;

import com.fultil.payload.request.OrderRequest;
import org.springframework.stereotype.Service;

@Service
public interface OrderService {
    void placeOrder(OrderRequest orderRequest);
}
