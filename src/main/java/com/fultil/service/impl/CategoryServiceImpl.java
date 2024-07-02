package com.fultil.service.impl;

import com.fultil.model.ProductCategoryEntity;
import com.fultil.payload.response.ProductCategoryResponse;
import com.fultil.repository.ProductCategoryRepository;
import com.fultil.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final ProductCategoryRepository productCategoryRepository;

 //   @Cacheable(value = "items", key = "#id")
    @Override
    public List<ProductCategoryResponse> getAllCategories() {
        List<ProductCategoryResponse> responses = new ArrayList<>();
        List<ProductCategoryEntity> productCategoryEntities = productCategoryRepository.findAll();
        for (ProductCategoryEntity entity : productCategoryEntities){
            responses.add(convertToCatResponse(entity));
        }
        return responses;
    }

    private static ProductCategoryResponse convertToCatResponse(ProductCategoryEntity entity){
        ProductCategoryResponse response = new ProductCategoryResponse();
        BeanUtils.copyProperties(entity, response);
        return response;
    }
}
