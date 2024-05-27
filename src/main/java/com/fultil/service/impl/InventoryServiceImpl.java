package com.fultil.service.impl;

import com.fultil.repository.ProductRepository;
import com.fultil.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {
    private final ProductRepository productRepository;

    @Override
    public boolean isInStock(String skuCode) {
        return productRepository.existsBySkuCode(skuCode);
    }
}
