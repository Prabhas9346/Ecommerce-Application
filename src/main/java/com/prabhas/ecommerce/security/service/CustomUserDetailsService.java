package com.prabhas.ecommerce.security.service;

import com.prabhas.ecommerce.models.Users;
import com.prabhas.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {


    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users=userService.fetchByUsername(username);
        if(users!=null){
            return new CustomUserDetails(users);
        }
        throw  new UsernameNotFoundException("User not found for username: " + username);
    }
}
