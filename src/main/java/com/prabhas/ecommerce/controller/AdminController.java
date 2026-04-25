package com.prabhas.ecommerce.controller;

import com.prabhas.ecommerce.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/admin/")
@Validated
public class AdminController {

    @Autowired
    AdminService adminService;


    @PostMapping("approve-seller/{id}")
    public ResponseEntity<?> approveSeller(@PathVariable Long id) {
        return adminService.approveSeller(id);

    }
}
