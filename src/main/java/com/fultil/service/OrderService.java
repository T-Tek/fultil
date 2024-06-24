package com.fultil.service;

import com.fultil.payload.request.OrderRequest;
import com.fultil.payload.response.OrderResponse;
import com.fultil.payload.response.PageResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService {
    void placeOrder(OrderRequest orderRequest);

    PageResponse<List<OrderResponse>> getAllOrdersByCurrentUser(int page, int size);
}
