package com.fultil.service.impl;

import com.fultil.model.*;
import com.fultil.exceptions.ResourceNotFoundException;
import com.fultil.payload.request.OrderItemRequest;
import com.fultil.payload.request.OrderRequest;
import com.fultil.payload.response.InventoryResponse;
import com.fultil.payload.response.OrderItemsResponse;
import com.fultil.payload.response.OrderResponse;
import com.fultil.payload.response.PageResponse;
import com.fultil.repository.OrderRepository;
import com.fultil.repository.ProductRepository;
import com.fultil.service.InventoryService;
import com.fultil.service.OrderService;
import com.fultil.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
//    private final ProductRepository productRepository;
    private final InventoryService inventoryService;

    @Override
    public void placeOrder(OrderRequest orderRequest) {
        User user = UserUtils.getAuthenticatedUser();

        log.info("Request to place order with {} line items.", orderRequest.getOrderItems().size());

        List<OrderItems> orderLineItems = new ArrayList<>();

        for (OrderItemRequest itemRequest : orderRequest.getOrderItems()) {
            OrderItems orderItem = mapToOrderLineItem(itemRequest);
            orderLineItems.add(orderItem);
        }

        Order newOrder = Order.builder()
                .user(user)
                .orderNumber(generateOrderNumber())
                .orderLineItems(orderLineItems)
                .build();

        log.info("Order placed successfully with order number: {}", newOrder.getOrderNumber());

        orderRepository.save(newOrder);
    }

    @Override
    public PageResponse<List<OrderResponse>> getAllOrdersByCurrentUser(int page, int size) {
        User user = UserUtils.getAuthenticatedUser();
        log.info("Request to get orders placed by {}", user.getEmail());

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<Order> orderPage = orderRepository.findAllByUser(user, pageable);
        if (orderPage.isEmpty()) {
            throw new ResourceNotFoundException("No orders found for user");
        }

        List<OrderResponse> orderResponses = new ArrayList<>();
        for (Order order : orderPage.getContent()) {
            orderResponses.add(mapToOrderResponse(order));
        }

        return new PageResponse<>(
                orderPage.getNumber(),
                orderPage.getTotalPages(),
                orderPage.hasNext(),
                Map.of("orders", orderResponses)
        );
    }

    private OrderItems mapToOrderLineItem(OrderItemRequest orderItemRequest) {
//        Product product = productRepository.findById(orderItemRequest.getProductId())
//                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
//
//        InventoryResponse inventoryResponse = inventoryService.checkStock(orderItemRequest.getProductId(), orderItemRequest.getQuantity());
//        if (!inventoryResponse.isInStock()) {
//            throw new ResourceNotFoundException("Insufficient stock for product: " + orderItemRequest.getProductId());
//        }
//
//        inventoryService.updateStock(orderItemRequest.getProductId(), orderItemRequest.getQuantity());
//
//        return OrderItems.builder()
//                .product(product)
//                .quantity(orderItemRequest.getQuantity())
//                .price(product.getPrice())
//                .skuCode(product.getSkuCode())
//                .build();
        return null;
    }

    private OrderResponse mapToOrderResponse(Order order) {
        return OrderResponse.builder()
                .orderNumber(order.getOrderNumber())
                .orderDate(order.getCreatedDate())
                .orderItems(mapToOrderItemsResponse(order.getOrderLineItems()))
                .build();
    }

    private List<OrderItemsResponse> mapToOrderItemsResponse(List<OrderItems> orderLineItems) {
        List<OrderItemsResponse> orderItemsResponses = new ArrayList<>();
        for (OrderItems item : orderLineItems) {
            orderItemsResponses.add(
                    OrderItemsResponse.builder()
                            .name(item.getProduct().getName())
                            .price(item.getPrice())
                            .quantity(item.getQuantity())
                            .vendorName(item.getProduct().getVendor().getName())
                            .build()
            );
        }
        return orderItemsResponses;
    }

    private String generateOrderNumber() {
        return UUID.randomUUID().toString();
    }
}
