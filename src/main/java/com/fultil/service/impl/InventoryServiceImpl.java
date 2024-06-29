package com.fultil.service.impl;

import com.fultil.model.Product;
import com.fultil.exceptions.ResourceNotFoundException;
import com.fultil.payload.response.InventoryResponse;
import com.fultil.repository.ProductRepository;
import com.fultil.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final ProductRepository productRepository;

    @Override
    public InventoryResponse checkStock(Long productId, int quantity) {
        log.info("Checking if product with ID: " + productId + " is in stock");
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        boolean inStock = product.getQuantity() >= quantity;
        return new InventoryResponse(productId, inStock, product.getQuantity());
    }

    @Override
    public void updateStock(Long productId, int quantity) {
        log.info("Request to update product with Id: {} by {}", productId, quantity);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if (product.getQuantity() < quantity) {
            throw new ResourceNotFoundException("Insufficient stock for product: " + product.getName());
        }

        product.setQuantity(product.getQuantity() - quantity);
        productRepository.save(product);
    }
}
