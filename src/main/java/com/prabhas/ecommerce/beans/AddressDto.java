package com.prabhas.ecommerce.beans;

import lombok.Data;

@Data
public class AddressDto {

    private Long id;
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String country;
    private String type;
    private boolean isDefault;
}
