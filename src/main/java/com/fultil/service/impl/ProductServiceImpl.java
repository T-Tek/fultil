package com.fultil.service.impl;

import com.fultil.model.Product;
import com.fultil.model.ProductCategoryEntity;
import com.fultil.model.Review;
import com.fultil.model.User;
import com.fultil.enums.ProductCategory;
import com.fultil.enums.ProductStatus;
import com.fultil.exceptions.ResourceNotFoundException;
import com.fultil.payload.request.ProductRequest;
import com.fultil.payload.response.PageResponse;
import com.fultil.payload.response.ProductResponse;
import com.fultil.payload.response.ReviewResponse;
import com.fultil.repository.ProductCategoryRepository;
import com.fultil.repository.ProductRepository;
import com.fultil.service.ProductService;
import com.fultil.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;

    @Override
    public ProductResponse createProduct(ProductRequest request) {
        User user = UserUtils.getAuthenticatedUser();
        log.info("Received request to create product with name: {} by: {}", request.getName(), user.getName());

        String categoryName = request.getCategory();

        ProductCategoryEntity category = productCategoryRepository.findByName(categoryName)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: ".concat(categoryName)));


        Product newProduct = Product.builder()
                .name(request.getName())
                .price(request.getPrice())
                .category(category)
                .description(request.getDescription())
                .quantity(request.getQuantity())
                .skuCode(UserUtils.generateSku(request.getName()))
                .status(ProductStatus.valueOf("IN_STOCK"))
                .vendor(user)
                .build();
        Product savedProduct = saveProduct(newProduct);
        log.info("Product with name '{}' is saved ", request.getName());
        return convertToResponseDto(savedProduct);
    }


    //   @Cacheable(value = "items", key = "#category + '-' + #page + '-' + #size")
    @Override
    public PageResponse<List<ProductResponse>> getProductsByCategory(String category, int page, int size) {
        List<ProductResponse> productResponseList = new ArrayList<>();
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = productRepository.findAllByCategory(category, pageable);

        if (productPage.isEmpty()) {
            log.info("No products found for category: " + category);
            throw new ResourceNotFoundException("No products found for category: " + category);
        }

        for (Product product : productPage) {
            productResponseList.add(convertToResponseDto(product));
        }

        return new PageResponse<>(
                productPage.getNumberOfElements(),
                productPage.getTotalPages(),
                productPage.hasNext(),
                Map.of("products", productResponseList)
        );
    }

    //  @Cacheable(value = "items", key = "#name + '-' + #page + '-' + #pageSize")
    @Override
    public PageResponse<List<ProductResponse>> getProductsByCreator(String name, int pageNumber, int pageSize) {
        String userEmail = UserUtils.getAuthenticatedUser().getEmail();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "id"));
        Page<Product> productPage = null;
        if (Objects.isNull(name)) {
            log.info("Request received to get products with page {}, and pageSize {}. Requested by user with email '{}'.", pageNumber, pageSize, userEmail);
            productPage = productRepository.findAllByCreatedBy(userEmail, pageable);
        } else {
            log.info("Request received to get products with name '{}', page {}, and pageSize {}. Requested by user with email '{}'.", name, pageNumber, pageSize, userEmail);
            productPage = productRepository.findProductByCreator(name, userEmail, pageable);
        }
        List<ProductResponse> products = new ArrayList<>();

        for (Product product : productPage) {
            products.add(convertToResponseDto(product));
        }
        if (products.isEmpty()) {
            log.warn("No products found.");
            throw new ResourceNotFoundException("No record found");
        }
        log.info("Retrieved {} products for name '{}' (Page {}/{}).", products.size(), name, productPage.getNumber(), productPage.getTotalPages());
        return new PageResponse<>(productPage.getNumberOfElements(), productPage.getTotalPages(), productPage.hasNext(),Map.of("products", products));
    }


    //    @Cacheable(value = "items")
    @Override
    public List<String> getAllProductCategories() {
        log.info("Getting list of product categories........");
        return getListOfCategoryFromEnum();
    }

    @Cacheable(value = "items", key = "#page + '-' + #size")
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
 //       Map<String, List<ProductResponse>> products = Collections.singletonMap("products", productResponseList);

        pageResponse.setTotalElements(productPage.getNumberOfElements());
        pageResponse.setTotalPages(productPage.getTotalPages());
        pageResponse.setHasNext(productPage.hasNext());
        pageResponse.setContent(Map.of("products", productResponseList));

        log.info("Retrieved {} products for (Page {}/{}).", productResponseList.size(), productPage.getNumber(), productPage.getTotalPages());

        return pageResponse;
    }

    //   @Cacheable(value = "items", key = "#name + '-' + #page + '-' + #size")
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
        pageResponse.setContent(Map.of("products", productResponseList));

        log.info("Retrieved {} products for (Page {}/{}).", productResponseList.size(), productPage.getNumber(), productPage.getTotalPages());

        return pageResponse;
    }

    //   @CachePut(value = "items", key = "#id + '-' + #productRequest")
    @Override
    public ProductResponse updateProduct(Long id, ProductRequest productRequest) {
        log.info("Request to update Product with id: {} ", id);

        User user = UserUtils.getAuthenticatedUser();

        log.info("Received request to update product with name: {} by: {}", productRequest.getName(), user.getName());

        String categoryName = productRequest.getCategory();
        ProductCategoryEntity category = productCategoryRepository.findByName(categoryName)
                .orElseThrow(() -> {
                    log.error("Category not found: {}", categoryName);
                    return new ResourceNotFoundException("Category not found: " + categoryName);
                });

        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Product with id {} not found", id);
                    return new ResourceNotFoundException("Product with id " + id + " not found");
                });

        product.setName(productRequest.getName());
        product.setPrice(productRequest.getPrice());
        product.setQuantity(productRequest.getQuantity());
        product.setCategory(category);
        product.setDescription(productRequest.getDescription());
        product.setVendor(user);

        Product savedProduct = saveProduct(product);
        log.info("Product with id '{}' is updated and saved", id);

        return convertToResponseDto(savedProduct);
    }

    @Override
    public Product findProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }


    private ProductResponse convertToResponseDto(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .category(convertCategoryEntityToEnum(product.getCategory()))
                .quantity(product.getQuantity())
                .status(product.getProductStatus())
                .description(product.getDescription())
                .vendor(product.getVendor().getFirstName())
                .reviews(mapToReviewResponseList(product.getReviews()))
                .build();
    }


    private List<ReviewResponse> mapToReviewResponseList(List<Review> reviews) {
        //   boolean hasOrderedProduct = review.getUser().getId().equals() && review.getOrder() != null;
        return reviews.stream()
                .map(ReviewServiceImpl::mapToReviewResponse)
                .collect(Collectors.toList());
    }


    private Product saveProduct(Product product) {
        log.info("Saving product with name: {}, price: {}, and category: {}", product.getName(), product.getPrice(), product.getCategory());
        return productRepository.save(product);
    }

    private void checkIfProductPageIsNotEmpty(Page<Product> productPage) {
        if (productPage.isEmpty()) {
            throw new ResourceNotFoundException("No products found");
        }
    }

    private ProductCategory convertCategoryEntityToEnum(ProductCategoryEntity categoryEntity) {
        String categoryName = categoryEntity.getName().toUpperCase().replace(" ", "_");
        return ProductCategory.valueOf(categoryName);
    }

    private List<String> getListOfCategoryFromEnum() {
        List<String> categoryList = new ArrayList<>();
        for (ProductCategory category : ProductCategory.values()) {
            categoryList.add(category.getDescription());
        }
        return categoryList;
    }
}