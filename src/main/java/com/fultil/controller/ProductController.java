package com.fultil.controller;

import com.fultil.entity.ProductCategoryEntity;
import com.fultil.enums.ProductCategory;
import com.fultil.enums.ResponseCodeAndMessage;
import com.fultil.payload.request.ProductRequest;
import com.fultil.payload.response.PageResponse;
import com.fultil.payload.response.ProductCategoryResponse;
import com.fultil.payload.response.ProductResponse;
import com.fultil.payload.response.Response;
import com.fultil.repository.ProductCategoryRepository;
import com.fultil.service.CategoryService;
import com.fultil.service.ProductService;
import com.fultil.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/product")
public class ProductController {
    private final ProductService productService;
    private final CategoryService categoryService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/create")
    public Response createProduct(@RequestBody ProductRequest productRequest, String categoryName){
        ProductResponse productResponse = productService.createProduct(productRequest, categoryName);
        return UserUtils.generateResponse(ResponseCodeAndMessage.SUCCESS, productResponse);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/user/products")
    public Response getProductsByCreator(@RequestParam(required = false) String productName,
                                         @RequestParam int page,
                                         @RequestParam int size,
                                         Principal principal) {
        PageResponse<List<ProductResponse>> products = productService.getProductsByCreator(productName, page, size, principal);
        return UserUtils.generateResponse(ResponseCodeAndMessage.SUCCESS, products);
    }


    //    @GetMapping("/all/{productName}")
//    public Response getAllProducts(@PathVariable String productName, @RequestParam int page, @RequestParam int size){
//        PageResponse<List<ProductResponse>> products = productService.getAllProducts(productName,page, size );
//        return UserUtils.generateResponse(ResponseCodeAndMessage.SUCCESS, products);
//    }
    @GetMapping("/all")
    public Response getAllProducts(@RequestParam int page, @RequestParam int size){
        PageResponse<List<ProductResponse>> products = productService.getAllProducts(page, size );
        return UserUtils.generateResponse(ResponseCodeAndMessage.SUCCESS, products);
    }

    @GetMapping("/search/{name}")
    public Response search(@PathVariable String name, @RequestParam int page, @RequestParam int size){
        PageResponse<List<ProductResponse>> products = productService.searchProductsByName(name, page, size );
        return UserUtils.generateResponse(ResponseCodeAndMessage.SUCCESS, products);
    }


    @GetMapping("/category")
    public Response getAllCategory(){
        List<ProductCategoryResponse> categories = categoryService.getAllCategories();
        return UserUtils.generateResponse(ResponseCodeAndMessage.SUCCESS, categories);
    }
}
