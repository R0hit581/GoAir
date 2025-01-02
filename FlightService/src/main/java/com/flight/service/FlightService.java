package com.flight.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.flight.dto.Booking;
import com.flight.entities.Flight;
import com.flight.exception.InvalidFlightException;
import com.flight.feign.BookFeign;
import com.flight.repository.FlightRepository;

import jakarta.transaction.Transactional;

@Service
public class FlightService {

    private static final Logger logger = LoggerFactory.getLogger(FlightService.class);

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private BookFeign bookFeign;

    @Transactional
    public Flight addFlight(Flight flight) {
        validateFlightDetails(flight);

        if (!flightRepository.findByFlightNumberAndFlightDate(flight.getFlightNumber(), flight.getFlightDate()).isEmpty()) {
            throw new InvalidFlightException("This flight on the specified date already exists.");
        }
        if (flight.getFlightDate().isBefore(LocalDate.now())) {
            throw new InvalidFlightException("Flights cannot be registered for past dates.");
        }

        setFlightDuration(flight);
        return flightRepository.save(flight);
    }

    public ResponseEntity<Flight> getFlightById(int id) {
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new InvalidFlightException("Flight not found with ID: " + id));
        return ResponseEntity.ok(dynamicFlightPrice(flight));
    }

    public ResponseEntity<Flight> updateFlightById(int id, int bookingId) throws Exception {
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new InvalidFlightException("Flight not found with ID: " + id));
        List<Booking> bookings = bookFeign.getBookingByFlightId(id);

        if (bookings.isEmpty()) {
            throw new Exception("No bookings available for this flight ID: " + id);
        }

        updateSeatAvailability(flight, bookings, bookingId);
        return ResponseEntity.ok(flightRepository.save(flight));
    }

    public ResponseEntity<List<Flight>> getBySourceAndDestination(String source, String destination) {
        List<Flight> flights = flightRepository.findBySourceAndDestination(source, destination);
        return ResponseEntity.ok(dynamicFlightPrice(validFlights(flights)));
    }

    public ResponseEntity<List<Flight>> getBySourceAndDestinationAndDate(String source, String destination, LocalDate date) {
        List<Flight> flights = flightRepository.findBySourceAndDestinationAndFlightDate(source, destination, date);
        return ResponseEntity.ok(dynamicFlightPrice(validFlights(flights)));
    }

    public ResponseEntity<Boolean> setFlightDetailsAfterBookingUpdate(int flightId, String preference) {
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new InvalidFlightException("Flight not found with ID: " + flightId));

        updateSeatsBasedOnPreference(flight, preference);
        flightRepository.save(flight);
        return ResponseEntity.ok(true);
    }

    public ResponseEntity<List<Flight>> getAllFlights() {
        List<Flight> flights = flightRepository.findAll();
        return ResponseEntity.ok(dynamicFlightPrice(validFlights(flights)));
    }

    public ResponseEntity<List<Flight>> getAllFlightsForAdmin() {
        return ResponseEntity.ok(flightRepository.findAll());
    }

    public ResponseEntity<Flight> updateFlightForAdmin(Flight flight) {
        if (flight.getFlightDate().isBefore(LocalDate.now())) {
            throw new InvalidFlightException("Flights cannot be registered for past dates.");
        }

        setFlightDuration(flight);
        return ResponseEntity.ok(flightRepository.save(flight));
    }

    // Helper Methods
    private void validateFlightDetails(Flight flight) {
        if (flight.getBusinessClassSeats() + flight.getEconomyClassSeats() != flight.getTotalSeats() ||
            flight.getAvailableSeats() != flight.getTotalSeats()) {
            throw new InvalidFlightException("Incorrect flight seat details provided.");
        }
    }

    private void setFlightDuration(Flight flight) {
        double duration = Duration.between(flight.getTakeOff(), flight.getLanding()).toMinutes() / 60.0;
        duration = duration < 0 ? 24 + duration : duration;
        flight.setDuration(duration);
    }

    private void updateSeatAvailability(Flight flight, List<Booking> bookings, int bookingId) {
        int totalBookedSeats = bookings.stream().mapToInt(Booking::getNoOfBooking).sum();
        int businessSeats = bookings.stream()
                .filter(b -> b.getBookingId() == bookingId)
                .mapToInt(Booking::getBusinessSeats).sum();
        int economySeats = bookings.stream()
                .filter(b -> b.getBookingId() == bookingId)
                .mapToInt(Booking::getEconomySeats).sum();

        flight.setBusinessClassSeats(flight.getBusinessClassSeats() - businessSeats);
        flight.setEconomyClassSeats(flight.getEconomyClassSeats() - economySeats);
        flight.setAvailableSeats(flight.getTotalSeats() - totalBookedSeats);
    }

    private void updateSeatsBasedOnPreference(Flight flight, String preference) {
        if ("economy".equalsIgnoreCase(preference)) {
            flight.setEconomyClassSeats(flight.getEconomyClassSeats() + 1);
        } else {
            flight.setBusinessClassSeats(flight.getBusinessClassSeats() + 1);
        }
        flight.setAvailableSeats(flight.getAvailableSeats() + 1);
    }

    private List<Flight> validFlights(List<Flight> flights) {
        List<Flight> validFlights = new ArrayList<>();
        for (Flight flight : flights) {
            if (isFlightValid(flight)) {
                validFlights.add(flight);
            }
        }
        return validFlights;
    }

    private boolean isFlightValid(Flight flight) {
        return (flight.getFlightDate().isAfter(LocalDate.now()) ||
                (flight.getFlightDate().isEqual(LocalDate.now()) &&
                 flight.getTakeOff().isAfter(LocalTime.now().plusMinutes(30))));
    }

    private Flight dynamicFlightPrice(Flight flight) {
        List<Flight> updated = dynamicFlightPrice(List.of(flight));
        return updated.isEmpty() ? flight : updated.get(0);
    }

    private List<Flight> dynamicFlightPrice(List<Flight> flights) {
        for (Flight flight : flights) {
            long daysToFlight = ChronoUnit.DAYS.between(LocalDate.now(), flight.getFlightDate());
            if (daysToFlight < 3 || flight.getAvailableSeats() < 10) {
                double priceMultiplier = flight.getAvailableSeats() < 5 ? 1.40 : 1.15;
                flight.setEconomyPrice(flight.getEconomyPrice() * priceMultiplier);
                flight.setBusinessPrice(flight.getBusinessPrice() * priceMultiplier);
            }
        }
        return flights;
    }
}