package com.microservices.productservice.dto;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;

@Data
public class GenericProductDto {
    private String id;
    private String name;
    private String description;
    private BigDecimal price;
}
