package com.booking.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.booking.dto.Flight;
import com.booking.entities.Booking;
import com.booking.entities.BookingAndFlight;
import com.booking.exception.InvalidBookingException;
import com.booking.exception.InvalidFlightException;
import com.booking.openfeign.ContactFeign;
import com.booking.openfeign.FlightFeign;
import com.booking.openfeign.PassengersFeign;
import com.booking.repository.BookingRepository;

@Service
public class BookingService {

    private static final Logger logger = LoggerFactory.getLogger(BookingService.class);

    @Autowired
    ContactFeign contactFeign;  // To handle sending booking confirmation emails

    @Autowired
    BookingRepository bookingRepository;  // Repository to interact with the database for booking operations

    @Autowired
    PassengersFeign passengersFeign;  // Feign client to interact with the Passenger service

    @Autowired
    FlightFeign flightFeign;  // Feign client to interact with the Flight service

    // Method to add a new booking
    public Booking addBooking(Booking booking) {
        booking.setBookingDate(LocalDate.now());  // Set the current date as the booking date
        
        // Validate if the number of passengers matches the specified count
        if ((booking.getNoOfBooking() != booking.getPassengersList().size()) || 
            ((booking.getEconomySeats() + booking.getBusinessSeats()) != booking.getNoOfBooking())) {
            throw new InvalidBookingException("Please specify correct no of passenger details");
        }

        logger.info("Adding booking: {}", booking);

        // Save the booking after fare calculation
        Booking book = bookingRepository.save(bookingFareCalculation(booking));
        
        // Update flight details after booking
        Flight fl = flightFeign.updateFlightById(booking.getFlightId(), booking.getBookingId());
        
        // Add passengers to the booking
        book.setPassengersList(passengersFeign.addPassengersByBookingId(booking.getPassengersList(), booking.getBookingId()));
        
        // Send booking confirmation email
        contactFeign.bookingMail(booking);

        return book;
    }

    // Method to get a booking by its ID
    public ResponseEntity<Booking> getBookingById(int id) {
        Booking book = bookingRepository.findById(id).orElse(null);
        if (book == null) {
            throw new InvalidBookingException("Please specify correct booking Id");
        }
        
        // Fetch passengers for the given booking
        book.setPassengersList(passengersFeign.getPassengersListByBookingId(id));
        logger.info("Retrieved booking by ID: {}", id);
        return ResponseEntity.ok(book);
    }

    // Method to get all bookings for a specific flight
    public List<Booking> getBookingByFlightId(int flightId) {
        List<Booking> bookingList = bookingRepository.findByFlightId(flightId);
        if (bookingList.isEmpty()) {
            throw new InvalidFlightException("Invalid flight id");
        }
        logger.info("Retrieved bookings by flight ID: {}", flightId);
        return bookingList;
    }

    // Method to delete a passenger by their ID and update the booking accordingly
    public ResponseEntity<String> deletePassengerById(int passengerId) {
        List<String> ll = passengersFeign.deletePassengerByPassengerId(passengerId).getBody();
        if (ll != null) {
            logger.info("Passengers deleted for passenger ID: {}", passengerId);
            int bookingId = Integer.parseInt(ll.get(0));
            String preference = ll.get(1);
            
            // Retrieve the booking for the given booking ID
            Optional<Booking> booking1 = bookingRepository.findById(bookingId);
            if (booking1.isEmpty()) {
                throw new InvalidBookingException("Bookings not found with booking Id " + bookingId);
            }
            Booking booking = booking1.get();
            
            // Recalculate refund after passenger deletion
            Booking newBooking = refundCalculator(booking, preference);
            newBooking.setNoOfBooking(newBooking.getNoOfBooking() - 1);
            int flightId = booking.getFlightId();
            bookingRepository.save(newBooking);
            
            // Update flight details after passenger removal
            flightFeign.setFlightDetailsAfterBookingUpdate(flightId, preference);
            logger.info("Booking updated after passenger deletion for booking ID: {}", bookingId);
            return ResponseEntity.ok("Deleted Successfully");
        }
        return ResponseEntity.ok("Passenger could not be deleted");
    }

    // Method to calculate booking fare based on the flight selected
    Booking bookingFareCalculation(Booking booking) {
        Flight flight = flightFeign.getFlightById(booking.getFlightId()).getBody();
        if (flight == null) {
            throw new InvalidBookingException("Flight not found with Id");
        }
        
        // Calculate the fare based on the number of seats and prices
        booking.setEconomyPrice(booking.getEconomySeats() * flight.getEconomyPrice());
        booking.setBusinessPrice(booking.getBusinessSeats() * flight.getBusinessPrice());
        booking.setTotalCost(booking.getBusinessPrice() + booking.getEconomyPrice());
        logger.info("Calculated fare for booking: {}", booking);
        return booking;
    }

    // Method to calculate refund amount based on the flight's departure date and passenger preference
    Booking refundCalculator(Booking booking, String preference) {
        Map<Long, Double> map = new HashMap<>();
        map.put(0L, 0.0);  // Refund based on number of days before flight
        map.put(1L, 0.5);
        map.put(2L, 0.75);
        double current_refund = 0;
        Flight flight = flightFeign.getFlightById(booking.getFlightId()).getBody();
        if (flight != null) {
            long days = ChronoUnit.DAYS.between(LocalDate.now(), flight.getFlightDate());
            days = days < 0 ? 0 : days;
            
            // Handle refund based on passenger preference (economy/business)
            if (preference.equalsIgnoreCase("economy")) {
                if (days < 3) {
                    current_refund = (map.get(days) * (booking.getEconomyPrice() / booking.getEconomySeats()));
                    booking.setRefundAmount(booking.getRefundAmount() + current_refund);
                } else {
                    current_refund = ((booking.getEconomyPrice() / booking.getEconomySeats()) * .8);
                    booking.setRefundAmount(booking.getRefundAmount() + current_refund);
                }
                booking.setEconomyPrice(booking.getEconomyPrice() - (booking.getEconomyPrice() / booking.getEconomySeats()));
                booking.setEconomySeats(booking.getEconomySeats() - 1);
            } else {
                if (days < 3) {
                    current_refund = (map.get(days) * (booking.getBusinessPrice() / booking.getBusinessSeats()));
                    booking.setRefundAmount(booking.getRefundAmount() + current_refund);
                } else {
                    current_refund = ((booking.getBusinessPrice() / booking.getBusinessSeats()) * .8);
                    booking.setRefundAmount((booking.getRefundAmount() + current_refund));
                }
                booking.setBusinessPrice(booking.getBusinessPrice() - (booking.getBusinessPrice() / booking.getBusinessSeats()));
                booking.setBusinessSeats(booking.getBusinessSeats() - 1);
            }
            booking.setTotalCost(booking.getTotalCost() - current_refund);
            logger.info("Calculated refund for booking: {} with preference: {}", booking, preference);
        }
        return booking;
    }

    // Method to get bookings by email
    public ResponseEntity<List<Booking>> getBookingsByEmail(String bookingEmail) {
        List<Booking> listOfBookings = bookingRepository.findByBookingEmail(bookingEmail);
        System.err.println(listOfBookings);
        if (listOfBookings == null) {
            throw new InvalidBookingException("Please specify correct booking email");
        }
        
        // Fetch passengers for each booking
        for (Booking book : listOfBookings) {
            book.setPassengersList(passengersFeign.getPassengersListByBookingId(book.getBookingId()));
        }
        logger.info("Retrieved bookings by email: {}", bookingEmail);
        return ResponseEntity.ok(listOfBookings);
    }

    // Method to get bookings with flight details by email
    public ResponseEntity<List<BookingAndFlight>> getBookingWithFlightByEmail(String bookingEmail) {
        List<Booking> listofBookings = bookingRepository.findByBookingEmail(bookingEmail);
        if (listofBookings == null) {
            throw new InvalidBookingException("Please specify correct booking email");
        }
        
        // Fetch passengers for each booking
        for (Booking book : listofBookings) {
            book.setPassengersList(passengersFeign.getPassengersListByBookingId(book.getBookingId()));
        }
        
        // Create a list of bookings with associated flight details
        List<BookingAndFlight> listofBookingsWithFlight = new ArrayList<>();
        for (Booking booking : listofBookings) {
            Flight fl = flightFeign.getFlightById(booking.getFlightId()).getBody();
            listofBookingsWithFlight.add(new BookingAndFlight(fl, booking));
        }
        
        return ResponseEntity.ok(listofBookingsWithFlight);
    }
}
