package com.prabhas.ecommerce.security.service;

import com.prabhas.ecommerce.beans.RefreshToken;
import com.prabhas.ecommerce.repositories.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.function.Function;

@Service
public class JWTService {

    @Value("${jwt.secret}")
    private String secret;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    // 🔹 Access Token (15 mins)
    private static final long ACCESS_TOKEN_EXPIRATION = 1000 * 60 * 15;

    // 🔹 Refresh Token (1 day)
    private static final long REFRESH_TOKEN_EXPIRATION = 1000 * 60 * 60 * 24;

    public String getToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return null;


    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));

    }


    public String getUsername(String token) {
        return extractUsername(token);
    }

    public String extractUsername(String token) {

        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsresolver) {
        final Claims claims = extractAllClaims(token);
        return claimsresolver.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


    public String generateToken(UserDetails userDetails) {
        return buildToken(userDetails, ACCESS_TOKEN_EXPIRATION);
    }


    public String generateRefreshToken(UserDetails userDetails) {
        return buildToken(userDetails, REFRESH_TOKEN_EXPIRATION);
    }


    public String buildToken(UserDetails userDetails, long expiration) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())  // username
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }


    public Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    public boolean isValidRefreshToken(String token) {

        Optional<RefreshToken> storedToken = refreshTokenRepository.findByToken(token);

        return storedToken.isPresent()
                && !storedToken.get().isRevoked()
                && storedToken.get().getExpiryDate().isAfter(LocalDateTime.now());
    }
}
