package com.microservices.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalIdCache;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDto {
    private String name;
}
