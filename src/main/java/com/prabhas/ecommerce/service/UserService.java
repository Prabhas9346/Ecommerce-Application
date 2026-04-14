package com.prabhas.ecommerce.service;

import com.prabhas.ecommerce.models.Users;
import com.prabhas.ecommerce.repositories.UsersRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

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

}
