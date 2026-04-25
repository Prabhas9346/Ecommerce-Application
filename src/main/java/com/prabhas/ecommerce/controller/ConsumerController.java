package com.prabhas.ecommerce.controller;

import com.prabhas.ecommerce.service.ConsumerService;
import com.prabhas.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping("/api/auth/consumer/")
public class ConsumerController {

    @Autowired
    ConsumerService consumerService;

    @Autowired
    ProductService productService;

    @PostMapping("seller-requests")
    public ResponseEntity<?> sellerRequest(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();

        return consumerService.requestSeller(username);


    }


}
