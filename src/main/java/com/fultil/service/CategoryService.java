package com.fultil.service;

import com.fultil.payload.response.ProductCategoryResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {
    List<ProductCategoryResponse> getAllCategories();
}
