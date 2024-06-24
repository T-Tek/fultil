package com.fultil.service.impl;

import com.fultil.entity.Order;
import com.fultil.entity.OrderLineItems;
import com.fultil.entity.User;
import com.fultil.exceptions.ResourceNotFoundException;
import com.fultil.payload.request.OrderLineItemsRequest;
import com.fultil.payload.request.OrderRequest;
import com.fultil.payload.response.OrderLineItemsResponse;
import com.fultil.payload.response.OrderResponse;
import com.fultil.payload.response.PageResponse;
import com.fultil.repository.OrderRepository;
import com.fultil.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
//    private final ProductRepository productRepository;

    @Override
    public void placeOrder(OrderRequest orderRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResourceNotFoundException("User is not authenticated, cannot place order");
        }

        User user = (User) authentication.getPrincipal();

        log.info("Request to place order with {} line items.", orderRequest.getOrderLineItemsRequests().size());

        List<OrderLineItems> orderLineItemsList = convertToOrderLineItemsEntity(orderRequest.getOrderLineItemsRequests());

        Order newOrder = Order.builder()
                .orderNumber(UUID.randomUUID().toString())
                .orderLineItems(orderLineItemsList)
                .user(user)
                .build();

        log.info("Order placed successfully with order number: {}", newOrder.getOrderNumber());

        orderRepository.save(newOrder);
    }
    @Override
    public PageResponse<List<OrderResponse>> getAllOrdersByCurrentUser(int page, int size) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResourceNotFoundException("User is not authenticated, cannot become a vendor");
        }

        User user = (User) authentication.getPrincipal();
        log.info("Request to get order/s placed by {}", user);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC);
        Page<Order> orderPage = orderRepository.findAllByUser(user, pageable);
        if (orderPage.isEmpty()){
            throw new ResourceNotFoundException("No order found for user");
        }
        List<OrderResponse> orderResponses = new ArrayList<>();
        for (Order order : orderPage){
            orderResponses.add(mapToOrderResponse(order));
        }
        return new PageResponse<>(
                orderPage.getNumberOfElements(),
                orderPage.getTotalPages(),
                orderPage.hasNext(),
                orderResponses
        );
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
    private OrderResponse mapToOrderResponse(Order order){
        return OrderResponse.builder()
                .orderNumber(order.getOrderNumber())
                .orderLineItemsResponses(mapToOrderLineItemsResponse(order.getOrderLineItems()))
                .build();
    }

    private List<OrderLineItemsResponse> mapToOrderLineItemsResponse(List<OrderLineItems> orderLineItemsList){
        List<OrderLineItemsResponse> orderLineItemsResponses = new ArrayList<>();
        for (OrderLineItems orderLineItems : orderLineItemsList){
            orderLineItemsResponses.add(mapToOrderLineItems(orderLineItems));
        }
        return orderLineItemsResponses;
    }
    private OrderLineItemsResponse mapToOrderLineItems(OrderLineItems orderLineItems){
        return OrderLineItemsResponse.builder()
                .price(orderLineItems.getPrice())
                .skuCode(orderLineItems.getSkuCode())
                .quantity(orderLineItems.getQuantity())
                .build();
    }
}
