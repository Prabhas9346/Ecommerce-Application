package com.prabhas.ecommerce.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.prabhas.ecommerce.models.Order;
import com.prabhas.ecommerce.models.Users;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUser(Users user);
}
