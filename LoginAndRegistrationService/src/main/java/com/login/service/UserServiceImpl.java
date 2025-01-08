package com.login.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.login.dto.AuthDto;
import com.login.entities.User;
import com.login.exception.InvalidCredentialException;
import com.login.exception.UserAlreadyPresentException;

import com.login.repository.UserRepository;
import com.login.security.Jwtutil;

@Service
public class UserServiceImpl implements UserService {

    // Logger for tracking operations in this class.
    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private PasswordEncoder passwordEncoder;  // For encoding passwords

    @Autowired
    private AuthenticationManager authenticationManager;  // For authenticating users

    @Autowired
    CustomUserDetailsService customUserDetailsService;  // Custom user service for user details loading

    @Autowired
    Jwtutil jwtutil;  // Utility class to generate JWT tokens

    @Autowired
    private UserRepository userRepository;  // User repository to interact with the database

    /*
     * Registers a new user.
     * - Checks if a user already exists with the same email or phone number.
     * - If user exists, throws UserAlreadyPresentException.
     * - If user does not exist, encodes the password and saves the user in the database.
     */
    @Override
    public Boolean registerUser(User user) throws UserAlreadyPresentException {
        // Check if a user with the same email or phone already exists.
        User usByEmail = userRepository.findByEmail(user.getEmail());
        User usByPhone = userRepository.findByPhone(user.getPhone());

        // If a user exists with either the same email or phone, throw an exception.
        boolean flag = false;
        if (usByEmail != null || usByPhone != null) {
            flag = true;
        }

        if (flag) {
            logger.info("User already exists...");
            throw new UserAlreadyPresentException("Can't add user. Already exists. Email or Phone already Exists");
        } else {
            // If user does not exist, encode the password and save the user to the repository.
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            return true;  // Registration successful
        }
    }

    /*
     * Logs in a user by validating credentials (email and password).
     * - Uses AuthenticationManager to authenticate the user.
     * - If authenticated, generates a JWT token and returns it.
     * - Throws InvalidCredentialException if authentication fails.
     */
    @Override
    public String login(AuthDto loginUser) throws InvalidCredentialException {
        String token = null;
        try {
            // Try to authenticate the user with provided credentials (email and password).
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginUser.getEmail(), loginUser.getPassword()));

            if (authentication.isAuthenticated()) {
                // If authentication is successful, fetch the user and generate a JWT token.
                User u = userRepository.findByEmail(loginUser.getEmail());
                token = jwtutil.generateToken(u.getName(), u.getRole(), u.getId(), u.getEmail());
                return token;  // Return the generated JWT token
            } else {
                // If authentication fails, throw InvalidCredentialException.
                throw new InvalidCredentialException("Invalid user name or password");
            }
        } catch (BadCredentialsException e) {
            // If credentials are bad, throw InvalidCredentialException.
            throw new InvalidCredentialException("Invalid user name or password");
        }
    }
}