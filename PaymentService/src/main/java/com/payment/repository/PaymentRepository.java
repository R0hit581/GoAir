package com.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.payment.entities.Payment;


/**
 * Repository interface for performing CRUD operations on the Payment entity.
 * It extends JpaRepository to provide built-in methods for interacting with the database.
 */
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    // JpaRepository provides basic CRUD methods like save, findById, findAll, delete, etc.
}