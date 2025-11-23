package com.prabhas.ecommerce.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.prabhas.ecommerce.models.CartItem;
import com.prabhas.ecommerce.models.Cart;
import com.prabhas.ecommerce.models.Product;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);
}
