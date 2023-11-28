package com.microservices.orderservice.Mapper;

import com.microservices.orderservice.dto.OrderResponse;
import com.microservices.orderservice.model.Order;

public class OrderMapper {

    public static OrderResponse orderToOrderResponse(Order order)
    {
        OrderResponse orderResponse = new OrderResponse();

        orderResponse.setOrderNumber(order.getOrderNumber());
        orderResponse.setOrderLineItems(order.getOrderLineItems());
        orderResponse.setId(order.getId());

        return orderResponse;
    }
}
