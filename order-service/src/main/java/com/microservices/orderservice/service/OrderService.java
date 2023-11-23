package com.microservices.orderservice.service;

import com.microservices.orderservice.dto.InventoryResponse;
import com.microservices.orderservice.dto.OrderLineItemDto;
import com.microservices.orderservice.dto.OrderRequest;
import com.microservices.orderservice.model.OrderLineItem;
import com.microservices.orderservice.model.Order;
import com.microservices.orderservice.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient webClient;

    public OrderService(OrderRepository orderRepository, WebClient webClient) {
        this.orderRepository = orderRepository;
        this.webClient = webClient;
    }

    public void placeOrder(OrderRequest orderRequest){
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());           // generating the order number

        List<OrderLineItem> orderLineItemList = orderRequest.getOrderLineItemDtos().stream()
                .map(this::mapToDto).toList();

        order.setOrderLineItems(orderLineItemList);
        List<String> skuCodes = order.getOrderLineItems().stream()
                                .map(OrderLineItem::getSkuCode).toList();
        //Getting the skuCodes of all the orders into a list to pass it to the inventory service

        // Call Inventory service and place order if product is in stock
        InventoryResponse[] inventoryResponseArray = webClient.get()
                                    .uri("http://localhost:8082/api/inventory",
                                            uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())//passing the skucodes to inventory service using the webclient
                                    .retrieve()
                                    .bodyToMono(InventoryResponse[].class)
                                    .block();
        assert inventoryResponseArray != null;
        boolean allProductInStock = Arrays.stream(inventoryResponseArray).allMatch(InventoryResponse::isInStock);
        if (allProductInStock){orderRepository.save(order);}
        else {throw  new IllegalArgumentException("Product is not in stock please try again later");}

    }

    private OrderLineItem mapToDto(OrderLineItemDto orderLineItemDto) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setPrice(orderLineItemDto.getPrice());
        orderLineItem.setQuantity(orderLineItemDto.getQuantity());
        orderLineItem.setSkuCode(orderLineItemDto.getSkuCode());
        return orderLineItem;
    }
}
