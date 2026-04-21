package com.prabhas.ecommerce.service;

import com.prabhas.ecommerce.beans.RefreshToken;
import com.prabhas.ecommerce.repositories.RefreshTokenRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RefreshTokenService {

    private RefreshTokenRepository refreshTokenRepository;

    RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public void save(String token,String username) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUsername(username);
        refreshToken.setToken(token);
        refreshToken.setRevoked(false);
        refreshToken.setExpiryDate(LocalDateTime.now().plusDays(30));
        refreshToken.setCreatedAt(LocalDateTime.now());
        refreshTokenRepository.save(refreshToken);
    }

    public void deleteByToken(String refreshToken) {
        refreshTokenRepository.deleteByToken(refreshToken);
    }
}
