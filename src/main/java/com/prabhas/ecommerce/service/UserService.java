package com.prabhas.ecommerce.service;

import com.prabhas.ecommerce.beans.RegistrationRequest;
import com.prabhas.ecommerce.models.Roles;
import com.prabhas.ecommerce.models.Users;
import com.prabhas.ecommerce.repositories.RolesRepository;
import com.prabhas.ecommerce.repositories.UsersRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    RolesRepository rolesRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();



    public Users fetchByUsername(String username) {
        Optional<Users>user = usersRepository.findByUsername(username);
        return user.orElse(null);
    }

//    public void save(@Valid RegistrationRequest request) {
//        Users users = new Users();
//        users.setUsername(request.getUserName());
//        users.setPassword(passwordEncoder.encode(request.getPassword()));
//        users.setEmail(request.getEmail());
//        users.setEnabled(true);
//        Roles roles = new Roles("ROLE_CONSUMER");
//        users.setRoles(Set.of(role s));
//        usersRepository.save(users);
//    }

    public ResponseEntity<?> save(@Valid RegistrationRequest request) {

        // 1. check username/email duplicates
        if (usersRepository.findByUsername(request.getUserName()).isPresent()) {
            return ResponseEntity.badRequest().body("Username already exists. Select some other username");
        }

        if (usersRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email already exists");
        }

        // 2. create user
        Users user = new Users();
        user.setUsername(request.getUserName());
        user.setEmail(request.getEmail());

        // 3. encode password
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user.setEnabled(true);

        // 4. assign default role
        Roles consumerRole = rolesRepository.findByName("ROLE_CONSUMER")
                .orElseThrow(() -> new RuntimeException("Role not found"));

        user.setRoles(Set.of(consumerRole));

        // 5. save user
        usersRepository.save(user);

        return ResponseEntity.ok("User registered successfully");
    }
}
