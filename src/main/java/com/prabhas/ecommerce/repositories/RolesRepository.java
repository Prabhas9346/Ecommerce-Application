package com.prabhas.ecommerce.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prabhas.ecommerce.models.Roles;

public interface RolesRepository extends JpaRepository<Roles, Long> {

    Optional<Roles> findByName(String name);
}
