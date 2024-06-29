package com.fultil.service;

import com.fultil.payload.response.InventoryResponse;
import org.springframework.stereotype.Service;

@Service
public interface InventoryService {
    InventoryResponse checkStock(Long productId, int quantity);
    void updateStock(Long productId, int quantity);
}