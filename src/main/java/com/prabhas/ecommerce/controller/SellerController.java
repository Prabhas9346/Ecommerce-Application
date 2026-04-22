package com.prabhas.ecommerce.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/seller/")
public class SellerController {

    @GetMapping("get/products")

    public ResponseEntity<?> getSellerProducts(@AuthenticationPrincipal UserDetails userDetails) {

        String username = userDetails.getUsername();



        return ResponseEntity.ok(username);



    }
}
