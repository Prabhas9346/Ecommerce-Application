package com.prabhas.ecommerce.controller;

import com.prabhas.ecommerce.beans.ProductCreateRequest;
import com.prabhas.ecommerce.beans.ProductUpdateRequest;
import com.prabhas.ecommerce.models.CartItem;
import com.prabhas.ecommerce.models.Product;
import com.prabhas.ecommerce.repositories.CartItemRepository;
import com.prabhas.ecommerce.repositories.ProductRepository;
import com.prabhas.ecommerce.repositories.UsersRepository;
import com.prabhas.ecommerce.service.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth/seller/")
@Validated
public class SellerController {

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    ProductService productService;


    @GetMapping("get/products")

    public ResponseEntity<?> getSellerProducts(@AuthenticationPrincipal UserDetails userDetails, @RequestParam(required = false) Integer pageNo, @RequestParam(required = false) Integer size) {
        String username = userDetails.getUsername();
        return productService.getsellerProducts(username, pageNo, size);

    }

    @GetMapping("get/product/{id}")
    public ResponseEntity<?> getSellerProduct( @PathVariable @Positive(message = "Id must be greater than 0") Long id, @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        return productService.getSellerProduct(id, username);
    }

    @PostMapping("add/product")
    public ResponseEntity<?> addProduct(@Valid @RequestBody ProductCreateRequest product, @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        System.out.println("SELLER API HIT");
        return productService.addProduct(product, username);

    }

    @PutMapping("update/product/{id}")
    public ResponseEntity<?> updateProduct(  @PathVariable @Positive(message = "Id must be greater than 0") Long id, @RequestBody ProductUpdateRequest product, @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        return productService.updateProduct(id, product, username);
    }

    @DeleteMapping("delete/product/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        return productService.deleteProduct(id, username); }
}
