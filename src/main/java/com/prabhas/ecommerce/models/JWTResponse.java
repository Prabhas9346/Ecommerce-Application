package com.prabhas.ecommerce.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JWTResponse {

    private String accessToken;
    private String refreshToken;
}
