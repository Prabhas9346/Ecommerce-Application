package com.prabhas.ecommerce.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.prabhas.ecommerce.models.Cart;
import com.prabhas.ecommerce.models.Users;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUser(Users user);
}
