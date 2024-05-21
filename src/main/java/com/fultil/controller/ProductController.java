package com.fultil.controller;

import com.fultil.enums.ProductCategory;
import com.fultil.enums.ResponseCodeAndMessage;
import com.fultil.payload.request.ProductRequest;
import com.fultil.payload.response.PageResponse;
import com.fultil.payload.response.ProductResponse;
import com.fultil.payload.response.Response;
import com.fultil.service.ProductService;
import com.fultil.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Var;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_USER')")
@RequestMapping("/api/v1/product")
public class ProductController {
    private final ProductService productService;

    @PostMapping("/create")
    public Response createProduct(@RequestBody ProductRequest productRequest){
        ProductResponse productResponse = productService.createProduct(productRequest);
        return UserUtils.generateResponse(ResponseCodeAndMessage.SUCCESS, productResponse);
    }

    @GetMapping("/{name}")
    public Response getAllProducts(@PathVariable String name, @RequestParam int page, @RequestParam int size){
        PageResponse<List<ProductResponse>> products = productService.findAllProducts(name,page, size );
        return UserUtils.generateResponse(ResponseCodeAndMessage.SUCCESS, products);
    }

    @GetMapping("/{category}")
    public Response getAllCategory(@PathVariable ProductCategory category){
        List<ProductResponse> categories = productService.getProductsByCategory(category);
        return UserUtils.generateResponse(ResponseCodeAndMessage.SUCCESS, categories);
    }
}
