package com.prabhas.ecommerce.controller;

import com.prabhas.ecommerce.beans.CheckoutRequest;
import com.prabhas.ecommerce.service.ConsumerService;
import com.prabhas.ecommerce.service.ProductService;
import jakarta.validation.constraints.Positive;
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

    @PostMapping("add-product/{id}")
    public ResponseEntity<?> addProduct(@AuthenticationPrincipal UserDetails userDetails, @PathVariable @Positive(message = "Id must be greater than 0") Long id, @RequestBody @Positive(message = "quantity must be greater than 0") Integer quantity) {
        String username = userDetails.getUsername();
        return consumerService.addToCart(username, id, quantity);

    }

    @PutMapping("/cart/items/{productId}")
    public ResponseEntity<?> updateCartItem(@PathVariable Long productId, @RequestParam Integer quantity, @AuthenticationPrincipal UserDetails user) {
        return consumerService.updateCartItem(user.getUsername(), productId, quantity);
    }

    @DeleteMapping("/cart/items/{productId}")
    public ResponseEntity<?> removeCartItem(
            @PathVariable Long productId,
            @AuthenticationPrincipal UserDetails user
    ) {
        return consumerService.removeCartItem(user.getUsername(), productId);
    }

    @GetMapping("/cart")
    public ResponseEntity<?> getCart(@AuthenticationPrincipal UserDetails user) {
        return consumerService.getCart(user.getUsername());
    }

    @PostMapping("/cart/checkout")
    public  ResponseEntity<?> cartCheckout(@AuthenticationPrincipal UserDetails user, @RequestBody CheckoutRequest checkoutRequest) {
        return consumerService.checkout(user.getUsername(), checkoutRequest);
    }




}
