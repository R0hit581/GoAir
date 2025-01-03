package com.passenger.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.passenger.entities.Passengers;
import com.passenger.exception.InvalidBookingException;
import com.passenger.repository.PassengersRepository;

@Service
public class PassengersService {

    private static final Logger logger = LoggerFactory.getLogger(PassengersService.class);

    @Autowired
    PassengersRepository passengersRepository;

    // Method to add passengers in bulk
    public List<Passengers> addPassengers(List<Passengers> passengersList, int bookingId) {
        // Set the booking ID for each passenger
        for (Passengers passenger : passengersList) {
            passenger.setBookingId(bookingId);
        }

        // Use saveAll to persist all passengers in a single call to the repository
        List<Passengers> savedPassengers = passengersRepository.saveAll(passengersList);
        
        // Log each added passenger
        savedPassengers.forEach(passenger -> {
            logger.info("Added passenger {} for booking ID {}", passenger.getPassengerId(), bookingId);
        });

        return savedPassengers;
    }

    // Method to get passengers by booking ID
    public List<Passengers> getPassengersListByBookingId(int bookingId) {
        List<Passengers> passengersList = passengersRepository.findByBookingId(bookingId);
        if (passengersList.isEmpty()) {
            logger.warn("No passengers found for booking ID {}", bookingId);
            throw new InvalidBookingException("No passengers found for booking ID " + bookingId);
        }
        return passengersList;
    }

    // Method to delete passenger by ID
    public ResponseEntity<List<String>> deletePassengerByPassengerId(int passengerId) {
        try {
            Optional<Passengers> optionalPassenger = passengersRepository.findById(passengerId);
            if (optionalPassenger.isPresent()) {
                Passengers passenger = optionalPassenger.get();
                int bookingId = passenger.getBookingId();
                String preference = passenger.getPreference();
                passengersRepository.deleteById(passengerId);

                List<String> response = new ArrayList<>();
                response.add(String.valueOf(bookingId));
                response.add(preference);

                logger.info("Deleted passenger with ID {}. Booking ID: {}, Preference: {}", passengerId, bookingId,
                        preference);
                return ResponseEntity.ok(response);
            } else {
                logger.warn("Passenger with ID {} not found", passengerId);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error deleting passenger with ID {}", passengerId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}