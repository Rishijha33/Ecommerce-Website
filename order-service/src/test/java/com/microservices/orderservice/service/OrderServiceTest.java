package com.microservices.orderservice.service;

import com.microservices.orderservice.dto.OrderLineItemDto;
import com.microservices.orderservice.exception.InvalidIdException;
import com.microservices.orderservice.exception.OrderNotFoundException;
import com.microservices.orderservice.model.OrderLineItem;
import com.microservices.orderservice.repository.OrderLineRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

import java.math.BigDecimal;

public class OrderServiceTest {
    @Mock  // we need a mock object of the given attribute
    private OrderLineRepository orderLineRepository;

    @InjectMocks//this is the class under test and this is where the mock objects will be injected
    private OrderService orderService;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this); //this will create a fresh mock for each test and will auto close the mocks as well
    }

    @Test
    public void testfindOrderLineItemSuccess() throws OrderNotFoundException {
        //Arrange
        Long mockId = (long) 1;
        OrderLineItem mockOrderLineItem = new OrderLineItem();
        mockOrderLineItem.setId(mockId);
        mockOrderLineItem.setQuantity(3);
        mockOrderLineItem.setSkuCode("testOrder");
        mockOrderLineItem.setPrice(BigDecimal.valueOf(33));
        when(orderLineRepository.getOrderLineItemById(mockId)).thenReturn(mockOrderLineItem);
        //Act
        OrderLineItemDto mockResponse = orderService.findOrderLineItem(mockId);
        //Assert
        Assertions.assertEquals(mockResponse.getId(), mockOrderLineItem.getId());
        Assertions.assertEquals(mockResponse.getPrice(), mockOrderLineItem.getPrice());
        Assertions.assertEquals(mockResponse.getQuantity(), mockOrderLineItem.getQuantity());
        Assertions.assertEquals(mockResponse.getSkuCode(), mockOrderLineItem.getSkuCode());


    }

    @Test
    public void testfindOrderLineItem_RepoReturnsNullObject() throws OrderNotFoundException {
        //Arrange
        Long mockId = null;
        OrderLineItem mockOrderLineItem = new OrderLineItem();
        mockOrderLineItem.setId(mockId);
        mockOrderLineItem.setQuantity(3);
        mockOrderLineItem.setSkuCode("testOrder");
        mockOrderLineItem.setPrice(BigDecimal.valueOf(33));
        when(orderLineRepository.getOrderLineItemById(mockId)).thenReturn(mockOrderLineItem);
        //Act and Assert
        Assertions.assertThrows(InvalidIdException.class, () -> orderService.findOrderLineItem(mockId));  //Asserting the thrown exception here

    }

    @Test
    public void testfindOrderLineItem_NullIdAsInput() {
        //Arrange
        Long mockId = (long) 1;
        when(orderLineRepository.getOrderLineItemById(mockId)).thenReturn(null);

        //Act
        // Assert
        Assertions.assertThrows(OrderNotFoundException.class, () -> orderService.findOrderLineItem(mockId));  //Asserting the thrown exception here

    }
}
