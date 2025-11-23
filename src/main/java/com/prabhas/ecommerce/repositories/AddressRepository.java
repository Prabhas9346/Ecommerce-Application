package com.prabhas.ecommerce.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prabhas.ecommerce.models.Address;
import com.prabhas.ecommerce.models.Users;

public interface AddressRepository extends JpaRepository<Address, Long> {

    List<Address> findByUser(Users user);
}
