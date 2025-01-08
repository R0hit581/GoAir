package com.login.service;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.login.entities.User;



// Custom implementation of UserDetails for integrating Spring Security with the application's user model.
public class CustomUserDetails implements UserDetails {

    private String userName; // Stores the user's name (username) from the database.
    private String password; // Stores the user's password.
    private String role;     // Stores the user's role for authorization.

    // Constructor to initialize CustomUserDetails with data from the User entity.
    public CustomUserDetails(User user) {
        this.userName = user.getName();
        this.password = user.getPassword();
        this.role = user.getRole();
    }

    // Returns the collection of authorities granted to the user.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Wrap the user's role in a SimpleGrantedAuthority and prefix with "ROLE_".
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_" + role));
    }

    // Returns the user's password.
    @Override
    public String getPassword() {
        return this.password;
    }

    // Returns the user's username.
    @Override
    public String getUsername() {
        return this.userName;
    }

    // Custom getter for role (not part of UserDetails interface).
    public String getRole() {
        return this.role;
    }

    // Indicates whether the user's account has expired. Always returns true (non-expired).
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // Indicates whether the user's account is locked. Always returns true (non-locked).
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // Indicates whether the user's credentials (password) have expired. Always returns true (non-expired).
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // Indicates whether the user is enabled. Always returns true (enabled).
    @Override
    public boolean isEnabled() {
        return true;
    }
}