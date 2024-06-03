package com.fultil.service.impl;

import com.fultil.entity.Product;
import com.fultil.entity.ProductCategoryEntity;
import com.fultil.entity.User;
import com.fultil.enums.ProductCategory;
import com.fultil.exceptions.ResourceNotFoundException;
import com.fultil.payload.request.ProductRequest;
import com.fultil.payload.response.PageResponse;
import com.fultil.payload.response.ProductResponse;
import com.fultil.repository.ProductCategoryRepository;
import com.fultil.repository.ProductRepository;
import com.fultil.service.ProductService;
import com.fultil.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;

    @Override
    public ProductResponse createProduct(ProductRequest request, String categoryName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResourceNotFoundException("User is not authenticated, can not create product");
        }
        User user = (User) authentication.getPrincipal();
        log.info("Received request to create product with name: {} by: {}",request.getName(), authentication.getName());

        ProductCategoryEntity category = productCategoryRepository.findByName(categoryName)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: ".concat(categoryName)));


        Product newProduct = Product.builder()
                .name(request.getName())
                .price(request.getPrice())
                .category(category)
                .description(request.getDescription())
                .quantity(request.getQuantity())
                .skuCode(UserUtils.generateSku(request.getName()))
              //  .status(ProductStatus.valueOf("IN_STOCK"))
                .user(user)
                .build();
        Product savedProduct = saveProduct(newProduct);
        log.info("Product with name '{}' is saved ", request.getName());
        return convertToResponseDto(savedProduct);
    }
    @Override
    public List<ProductResponse> getProductsByCategory(ProductCategory category) {
        List<ProductResponse> responses = new ArrayList<>();
        List<Product> productList = productRepository.findAllByCategory(category);

        if (productList.isEmpty()) {
            log.info("No products found for category: " + category);
            throw new ResourceNotFoundException("No products found for category: " + category);
        }

        for (Product product : productList) {
            responses.add(convertToResponseDto(product));
        }

        return responses;
    }

    @Override
    public PageResponse<List<ProductResponse>> getProductsByCreator(String name, int page, int size, Principal creator) {
        Authentication authentication = (UsernamePasswordAuthenticationToken) creator;
        User currentUser = (User) authentication.getPrincipal();
        String userEmail = currentUser.getEmail();


        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Product> productPage = null;
        if (Objects.isNull(name)){
            log.info("Request received to get products with page {}, and size {}. Requested by user with email '{}'.", page, size, userEmail);
            productPage = productRepository.findAllByCreatedBy(currentUser.getEmail(), pageable);
        }else {
            log.info("Request received to get products with name '{}', page {}, and size {}. Requested by user with email '{}'.", name, page, size, userEmail);
            productPage = productRepository.findProductByCreator(name, currentUser.getEmail(), pageable);
        }
        if (productPage.isEmpty()){
            log.warn("No products found.");
            throw new ResourceNotFoundException("No record found");
        }
        List<ProductResponse> products = new ArrayList<>();

        for (Product product : productPage) {
            products.add(convertToResponseDto(product));
        }
        PageResponse<List<ProductResponse>> pageResponse = new PageResponse<>();

            pageResponse.setTotalElements(productPage.getNumberOfElements());
            pageResponse.setTotalPages(productPage.getTotalPages());
            pageResponse.setHasNext(productPage.hasNext());
            pageResponse.setContents(products);

            log.info("Retrieved {} products for name '{}' (Page {}/{}).", products.size(), name, productPage.getNumber(), productPage.getTotalPages());

            return pageResponse;
    }

    @Override
    public List<String> getAllProductCategories() {
        log.info("Getting list of product categories........");
        return getListOfCategoryFromEnum();
    }

    @Override
    public PageResponse<List<ProductResponse>> getAllProducts(int page, int size) {
        log.info("Request received to get products with page {}, and size {}.", page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));

        Page<Product> productPage = productRepository.findAll(pageable);
        checkIfProductPageIsNotEmpty(productPage);
        List<ProductResponse> productResponseList = new ArrayList<>();

        for (Product product : productPage) {
            productResponseList.add(convertToResponseDto(product));
        }
        PageResponse<List<ProductResponse>> pageResponse = new PageResponse<>();

        pageResponse.setTotalElements(productPage.getNumberOfElements());
        pageResponse.setTotalPages(productPage.getTotalPages());
        pageResponse.setHasNext(productPage.hasNext());
        pageResponse.setContents(productResponseList);

        log.info("Retrieved {} products for (Page {}/{}).", productResponseList.size(), productPage.getNumber(), productPage.getTotalPages());

        return pageResponse;
    }

    @Override
    public PageResponse<List<ProductResponse>> searchProductsByName(String name, int page, int size) {
        log.info("Request received to get products with name {} page {}, and size {}.", name, page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));

        Page<Product> productPage = productRepository.findAllByNameContainingIgnoreCase(name, pageable);
        checkIfProductPageIsNotEmpty(productPage);
        List<ProductResponse> productResponseList = new ArrayList<>();

        for (Product product : productPage) {
            productResponseList.add(convertToResponseDto(product));
        }
        PageResponse<List<ProductResponse>> pageResponse = new PageResponse<>();

        pageResponse.setTotalElements(productPage.getNumberOfElements());
        pageResponse.setTotalPages(productPage.getTotalPages());
        pageResponse.setHasNext(productPage.hasNext());
        pageResponse.setContents(productResponseList);

        log.info("Retrieved {} products for (Page {}/{}).", productResponseList.size(), productPage.getNumber(), productPage.getTotalPages());

        return pageResponse;
    }

    private ProductResponse convertToResponseDto(Product product) {
        return ProductResponse.builder()
                .name(product.getName())
                .price(product.getPrice())
                .category(convertCategoryEntityToEnum(product.getCategory()))
                .description(product.getDescription())
                .build();
    }

    private Product saveProduct(Product product) {
        log.info("Saving product with name: {}, price: {}, and category: {}", product.getName(), product.getPrice(), product.getCategory());
        return productRepository.save(product);
    }
    private void checkIfProductPageIsNotEmpty(Page<Product> productPage){
        if (productPage.isEmpty()){
            throw new ResourceNotFoundException("Product not found");
        }
    }

    private ProductCategory convertCategoryEntityToEnum(ProductCategoryEntity categoryEntity) {
        String categoryName = categoryEntity.getName().toUpperCase().replace(" ", "_");
        return ProductCategory.valueOf(categoryName);
    }

    private List<String> getListOfCategoryFromEnum(){
        List<String> categoryList = new ArrayList<>();
        for (ProductCategory category : ProductCategory.values()){
            categoryList.add(category.getDescription());
        }
        return categoryList;
    }
}