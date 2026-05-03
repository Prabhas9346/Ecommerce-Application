package com.prabhas.ecommerce.beans;



import com.prabhas.ecommerce.models.PaymentMethod;
import lombok.Data;

@Data
public class CheckoutRequest {

    private Long addressId;
    private PaymentMethod paymentMethod; // COD / ONLINE

}
