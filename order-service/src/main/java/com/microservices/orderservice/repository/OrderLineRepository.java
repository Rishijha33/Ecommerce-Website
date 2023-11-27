package com.microservices.orderservice.repository;

import com.microservices.orderservice.model.OrderLineItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

@Repository
public interface OrderLineRepository extends JpaRepository<OrderLineItem, Long> {



    OrderLineItem getOrderLineItemById (Long id);
}
