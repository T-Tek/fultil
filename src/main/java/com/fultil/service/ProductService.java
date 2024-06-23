package com.fultil.service;

import com.fultil.enums.ProductCategory;
import com.fultil.payload.request.ProductRequest;
import com.fultil.payload.response.PageResponse;
import com.fultil.payload.response.ProductResponse;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public interface ProductService {
    ProductResponse createProduct(ProductRequest request);
    PageResponse<List<ProductResponse>> getProductsByCategory(String category, int page, int size);
    PageResponse<List<ProductResponse>> getProductsByCreator(String name, int page, int size, Principal principal);
    List<String> getAllProductCategories();
    PageResponse<List<ProductResponse>> getAllProducts(int page, int size);
    PageResponse<List<ProductResponse>> searchProductsByName(String name, int page, int size);
    ProductResponse updateProduct(Long id, ProductRequest productRequest);
}
