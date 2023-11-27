package com.microservices.orderservice.Mapper;

import com.microservices.orderservice.dto.OrderLineItemDto;

import com.microservices.orderservice.model.OrderLineItem;

public class OrderLineItemMapper {

    static public OrderLineItemDto orderLineItemToDto(OrderLineItem orderLineItem){
        OrderLineItemDto orderLineItemDto = new OrderLineItemDto();
        orderLineItemDto.setId(orderLineItem.getId());
        orderLineItemDto.setPrice(orderLineItem.getPrice());
        orderLineItemDto.setQuantity(orderLineItem.getQuantity());
        orderLineItemDto.setSkuCode(orderLineItem.getSkuCode());

        return orderLineItemDto;
    }
}
