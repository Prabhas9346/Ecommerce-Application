package com.prabhas.ecommerce.controller;

import com.prabhas.ecommerce.beans.*;
import com.prabhas.ecommerce.security.service.CustomUserDetailsService;
import com.prabhas.ecommerce.security.service.JWTService;
import com.prabhas.ecommerce.service.ProductService;
import com.prabhas.ecommerce.service.RefreshTokenService;
import com.prabhas.ecommerce.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public/")
@Validated
public class PublicController {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    JWTService jwtService;

    @Autowired
    UserService userService;

    @Autowired
    ProductService productService;


    @Autowired
    private RefreshTokenService refreshTokenService;

    @PostMapping("/login")
    public ResponseEntity<JWTResponse> login(@Valid @RequestBody AuthenticationRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        JWTResponse jwtResponse = new JWTResponse();
        jwtResponse.setAccessToken(jwtService.generateToken(userDetails));
        jwtResponse.setRefreshToken(jwtService.generateRefreshToken(userDetails));
        refreshTokenService.save(jwtResponse.getRefreshToken(), request.getUsername());


        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("logout")
    public ResponseEntity<String> logout(@RequestBody RefreshRequest request) {

        String refreshToken = request.getRefreshToken();

        // delete or mark revoked
        refreshTokenService.deleteByToken(refreshToken);

        return ResponseEntity.ok("Logged out successfully");
    }

    @PostMapping("refresh")
    public ResponseEntity<JWTResponse> refresh(@RequestBody RefreshRequest request) {

        String refreshToken = request.getRefreshToken();

        // 1. Validate token
        String username = jwtService.extractUsername(refreshToken);

        if (!jwtService.isValidRefreshToken(refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }


        // 3. Generate new access token
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

        String newAccessToken = jwtService.generateToken(userDetails);

        return ResponseEntity.ok(new JWTResponse(newAccessToken, refreshToken));
    }

    @PostMapping("register")
    public ResponseEntity<?> register(@Valid @RequestBody RegistrationRequest request) {
      return  userService.save(request);
//        return ResponseEntity.ok("Registered successfully");

    }

    @GetMapping("/get/products")
    public ResponseEntity<?> getProducts(@RequestParam(required = false) Integer pageNo, @RequestParam(required = false) Integer size) {
        return productService.getAllProducts(pageNo, size);

    }

    @GetMapping("/get/product/{id}")
    public ResponseEntity<?> getProductDetails(@PathVariable @Positive(message = "Id must be greater than 0") Long id) {
        return productService.getProductDetails(id);

    }

    @GetMapping("/get/products/filter")
    public Object getProducts(
            ProductFilter filter,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    ) {
        return productService.getProducts(filter, page, size);
    }








}
