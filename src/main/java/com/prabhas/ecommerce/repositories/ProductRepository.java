package com.prabhas.ecommerce.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prabhas.ecommerce.models.Product;
import com.prabhas.ecommerce.models.Category;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategory(Category category);

    List<Product> findByIsActiveTrue();

    List<Product> findByNameContainingIgnoreCase(String keyword);

    Optional<Product> findByName(String smartphone);
}
