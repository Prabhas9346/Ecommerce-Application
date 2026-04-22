package com.prabhas.ecommerce.service;

import com.prabhas.ecommerce.beans.RegistrationRequest;
import com.prabhas.ecommerce.models.Roles;
import com.prabhas.ecommerce.models.Users;
import com.prabhas.ecommerce.repositories.UsersRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    UsersRepository usersRepository;

    public UserService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public Users fetchByUsername(String username) {
        Optional<Users>user = usersRepository.findByUsername(username);
        return user.orElse(null);
    }

    public void save(@Valid RegistrationRequest request) {
        Users users = new Users();
        users.setUsername(request.getUserName());
        users.setPassword(request.getPassword());
        users.setEmail(request.getEmail());
        users.setEnabled(true);
        Roles roles = new Roles("ROLE_USER");
        users.setRoles(Set.of(roles));
        users.setAddresses(request.getAddress());
        usersRepository.save(users);
    }
}
