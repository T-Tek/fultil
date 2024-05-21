package com.fultil.service.impl;

import com.fultil.entity.Product;
import com.fultil.enums.ProductCategory;
import com.fultil.exceptions.ResourceNotFoundException;
import com.fultil.payload.request.ProductRequest;
import com.fultil.payload.response.PageResponse;
import com.fultil.payload.response.ProductResponse;
import com.fultil.repository.ProductRepository;
import com.fultil.service.ProductService;
import com.fultil.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public ProductResponse createProduct(ProductRequest request) {
        log.info("Received request to create a product with payload: {}", request);
        Product newProduct = buildProductFromRequest(request);
        Product savedProduct = saveProduct(newProduct);
        return convertToDto(savedProduct);
    }
    @Override
    public List<ProductResponse> getProductsByCategory(ProductCategory category) {
        List<ProductResponse> responses = new ArrayList<>();
        List<Product> productList = productRepository.findAllByCategory(category);
        for (Product product : productList){
            responses.add(convertToDto(product));
        }
        return responses;
    }
    @Override
    public PageResponse<List<ProductResponse>> findAllProducts(String name, int page, int size) {
        log.info("Request received to get products with name '{}', page {}, and size {}.", name, page, size);

        Pageable pageable = PageRequest.of(page, size);

        Page<Product> productPage = productRepository.findAllByName(name, pageable);
        if (productPage.isEmpty()){
            log.warn("No products found for name '{}'.", name);
            throw new ResourceNotFoundException("No record found");
        }
        List<ProductResponse> productList = new ArrayList<>();

        for (Product product : productPage) {
            productList.add(convertToDto(product));
        }
        PageResponse<List<ProductResponse>> pageResponse = new PageResponse<>();

            pageResponse.setTotalElements(productPage.getNumberOfElements());
            pageResponse.setTotalPages(productPage.getTotalPages());
            pageResponse.setHasNext(productPage.hasNext());
            pageResponse.setContents(productList);

            log.info("Retrieved {} products for name '{}' (Page {}/{}).", productList.size(), name, productPage.getNumber(), productPage.getTotalPages());

            return pageResponse;
    }

    @Override
    public List<String> getAllProductCategories() {
        log.info("Getting list of product categories........");
        return getListOfCategoryFromEnum();
    }

    private Product buildProductFromRequest(ProductRequest request) {
        return Product.builder()
                .name(request.getName())
                .price(request.getPrice())
                .category(request.getCategory())
                .description(request.getDescription())
                .sku(UserUtils.generateSku())
                .build();
    }

    private ProductResponse convertToDto(Product product) {
        return ProductResponse.builder()
                .name(product.getName())
                .price(product.getPrice())
                .category(product.getCategory())
                .description(product.getDescription())
                .build();
    }

    private Product saveProduct(Product product) {
        log.info("Saving product with name: {}, price: {}, and category: {}", product.getName(), product.getPrice(), product.getCategory());
        return productRepository.save(product);
    }

    private List<String> getListOfCategoryFromEnum(){
        List<String> categoryList = new ArrayList<>();
        for (ProductCategory category : ProductCategory.values()){
            categoryList.add(category.getDescription());
        }
        return categoryList;
    }
}