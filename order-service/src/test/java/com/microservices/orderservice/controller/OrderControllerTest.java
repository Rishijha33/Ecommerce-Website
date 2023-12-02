package com.microservices.orderservice.controller;

import com.microservices.orderservice.dto.OrderResponse;
import com.microservices.orderservice.model.OrderLineItem;
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
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private OrderService orderService;

    // UT for controllers via API
    @Test
    public void getAllOrderReturnEmptyListWhenNoOrdersAvailable() throws Exception {
        //Arrange
        List<OrderResponse> emptyOrderResponses = new ArrayList<>();
        when(orderService.getAllOrders()).thenReturn(emptyOrderResponses);

        //Act
        mockMvc.perform(MockMvcRequestBuilders.get("/api/order"))                 //Invoking the controller api via mockMvc
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    @Test
    public void getAllOrderReturnOrders() throws Exception {
        //Arrange
        List<OrderResponse> orderResponses = new ArrayList<>();
        List<OrderLineItem> emptyOrderLineItems = new ArrayList<>();
        OrderResponse orderResponse1 = new OrderResponse();
        orderResponse1.setId((long)1010);
        orderResponse1.setOrderNumber("TestOrder");
        orderResponse1.setOrderLineItems(emptyOrderLineItems);

        OrderResponse orderResponse2 = new OrderResponse();
        orderResponse2.setId((long)1011);
        orderResponse2.setOrderNumber("TestOrder2");
        orderResponse2.setOrderLineItems(emptyOrderLineItems);

        orderResponses.add(orderResponse1);
        orderResponses.add(orderResponse2);

        when(orderService.getAllOrders()).thenReturn(orderResponses);


        //Act
        mockMvc.perform(MockMvcRequestBuilders.get("/api/order"))                 //Invoking the controller api via mockMvc
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("[{\"id\":1010,\"orderNumber\":\"TestOrder\",\"orderLineItems\":[]},{\"id\":1011,\"orderNumber\":\"TestOrder2\",\"orderLineItems\":[]}]"));
    }
}
