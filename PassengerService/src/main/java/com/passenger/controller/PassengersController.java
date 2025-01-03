package com.passenger.controller;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.passenger.entities.Passengers;
import com.passenger.service.PassengersService;
@RestController
@RequestMapping("/passenger")
public class PassengersController {

    private static final Logger logger = LoggerFactory.getLogger(PassengersController.class);

    @Autowired
    private PassengersService passengersService;

    // Endpoint to add passengers by booking ID
    @PostMapping("/add/{bookingId}")
    public List<Passengers> addPassengersByBookingId(@RequestBody List<Passengers> passengersList, @PathVariable("bookingId") int bookingId) {
        logger.info("Request to add passengers for booking ID: {}", bookingId);
        return passengersService.addPassengers(passengersList, bookingId);
    }

    // Endpoint to retrieve passengers list by booking ID
    @GetMapping("/get/{bookingId}")
    public List<Passengers> getPassengersListByBookingId(@PathVariable("bookingId") int bookingId) {
        logger.info("Retrieving passengers list for booking ID: {}", bookingId);
        return passengersService.getPassengersListByBookingId(bookingId);
    }

    // Endpoint to delete a passenger by passenger ID
    @DeleteMapping("/deleteById/{passengerId}")
    public ResponseEntity<List<String>> deletePassengerByPassengerId(@PathVariable("passengerId") int passengerId) {
        logger.info("Request to delete passenger with ID: {}", passengerId);
        return passengersService.deletePassengerByPassengerId(passengerId);
    }
}