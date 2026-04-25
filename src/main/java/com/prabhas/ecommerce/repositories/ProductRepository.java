package com.prabhas.ecommerce.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import com.prabhas.ecommerce.models.Product;
import com.prabhas.ecommerce.models.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategory(Category category);

    List<Product> findByIsActiveTrue();

    List<Product> findByNameContainingIgnoreCase(String keyword);

    Optional<Product> findByName(String smartphone);

    ArrayList<Product> findBySeller_username(String username);

    Page<Product> findBySeller_username(String username, Pageable pageable);

    Page<Product>findAll(Specification<Product> spec, Pageable pageable);

    List<Product> findAll(Specification<Product> spec);
}
