package com.microservices.orderservice.service;

import com.microservices.orderservice.Mapper.OrderLineItemMapper;
import com.microservices.orderservice.Mapper.OrderMapper;
import com.microservices.orderservice.dto.InventoryResponse;
import com.microservices.orderservice.dto.OrderLineItemDto;
import com.microservices.orderservice.dto.OrderRequest;
import com.microservices.orderservice.dto.OrderResponse;
import com.microservices.orderservice.exception.InvalidIdException;
import com.microservices.orderservice.exception.OrderNotFoundException;
import com.microservices.orderservice.model.OrderLineItem;
import com.microservices.orderservice.model.Order;
import com.microservices.orderservice.repository.OrderLineRepository;
import com.microservices.orderservice.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static com.microservices.orderservice.Mapper.OrderMapper.*;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;
    private final OrderLineRepository orderLineRepository;

    public OrderService(OrderRepository orderRepository, WebClient.Builder webClientBuilder, OrderLineRepository orderLineRepository) {
        this.orderRepository = orderRepository;
        this.webClientBuilder = webClientBuilder;
        this.orderLineRepository = orderLineRepository;
    }

    public String placeOrder(OrderRequest orderRequest){
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());           // generating the order number

        List<OrderLineItem> orderLineItemList = orderRequest.getOrderLineItemDtos().stream()
                .map(this::mapToDto).toList();

        order.setOrderLineItems(orderLineItemList);
        List<String> skuCodes = order.getOrderLineItems().stream()
                                .map(OrderLineItem::getSkuCode).toList();
        //Getting the skuCodes of all the orders into a list to pass it to the inventory service

        // Call Inventory service and place order if product is in stock
        InventoryResponse[] inventoryResponseArray = webClientBuilder.build().get()
                                    .uri("http://inventory-service/api/inventory",
                                            uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())//passing the skucodes to inventory service using the webclient
                                    .retrieve()
                                    .bodyToMono(InventoryResponse[].class)
                                    .block();
        assert inventoryResponseArray != null;
        boolean allProductInStock = Arrays.stream(inventoryResponseArray).allMatch(InventoryResponse::isInStock);
        if (allProductInStock){
            orderRepository.save(order);
            return "Order placed successfully";
        }
        else {throw  new IllegalArgumentException("Product is not in stock please try again later");}
    }

    private OrderLineItem mapToDto(OrderLineItemDto orderLineItemDto) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setPrice(orderLineItemDto.getPrice());
        orderLineItem.setQuantity(orderLineItemDto.getQuantity());
        orderLineItem.setSkuCode(orderLineItemDto.getSkuCode());
        return orderLineItem;
    }

    public OrderLineItemDto findOrderLineItem(Long id) throws OrderNotFoundException {
        if(id == null)
        {
            throw new InvalidIdException("The Id provided is not valid");
        }
        OrderLineItem orderLineItem = orderLineRepository.getOrderLineItemById(id);

        if(orderLineItem == null)
        {
            throw new OrderNotFoundException("Order with given Id is not present");
        }
        return OrderLineItemMapper.orderLineItemToDto(orderLineItem);
    }


    public List<OrderResponse> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        List<OrderResponse> orderResponses = new ArrayList<>();
        if(orders.isEmpty()){return orderResponses;}
        for(Order order:orders)
        {
            OrderResponse orderResponse = orderToOrderResponse(order);
            orderResponses.add(orderResponse);
        }

        return orderResponses;
    }
}
