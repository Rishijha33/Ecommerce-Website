package com.microservices.orderservice.controller;

import com.microservices.orderservice.dto.OrderResponse;
import com.microservices.orderservice.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

@WebMvcTest(OrderController.class)
public class OrderControllerIT {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private OrderService orderService;

    // UT for controllers via API
    @Test
    public void getAllProductReturnEmptyListWhenNoProductsAvailable() throws Exception {
        //Arrange
        List<OrderResponse> emptyOrderResponses = new ArrayList<>();
        when(orderService.getAllOrders()).thenReturn(emptyOrderResponses);

        //Act
        mockMvc.perform(MockMvcRequestBuilders.get("/api/order"))                 //Invoking the controller api via mockMvc
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }
}
