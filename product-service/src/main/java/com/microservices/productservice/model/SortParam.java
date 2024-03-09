package com.microservices.productservice.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class SortParam {
    private String sortParamName;
    private String sortType; // Ascending or Descending
}
