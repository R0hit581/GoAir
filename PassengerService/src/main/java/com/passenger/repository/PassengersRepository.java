package com.passenger.repository;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.passenger.entities.Passengers;


@Repository
public interface PassengersRepository extends JpaRepository<Passengers, Integer> {
    List<Passengers> findByBookingId(int bookingId);
    
    // Pagination support for retrieving passengers by booking ID
    Page<Passengers> findByBookingId(int bookingId, Pageable pageable);
}