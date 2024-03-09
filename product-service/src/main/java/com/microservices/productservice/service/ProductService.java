package com.microservices.productservice.service;

import com.microservices.productservice.dto.ProductRequest;
import com.microservices.productservice.dto.ProductResponse;
import com.microservices.productservice.exception.ProductNotFoundException;
import com.microservices.productservice.model.Product;
import com.microservices.productservice.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.List;

@Service
@Slf4j
//@RequiredArgsConstructor // this will create a constructor at runtime and will add all the dependent attributes
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public String createProduct(ProductRequest productRequest)
    {
        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice()).build();
        productRepository.save(product);
        log.info("Product {} is saved", product.getId());  // this is equivalent to "Product" + product.getId + " is saved"
        return "Product saved successfully name: "+productRequest.getName();
    }

    public List<ProductResponse> getAllProducts()
    {
        PageRequest pageRequest = PageRequest.of(1, 10);
        Page<Product> products = productRepository.findAll(pageRequest);
        System.out.println(products);
        log.info(products.toString());
        List<ProductResponse> productResponses = products.stream().map(this::mapToProductResponse).toList();
        log.info("Sent the response");
        return productResponses;
    }


    public ProductResponse getProductById(String id) {
        Product repoResponse = productRepository.findProductById(id);
        ProductResponse response = mapToProductResponse(repoResponse);
        return response;
    }

    public ProductResponse getProductByName(String name) throws ProductNotFoundException {
        Product repoResponse = productRepository.findDistinctByNameLike(name);
        if (repoResponse == null){
            throw new ProductNotFoundException("No Product with such name found");
        }
        ProductResponse response = mapToProductResponse(repoResponse);
        log.info(response.toString());
        return response;
    }

    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }


}
