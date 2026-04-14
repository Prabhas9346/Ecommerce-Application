package com.prabhas.ecommerce.security.service;

import com.prabhas.ecommerce.models.Users;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Custom implementation of UserDetails interface to wrap Users entity for Spring Security.
 * This class adapts the Users entity to be compatible with Spring Security's authentication mechanism.
 */
public class CustomUserDetails implements UserDetails {

    private final Users users;

    /**
     * Constructs a new CustomUserDetails wrapping the provided Users entity.
     *
     * @param users the Users entity to be wrapped (must not be null)
     */
    public CustomUserDetails(Users users) {
        this.users = users;

    }

    /**
     * Returns the authorities granted to the user. Cannot return <code>null</code>.
     *
     * @return the authorities, sorted by natural key (never <code>null</code>)
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return users.getRoles().stream().map(role->new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }

    /**
     * Returns the password used to authenticate the user. Can be null if the user has not
     * specified a password (e.g. the user Passkeys instead).
     *
     * @return the password
     */
    @Override
    public @Nullable String getPassword() {
        return users.getPassword();
    }

    /**
     * Returns the username used to authenticate the user. Cannot return
     * <code>null</code>.
     *
     * @return the username (never <code>null</code>)
     */
    @Override
    public String getUsername() {
        return users.getUsername();
    }
}
