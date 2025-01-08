package com.login.service;

import com.login.dto.AuthDto;
import com.login.entities.User;
import com.login.exception.InvalidCredentialException;
import com.login.exception.UserAlreadyPresentException;


public interface UserService {

    /*
     * This method is responsible for registering a new user.
     * It returns a Boolean indicating whether the registration was successful.
     * Throws UserAlreadyPresentException if a user with the same email or phone already exists.
     */
    public Boolean registerUser(User user) throws UserAlreadyPresentException;

    /*
     * This method is responsible for logging in an existing user.
     * It accepts an AuthDto containing the login credentials (email and password).
     * Returns a JWT token string if the login is successful.
     * Throws InvalidCredentialException if the email or password is incorrect.
     */
    public String login(AuthDto loginUser) throws InvalidCredentialException;
}