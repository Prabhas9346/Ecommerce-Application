package com.prabhas.ecommerce.beans;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductCreateRequest {

    private String name;
    private String description;

    private Double price;
    private Integer stock;

    private String imageUrl;

    private Long categoryId;
}
