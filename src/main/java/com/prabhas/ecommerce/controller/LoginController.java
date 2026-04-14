package com.prabhas.ecommerce.controller;

import com.prabhas.ecommerce.models.AuthenticationRequest;
import com.prabhas.ecommerce.models.JWTResponse;
import com.prabhas.ecommerce.security.service.CustomUserDetailsService;
import com.prabhas.ecommerce.security.service.JWTService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    JWTService jwtService;

    @PostMapping("/api/public/login")
    public ResponseEntity<JWTResponse> login(@RequestBody AuthenticationRequest request) {

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

        return ResponseEntity.ok(jwtResponse);
    }
}
