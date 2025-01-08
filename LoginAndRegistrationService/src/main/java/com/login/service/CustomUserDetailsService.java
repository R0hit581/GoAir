package com.login.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.login.entities.User;
import com.login.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    // Logger for debugging and tracking user load operations.
    private Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Autowired
    private UserRepository userRepository;

    /*
     * This method is part of the UserDetailsService interface and is responsible 
     * for loading user details based on the provided username (email in this case).
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        // Log the attempt to load user details for the given username.
        logger.info("Loading user details for username: {}", username);

        // Try to fetch the user from the database by their email (username).
        User user = userRepository.findByEmail(username);

        // If no user is found, throw an exception with an appropriate error message.
        if (user == null) {
            logger.error("User not found: {}", username); // Log an error if the user is not found.
            throw new UsernameNotFoundException("User Not Found: " + username);
        }

        // If the user is found, return a CustomUserDetails object populated with user data.
        return new CustomUserDetails(user);
    }
}