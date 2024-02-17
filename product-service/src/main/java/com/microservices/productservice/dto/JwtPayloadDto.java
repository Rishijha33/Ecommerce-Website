package com.microservices.productservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class JwtPayloadDto {

    @JsonProperty("createdAt")
    private long createdAt;
    @JsonProperty("roles")
    private String[] roles;
    @JsonProperty("expiryAt")
    private long expiryAt;
    @JsonProperty("userId")
    private int userId;
}
