package com.microservices.productservice.controller;

import com.microservices.productservice.dto.ProductRequest;
import com.microservices.productservice.dto.ProductResponse;
import com.microservices.productservice.exception.ProductNotFoundException;
import com.microservices.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/product")
//@RequiredArgsConstructor - use this if you don't want the constructor
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String createProduct(@RequestBody ProductRequest productRequest){
        String response = productService.createProduct(productRequest);
        return response;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getAllProducts(){
        return productService.getAllProducts();
    }

    @GetMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponse getProductById(@PathVariable("id") String id)
    {
        return productService.getProductById(id);
    }

    @GetMapping(path = "/name/{name}")
    @ResponseStatus(HttpStatus.OK)
    public CompletableFuture<ProductResponse> getProductByName(@PathVariable("name") String getProductByName) throws ProductNotFoundException {
        ProductResponse productResponse = productService.getProductByName(getProductByName);
        return CompletableFuture.supplyAsync(() -> productResponse);
    }

}
