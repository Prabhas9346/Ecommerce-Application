package com.prabhas.ecommerce.repositories;

import com.prabhas.ecommerce.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import com.prabhas.ecommerce.models.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    boolean existsByProduct(Product product);
}
