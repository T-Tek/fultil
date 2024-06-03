package com.fultil.service.impl;

import com.fultil.entity.ProductCategoryEntity;
import com.fultil.enums.ProductCategory;
import com.fultil.repository.ProductCategoryRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductCategoryService {

    private final ProductCategoryRepository productCategoryRepository;

    @PostConstruct
    public void initCategories() {
        for (ProductCategory category : ProductCategory.values()) {
            if (productCategoryRepository.findByName(category.getDescription()).isEmpty()) {
                ProductCategoryEntity categoryEntity = ProductCategoryEntity.builder()
                        .name(category.getDescription())
                        .build();
                productCategoryRepository.save(categoryEntity);
            }
        }
    }
}
