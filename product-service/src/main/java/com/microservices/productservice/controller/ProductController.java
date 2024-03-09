package com.microservices.productservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservices.productservice.client.UserServiceClient;
import com.microservices.productservice.dto.JwtPayloadDto;
import com.microservices.productservice.dto.ProductRequest;
import com.microservices.productservice.dto.ProductResponse;
import com.microservices.productservice.dto.ValidateTokenDto;
import com.microservices.productservice.exception.ProductNotFoundException;
import com.microservices.productservice.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.EntityResponse;

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
        public List<ProductResponse> getAllProducts() throws JsonProcessingException {
//        log.info(token);
//        validateUser(token);
        log.info("Sent");
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
    public ResponseEntity<ProductResponse> getProductByName(@PathVariable("name") String getProductByName) throws ProductNotFoundException {
        ProductResponse productResponse = productService.getProductByName(getProductByName);
        log.info(String.valueOf(productResponse));
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    private void validateUser(String token) throws JsonProcessingException {
        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String header = new String(decoder.decode(chunks[0]));
        String payload = new String(decoder.decode(chunks[1]));
        ObjectMapper mapper = new ObjectMapper();
        log.info("HEADER : "+ header + " PAYLOAD : " + payload);
        JwtPayloadDto jwtPayload = mapper.readValue(payload, JwtPayloadDto.class);
        log.info("JWT" + jwtPayload.toString());
        int userId = jwtPayload.getUserId();
        ValidateTokenDto validateTokenDto = new ValidateTokenDto((long)userId, token);
        String response = userServiceClient.validateToken(validateTokenDto);
        log.info(response);


//        String response = userServiceClient.validateToken(token);
//        log.info(response);
    }

}
