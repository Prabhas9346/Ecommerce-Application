package com.prabhas.ecommerce.beans;

import lombok.Data;
@Data
public class ProductResponseDto {
    private Long id;
    private String name;
    private String description;
    private double price;
    private double stock;
    private String imageUrl;
    private boolean isActive;
    private Long sellerId;
    private Long categoryId;
}
