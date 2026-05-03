package com.prabhas.ecommerce.controller;

import com.prabhas.ecommerce.models.SellerRequest;
import com.prabhas.ecommerce.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping("reject-seller/{id}")
    public ResponseEntity<?> rejectSeller(@PathVariable Long id) {
        return adminService.rejectSeller(id);
    }

    @GetMapping("get-seller/request")
    public ResponseEntity<?> getSellerRequest() {
        return adminService.getSellerRequest();
    }


}
