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
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        // Handle null input
        if (identifier == null) {
            throw new UsernameNotFoundException("Identifier cannot be null");
        }
        
        // Clean up identifier (trim and remove quotes, but let database handle case)
        String cleanedIdentifier = cleanIdentifier(identifier);
        
        // Try email first, then fallback to username (both case-insensitive)
        Users user = userService.fetchByEmail(cleanedIdentifier);
        if (user == null) {
            user = userService.fetchByUsername(cleanedIdentifier);
        }
        
        if (user != null) {
            return new CustomUserDetails(user);
        }
        throw new UsernameNotFoundException("User not found for email/username: " + identifier);
    }
    
    private String cleanIdentifier(String identifier) {
        if (identifier == null) return null;
        
        // Trim whitespace and remove quotes
        String cleaned = identifier.trim();
        cleaned = cleaned.replaceAll("['\"]", ""); // Remove quotes
        
        return cleaned;
    }
}
