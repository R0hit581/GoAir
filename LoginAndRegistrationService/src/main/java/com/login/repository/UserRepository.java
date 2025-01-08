package com.login.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.login.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    /*
     * Repository interface for User entity.
     * Extends JpaRepository to provide basic CRUD operations and additional query methods.
     */

    User findByName(String username);
    // Custom query method to find a User by their name.

    User findByEmail(String userName);
    // Custom query method to find a User by their email address.

    User findByPhone(String phone);
    // Custom query method to find a User by their phone number.
}