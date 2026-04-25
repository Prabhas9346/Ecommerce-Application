package com.prabhas.ecommerce.controller;

import com.prabhas.ecommerce.service.ConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("/api/auth/consumer/")
public class ConsumerController {

    @Autowired
    ConsumerService consumerService;

    @PostMapping("seller-requests")
    public ResponseEntity<?> sellerRequest(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();

        return consumerService.requestSeller(username);


    }
}
