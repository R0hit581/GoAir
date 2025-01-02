package com.flight.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.flight.entities.Flight;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Integer> {

    // Find flights by source and destination (with pagination and sorting)
    List<Flight> findBySourceAndDestination(String source, String destination);
    
    // Find flights by source, destination, and flight date
    List<Flight> findBySourceAndDestinationAndFlightDate(String source, String destination, LocalDate flightDate);
    
    // Find flights by flight number and flight date
    List<Flight> findByFlightNumberAndFlightDate(int flightNumber, LocalDate flightDate);

    // Optional: If you expect a large list, use pagination and sorting
    Page<Flight> findBySourceAndDestination(String source, String destination, Pageable pageable);

    // Optional: Add method to handle case where flightDate can be null, using the 'Optional' type.
    List<Flight> findBySourceAndDestinationAndFlightDateIsNull(String source, String destination);

}