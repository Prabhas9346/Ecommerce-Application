package com.prabhas.ecommerce.service;

import com.prabhas.ecommerce.models.RequestStatus;
import com.prabhas.ecommerce.models.Roles;
import com.prabhas.ecommerce.models.SellerRequest;
import com.prabhas.ecommerce.models.Users;
import com.prabhas.ecommerce.repositories.RolesRepository;
import com.prabhas.ecommerce.repositories.SellerRequestRepository;
import com.prabhas.ecommerce.repositories.UsersRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ConsumerService {

    @Autowired
    UsersRepository usersRepository;


    @Autowired
    SellerRequestRepository sellerRequestRepository;

    @Autowired
    RolesRepository rolesRepository;

    @Transactional
    public ResponseEntity<?> requestSeller(String username) {

        if (username == null || username.isBlank()) {
            return ResponseEntity.badRequest().body("Username cannot be null or empty");
        }

        Users user = usersRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Roles sellerRole = rolesRepository.findByName("ROLE_SELLER")
                .orElseThrow(() -> new RuntimeException("Role not found"));

        // Already seller
        if (user.getRoles().contains(sellerRole)) {
            return ResponseEntity.badRequest().body("User is already a seller");
        }

        SellerRequest existing = user.getSellerRequest();

        if (existing != null) {

            if (existing.getRequestStatus() == RequestStatus.PENDING) {
                return ResponseEntity.badRequest().body("Seller request already pending");
            }

            if (existing.getRequestStatus() == RequestStatus.REJECTED) {
                existing.setRequestStatus(RequestStatus.PENDING);
                return ResponseEntity.ok("Seller request submitted again");
            }
        }

        // First time request
        SellerRequest newRequest = new SellerRequest();
        newRequest.setConsumer(user);
        newRequest.setRequestStatus(RequestStatus.PENDING);

        user.setSellerRequest(newRequest);

        return ResponseEntity.ok("Seller request accepted");
    }

}
