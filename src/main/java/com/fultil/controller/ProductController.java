package com.fultil.controller;

import com.fultil.enums.ResponseCodeAndMessage;
import com.fultil.payload.request.ProductRequest;
import com.fultil.payload.response.PageResponse;
import com.fultil.payload.response.ProductCategoryResponse;
import com.fultil.payload.response.ProductResponse;
import com.fultil.payload.response.Response;
import com.fultil.service.CategoryService;
import com.fultil.service.ProductService;
import com.fultil.utils.UserUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/product")
public class ProductController {
    private final ProductService productService;
    private final CategoryService categoryService;

    @PreAuthorize("hasRole('ROLE_VENDOR')")
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Response createProduct(@RequestBody @Valid ProductRequest productRequest){
        ProductResponse productResponse = productService.createProduct(productRequest);
        return UserUtils.generateResponse(ResponseCodeAndMessage.SUCCESS, productResponse);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/user/products")
    @ResponseStatus(HttpStatus.OK)
    public Response getProductsByCreator(@RequestParam(required = false) String productName,
                                         @RequestParam int page,
                                         @RequestParam int size) {
        PageResponse<List<ProductResponse>> products = productService.getProductsByCreator(productName, page, size);
        return UserUtils.generateResponse(ResponseCodeAndMessage.SUCCESS, products);
    }
    @GetMapping("/all/{category}")
    @ResponseStatus(HttpStatus.OK)
    public Response getProductsByCategory(@PathVariable String category, @RequestParam int page, @RequestParam int size){
        PageResponse<List<ProductResponse>> products = productService.getProductsByCategory(category, page, size );
        return UserUtils.generateResponse(ResponseCodeAndMessage.SUCCESS, products);
    }
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public Response getAllProducts(@RequestParam int page, @RequestParam int size){
        PageResponse<List<ProductResponse>> products = productService.getAllProducts(page, size );
        return UserUtils.generateResponse(ResponseCodeAndMessage.SUCCESS, products);
    }

    @GetMapping("/search/{name}")
    @ResponseStatus(HttpStatus.OK)
    public Response search(@PathVariable String name, @RequestParam int page, @RequestParam int size){
        PageResponse<List<ProductResponse>> products = productService.searchProductsByName(name, page, size );
        return UserUtils.generateResponse(ResponseCodeAndMessage.SUCCESS, products);
    }


    @GetMapping("/category")
    @ResponseStatus(HttpStatus.OK)
    public Response getAllCategory(){
        List<ProductCategoryResponse> categories = categoryService.getAllCategories();
        return UserUtils.generateResponse(ResponseCodeAndMessage.SUCCESS, categories);
    }

//    //redis cache testing here
//    @GetMapping("/testCache")
//    @ResponseStatus(HttpStatus.OK)
//    public String testCache(@RequestParam int page, @RequestParam int size) {
//        long start = System.currentTimeMillis();
//        PageResponse<List<ProductResponse>> response1 = productService.getAllProducts(page, size);
//        long duration1 = System.currentTimeMillis() - start;
//
//        start = System.currentTimeMillis();
//        PageResponse<List<ProductResponse>> response2 = productService.getAllProducts(page, size);
//        long duration2 = System.currentTimeMillis() - start;
//
//        return "First call duration: " + duration1 + "ms, Second call duration: " + duration2 + "ms";
//    }
}
