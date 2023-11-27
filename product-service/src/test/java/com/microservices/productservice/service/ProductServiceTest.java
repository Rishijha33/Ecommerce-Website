package com.microservices.productservice.service;
import com.microservices.productservice.dto.ProductResponse;
import com.microservices.productservice.model.Product;
import com.microservices.productservice.repository.ProductRepository;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProductServiceTest {
    @Mock  // we need a mock object of the given attribute
    private ProductRepository productRepository;
    @InjectMocks //this is the class under test and this is where the mock objects will be injected
    private ProductService productService;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this); //this will create a fresh mock for each test and will auto close the mocks as well
    }

    @Test
    public void testFindAllProductSuccess(){
        //Arrange
        Product mockProduct = new Product();
        List<Product> mockProducts = new ArrayList<>();
        mockProduct.setId(UUID.randomUUID().toString());
        mockProduct.setDescription("Test product");
        mockProduct.setPrice(BigDecimal.valueOf(1200));
        mockProduct.setName("Test product");
        mockProducts.add(mockProduct);
        when(productRepository.findAll()).thenReturn(mockProducts);
        //Act
        List<ProductResponse> actualResponses =productService.getAllProducts();
        //Assert
        Assertions.assertIterableEquals(actualResponses, mockProducts);
    }
}
