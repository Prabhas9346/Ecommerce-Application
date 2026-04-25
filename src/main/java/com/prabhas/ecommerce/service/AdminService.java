package com.prabhas.ecommerce.service;

import com.prabhas.ecommerce.models.RequestStatus;
import com.prabhas.ecommerce.models.Roles;
import com.prabhas.ecommerce.models.SellerRequest;
import com.prabhas.ecommerce.models.Users;
import com.prabhas.ecommerce.repositories.RolesRepository;
import com.prabhas.ecommerce.repositories.UsersRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public class AdminService {

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    RolesRepository rolesRepository;


    @Transactional
    public ResponseEntity<?> approveSeller(Long id) {

        Users user = usersRepository.findBysellerRequest_id(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        SellerRequest request = user.getSellerRequest();

        if (request == null) {
            return ResponseEntity.badRequest().body("Seller request not found");
        }

        if (request.getRequestStatus() != RequestStatus.PENDING) {
            return ResponseEntity.badRequest()
                    .body("Request already " + request.getRequestStatus());
        }

        Roles sellerRole = rolesRepository.findByName("ROLE_SELLER")
                .orElseThrow(() -> new RuntimeException("Role not found"));

        // Add role only if not already present
        user.getRoles().add(sellerRole);
        request.setRequestStatus(RequestStatus.APPROVED);

        return ResponseEntity.ok("Request Approved");
    }


    @Transactional
    public ResponseEntity<?> rejectSeller(Long id) {

        Users user = usersRepository.findBysellerRequest_id(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        SellerRequest request = user.getSellerRequest();

        if (request == null) {
            return ResponseEntity.badRequest().body("Seller request not found");
        }

        if (request.getRequestStatus() != RequestStatus.PENDING) {
            return ResponseEntity.badRequest()
                    .body("Request already " + request.getRequestStatus());
        }

        request.setRequestStatus(RequestStatus.REJECTED);

        return ResponseEntity.ok("Request Rejected");
    }
}
