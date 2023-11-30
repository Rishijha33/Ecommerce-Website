package com.microservices.orderservice.controller;

import com.microservices.orderservice.dto.OrderLineItemDto;
import com.microservices.orderservice.dto.OrderRequest;
import com.microservices.orderservice.dto.OrderResponse;
import com.microservices.orderservice.exception.OrderNotFoundException;
import com.microservices.orderservice.model.Order;
import com.microservices.orderservice.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CircuitBreaker(name = "inventory", fallbackMethod = "fallBackMethod")
    @TimeLimiter(name = "inventoryTimeLimit", fallbackMethod = "fallBackMethodTimeLimiter")
    @Retry(name = "inventoryRetry")
    public CompletableFuture<String> placeOrder(@RequestBody OrderRequest orderRequest){

        return CompletableFuture.supplyAsync(() ->orderService.placeOrder(orderRequest));  //Using future to implement the time limit function as t
        // his might not return a string if the api times-out
    }

    //Fall back method for the ciruit breaker
    private CompletableFuture<String> fallBackMethod(OrderRequest orderRequest, RuntimeException runtimeException){
        return CompletableFuture.supplyAsync(() -> "Oops! something went wrong, our minions are looking into it, please order after sometime");
    }

    private CompletableFuture<String> fallBackMethodTimeLimiter(OrderRequest orderRequest, TimeoutException runtimeException){
        return CompletableFuture.supplyAsync(() -> "Api timed out please try after sometime");
    }

    @RequestMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OrderLineItemDto getOrderLineItemById(@PathVariable("id") Long id) throws OrderNotFoundException {
        return orderService.findOrderLineItem(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<OrderResponse> getAllOrders()
    {
        List<OrderResponse> orders = orderService.getAllOrders();
        return orders;
    }
}
