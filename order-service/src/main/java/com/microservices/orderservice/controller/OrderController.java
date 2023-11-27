package com.microservices.orderservice.controller;

import com.microservices.orderservice.dto.OrderLineItemDto;
import com.microservices.orderservice.dto.OrderRequest;
import com.microservices.orderservice.exception.OrderNotFoundException;
import com.microservices.orderservice.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String placeOrder(@RequestBody OrderRequest orderRequest){
        orderService.placeOrder(orderRequest);
        return "Order placed successfully";
    }

    @RequestMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OrderLineItemDto getOrderLineItemBySkuCode(@PathVariable("id") Long id) throws OrderNotFoundException {
        return orderService.findOrderLineItem(id);
    }
}
