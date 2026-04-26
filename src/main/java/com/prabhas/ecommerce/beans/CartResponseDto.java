package com.prabhas.ecommerce.beans;


import lombok.Data;
import java.util.List;

@Data
public class CartResponseDto {
    private Long cartId;
    private double totalPrice;
    private List<CartItemDto> items;
}
