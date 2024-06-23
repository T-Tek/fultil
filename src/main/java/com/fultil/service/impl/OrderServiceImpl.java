package com.fultil.service.impl;

import com.fultil.entity.Order;
import com.fultil.entity.OrderLineItems;
import com.fultil.payload.request.OrderLineItemsRequest;
import com.fultil.payload.request.OrderRequest;
import com.fultil.repository.OrderRepository;
import com.fultil.repository.ProductRepository;
import com.fultil.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Override
    public void placeOrder(OrderRequest orderRequest) {
        log.info("Starting to create order with {} line items.", orderRequest.getOrderLineItemsRequests().size());

        List<OrderLineItems> orderLineItemsList = convertToOrderLineItemsEntity(orderRequest.getOrderLineItemsRequests());

        Order newOrder = Order.builder()
                .orderNumber(UUID.randomUUID().toString())
                .orderLineItems(orderLineItemsList)
                .build();

        log.info("Order created successfully with order number: {}", newOrder.getOrderNumber());

        orderRepository.save(newOrder);
    }

    private List<OrderLineItems> convertToOrderLineItemsEntity(List<OrderLineItemsRequest> orderLineItemsRequests) {
        List<OrderLineItems> orderLineItemsList = new ArrayList<>();
        for (OrderLineItemsRequest request : orderLineItemsRequests) {
            OrderLineItems orderLineItems = convertToOrderEntity(request);
            orderLineItemsList.add(orderLineItems);
        }
        return orderLineItemsList;
    }

    private OrderLineItems convertToOrderEntity(OrderLineItemsRequest request) {
        log.info("Converting OrderLineItemsRequest to OrderLineItems for SKU code: {}", request.getSkuCode());
        return OrderLineItems.builder()
                .price(request.getPrice())
                .quantity(request.getQuantity())
                .skuCode(request.getSkuCode())
                .build();
    }
}
