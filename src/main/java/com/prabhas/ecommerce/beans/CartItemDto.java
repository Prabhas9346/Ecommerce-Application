package com.prabhas.ecommerce.beans;


import lombok.Data;

@Data
public class CartItemDto {
    private Long productId;
    private String productName;
    private String imageUrl;
    private double unitPrice;
    private int quantity;
    private double totalPrice;
}
