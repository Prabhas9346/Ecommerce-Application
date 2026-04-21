package com.prabhas.ecommerce.controller;

import com.prabhas.ecommerce.beans.AuthenticationRequest;
import com.prabhas.ecommerce.beans.JWTResponse;
import com.prabhas.ecommerce.beans.RefreshRequest;
import com.prabhas.ecommerce.repositories.RefreshTokenRepository;
import com.prabhas.ecommerce.security.service.CustomUserDetailsService;
import com.prabhas.ecommerce.security.service.JWTService;
import com.prabhas.ecommerce.service.RefreshTokenService;
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

//    @Autowired
//    private RefreshTokenRepository tokenRepository;

    @Autowired
    private RefreshTokenService refreshTokenService;

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
        refreshTokenService.save(jwtResponse.getRefreshToken(), request.getUsername());


        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("api/public/logout")
    public ResponseEntity<String> logout(@RequestBody RefreshRequest request) {

        String refreshToken = request.getRefreshToken();

        // delete or mark revoked
        refreshTokenService.deleteByToken(refreshToken);

        return ResponseEntity.ok("Logged out successfully");
    }

    @PostMapping("api/public/refresh")
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



}
