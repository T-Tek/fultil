package com.fultil.service.impl;

import com.fultil.model.Order;
import com.fultil.model.OrderItems;
import com.fultil.model.Product;
import com.fultil.model.User;
import com.fultil.exceptions.ResourceNotFoundException;
import com.fultil.payload.request.OrderItemRequest;
import com.fultil.payload.request.OrderRequest;
import com.fultil.payload.response.OrderItemsResponse;
import com.fultil.payload.response.OrderResponse;
import com.fultil.payload.response.PageResponse;
import com.fultil.repository.OrderRepository;
import com.fultil.repository.ProductRepository;
import com.fultil.service.OrderService;
import com.fultil.utils.UserUtils;
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
    private final ProductRepository productRepository;

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
                orderResponses
        );
    }
    private OrderItems mapToOrderLineItem(OrderItemRequest orderItemRequest) {
        Product product = productRepository.findById(orderItemRequest.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        Integer availableQuantity = product.getQuantity();
        Integer requestedQuantity = orderItemRequest.getQuantity();
        if (requestedQuantity > availableQuantity) {
            throw new ResourceNotFoundException("Only " + availableQuantity + " " + product.getName() + "(s) in stock.");
        }

        // updating the product's stock here
        product.setQuantity(availableQuantity - requestedQuantity);
        productRepository.save(product);

        return OrderItems.builder()
                .product(product)
                .quantity(requestedQuantity)
                .price(product.getPrice())
                .skuCode(product.getSkuCode())
                .build();
    }

    private OrderResponse mapToOrderResponse(Order order) {
        return OrderResponse.builder()
                .orderNumber(order.getOrderNumber())
                .userName(order.getUser().getFirstName() + " " + order.getUser().getLastName())
                .orderLineItemsResponses(mapToOrderLineItemsResponse(order.getOrderLineItems()))
                .build();
    }

    private List<OrderItemsResponse> mapToOrderLineItemsResponse(List<OrderItems> orderLineItemsList) {
        List<OrderItemsResponse> responses = new ArrayList<>();
        for (OrderItems item : orderLineItemsList) {
            responses.add(mapToOrderLineItems(item));
        }
        return responses;
    }

    private OrderItemsResponse mapToOrderLineItems(OrderItems orderLineItems) {
        return OrderItemsResponse.builder()
                .price(orderLineItems.getPrice())
                .skuCode(orderLineItems.getSkuCode())
                .quantity(orderLineItems.getQuantity())
                .vendorName(orderLineItems.getProduct().getVendor().getFirstName() + " " + orderLineItems.getProduct().getVendor().getLastName())
                .build();
    }

    private String generateOrderNumber() {
        String orderNumber = UUID.randomUUID().toString().toUpperCase().replace("-", "").substring(0, 4);
        return "ORD-".concat(orderNumber);
    }
}
