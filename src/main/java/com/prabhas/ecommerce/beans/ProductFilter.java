package com.prabhas.ecommerce.beans;

import lombok.Data;

@Data
public class ProductFilter {
    private String name;
    private Double minPrice;
    private Double maxPrice;
    private Long categoryId;
    private Boolean isActive;
}
