package com.fultil.service.impl;

import com.fultil.entity.ProductCategoryEntity;
import com.fultil.enums.ProductCategory;
import com.fultil.repository.ProductCategoryRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductCategoryService {

    private final ProductCategoryRepository productCategoryRepository;

    @PostConstruct
    public void initCategories() {
        log.info("Initializing product categories...");
        for (ProductCategory category : ProductCategory.values()) {
            if (productCategoryRepository.findByName(category.getDescription()).isEmpty()) {
                log.info("Creating category: {}", category.getDescription());
                ProductCategoryEntity categoryEntity = ProductCategoryEntity.builder()
                        .name(category.getDescription())
                        .build();
                productCategoryRepository.save(categoryEntity);
                log.info("Category created: {}", category.getDescription());
            } else {
                log.info("Category already exists: {}", category.getDescription());
            }
        }
        log.info("Product categories initialized successfully");
    }
}