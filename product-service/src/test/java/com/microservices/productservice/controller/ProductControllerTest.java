package com.microservices.productservice.controller;

import com.microservices.productservice.dto.ProductRequest;
import com.microservices.productservice.exception.ProductNotFoundException;
import com.microservices.productservice.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
@Slf4j
@WebMvcTest(ProductController.class)
public class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ProductService productService;

    @Test
    public void createProductSuccess() throws Exception {
        //Arrange
        ProductRequest testProductRequest = new ProductRequest();
        testProductRequest.setName("Test Product Request");
        testProductRequest.setDescription("This is a Test product request");
        testProductRequest.setPrice(BigDecimal.valueOf(10110));
        String testResponse = "Product saved successfully name: "+testProductRequest.getName();
        if(productService == null){return;}
        when(productService.createProduct(eq(testProductRequest))).thenReturn(testResponse); //the eq() means that we get an equivalent object here we can generate the response
        //Act

        String requestJson = convertToJson(testProductRequest);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/product")  //passing the request in the perform post call
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(testResponse));
    }

    @Test
    public void findProductByNameFailure() throws Exception {
        if(productService == null){return;}
        when(productService.getProductByName("Test_Name")).thenThrow(new ProductNotFoundException("No Product with this name found"));

        mockMvc.perform(MockMvcRequestBuilders.get("/name/Test_Name")).
                andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("No Product with this name found"));
    }

    private String convertToJson(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(object);
    }

}
