package com.prabhas.ecommerce.beans;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Entity
@Getter
@Setter
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @Column(length = 500)
    private String token;

    private LocalDateTime expiryDate;

    private boolean revoked;

    private LocalDateTime createdAt;

    // getters & setters
}
