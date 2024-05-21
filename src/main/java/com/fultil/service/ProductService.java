package com.fultil.service;

import com.fultil.entity.Product;
import com.fultil.enums.ProductCategory;
import com.fultil.payload.request.ProductRequest;
import com.fultil.payload.response.PageResponse;
import com.fultil.payload.response.ProductResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {
    ProductResponse createProduct(ProductRequest request);
    List<ProductResponse> getProductsByCategory(ProductCategory category);
    PageResponse<List<ProductResponse>> findAllProducts(String name, int page, int size);
    List<String> getAllProductCategories();
}
