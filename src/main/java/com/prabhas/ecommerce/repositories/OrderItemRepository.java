package com.prabhas.ecommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.prabhas.ecommerce.models.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
