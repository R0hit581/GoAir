package com.booking.controller;

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

import com.booking.entities.Booking;
import com.booking.entities.BookingAndFlight;
import com.booking.service.BookingService;

import jakarta.ws.rs.Path;

@RestController
@RequestMapping("/book")  // Base URL mapping for all booking-related endpoints
public class BookingController {

    private static final Logger logger = LoggerFactory.getLogger(BookingController.class);
    
    @Autowired
    BookingService bookingService;  // Service layer to handle business logic

    // Endpoint to add a new booking
    @PostMapping("/add")
    public Booking addBooking(@RequestBody Booking booking) {
        logger.info("Adding booking: {}", booking);
        return bookingService.addBooking(booking);  // Call the service method to add booking
    }
    
    // Endpoint to fetch a booking by its ID
    @GetMapping("/getByBookingId/{bookingId}")
    public ResponseEntity<Booking> getBookingById(@PathVariable("bookingId") int bookingId) {
        logger.info("Getting booking by ID: {}", bookingId);
        return bookingService.getBookingById(bookingId);  // Call the service method to get booking by ID
    }
    
    // Endpoint to fetch all bookings for a specific flight by its ID
    @GetMapping("/getByFlightId/{flightId}")
    public List<Booking> getBookingsByFlightId(@PathVariable("flightId") int flightId) throws Exception {
        logger.info("Getting bookings by flight ID: {}", flightId);
        return bookingService.getBookingByFlightId(flightId);  // Call the service to get bookings by flight ID
    }
    
    // Endpoint to cancel a booking by passenger ID (deletes the passenger and updates the booking)
    @DeleteMapping("/deleteByPassengerId/{passengerId}")
    public ResponseEntity<String> cancelBooking(@PathVariable("passengerId") int passengerId) {
        logger.info("Cancelling booking for passenger ID: {}", passengerId);
        return bookingService.deletePassengerById(passengerId);  // Call service to delete passenger
    }
    
    // Endpoint to fetch bookings by the email of the person who made the booking
    @GetMapping("/getBookingsByEmail/{email}")
    public ResponseEntity<List<Booking>> getBookingsByEmail(@PathVariable("email") String bookingEmail) {
        logger.info("Getting bookings by email: {}", bookingEmail);
        return bookingService.getBookingsByEmail(bookingEmail);  // Call service to get bookings by email
    }
    
    // Endpoint to fetch bookings along with flight details by the user's email
    @GetMapping("/getBookingsWithFlightByEmail/{email}")
    public ResponseEntity<List<BookingAndFlight>> getBookingsWithFlightByEmail(@PathVariable("email") String bookingEmail){
        logger.info("Getting bookings with flight details for email: {}", bookingEmail);
        return bookingService.getBookingWithFlightByEmail(bookingEmail);  // Call service to get bookings with flight details
    }
}
