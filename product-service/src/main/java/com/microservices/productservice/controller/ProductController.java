package com.microservices.productservice.controller;

import com.microservices.productservice.client.UserServiceClient;
import com.microservices.productservice.dto.ProductRequest;
import com.microservices.productservice.dto.ProductResponse;
import com.microservices.productservice.exception.ProductNotFoundException;
import com.microservices.productservice.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/product")
@Slf4j
//@RequiredArgsConstructor - use this if you don't want the constructor
public class ProductController {

    private final ProductService productService;
    private final UserServiceClient userServiceClient;

    public ProductController(ProductService productService, UserServiceClient userServiceClient) {
        this.productService = productService;
        this.userServiceClient = userServiceClient;
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String createProduct(@RequestBody ProductRequest productRequest){
        String response = productService.createProduct(productRequest);
        return response;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getAllProducts(@RequestHeader("token") String token){
        log.info(token);
        validateUser(token);
        return productService.getAllProducts();

    }

    @GetMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponse getProductById(@PathVariable("id") String id, @RequestHeader("token") String token)
    {
        return productService.getProductById(id);
    }

    @GetMapping(path = "/name/{name}")
    @ResponseStatus(HttpStatus.OK)
    public CompletableFuture<ProductResponse> getProductByName(@PathVariable("name") String getProductByName, @RequestHeader("token") String token) throws ProductNotFoundException {
        ProductResponse productResponse = productService.getProductByName(getProductByName);
        return CompletableFuture.supplyAsync(() -> productResponse);
    }

    private void validateUser(String token){
        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String header = new String(decoder.decode(chunks[0]));
        String payload = new String(decoder.decode(chunks[1]));
        log.info("HEADER : "+ header + " PAYLOAD : " + payload);
//        String response = userServiceClient.validateToken(token);
//        log.info(response);
    }

}
