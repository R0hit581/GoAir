package com.flight.controller;


import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.flight.entities.Flight;
import com.flight.exception.InvalidFlightException;
import com.flight.service.FlightService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/flights")
@AllArgsConstructor
@Validated
public class FlightController {

    private static final Logger logger = LoggerFactory.getLogger(FlightController.class);

    @Autowired
    private final FlightService flightService;

    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addFlight(@Valid @RequestBody Flight flight) {
        Map<String, Object> response = new HashMap<>();
        try {
            logger.info("Received request to add flight: {}", flight);
            Flight savedFlight = flightService.addFlight(flight);
            response.put("status", "success");
            response.put("message", "Flight added successfully");
            response.put("data", savedFlight);
            logger.info("Flight added successfully: {}", savedFlight);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (InvalidFlightException e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            logger.error("Error adding flight: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Flight> getFlightById(@PathVariable("id") int id) {
        logger.info("Getting flight by ID: {}", id);
        return flightService.getFlightById(id);
    }

    @PutMapping("/update/{flightId}/booking/{bookingId}")
    public ResponseEntity<Flight> updateFlightById(@PathVariable("flightId") int flightId,
                                                   @PathVariable("bookingId") int bookingId) throws Exception {
        logger.info("Updating flight with ID: {} for booking ID: {}", flightId, bookingId);
        return flightService.updateFlightById(flightId, bookingId);
    }

    @GetMapping("/get/{source}/{destination}")
    public ResponseEntity<List<Flight>> getBySourceAndDestination(@PathVariable("source") String source,
                                                                   @PathVariable("destination") String destination) {
        logger.info("Getting flights from source: {} to destination: {}", source, destination);
        return flightService.getBySourceAndDestination(source, destination);
    }

    @GetMapping("/get/{source}/{destination}/{date}")
    public ResponseEntity<List<Flight>> getBySourceAndDestinationAndDate(@PathVariable("source") String source,
                                                                          @PathVariable("destination") String destination,
                                                                          @PathVariable("date") String date) throws Exception {
        logger.info("Getting flights from source: {} to destination: {} on date: {}", source, destination, date);
        LocalDate flightDate = LocalDate.parse(date);
        return flightService.getBySourceAndDestinationAndDate(source, destination, flightDate);
    }

    @PostMapping("/set/{flightId}/{preference}")
    public ResponseEntity<Boolean> setFlightDetailsAfterBookingUpdate(@PathVariable("flightId") int flightId,
                                                                       @PathVariable("preference") String preference) throws Exception {
        logger.info("Setting flight details after booking update for flight ID: {} with preference: {}", flightId, preference);
        return flightService.setFlightDetailsAfterBookingUpdate(flightId, preference);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Flight>> findAllFlights() {
        logger.info("Getting all flights");
        return flightService.getAllFlights();
    }

    @GetMapping("/getAllForadmin")
    public ResponseEntity<List<Flight>> findAllFlightsForAdmin() {
        logger.info("Getting all flights for Admin");
        return flightService.getAllFlightsForAdmin();
    }

    @PutMapping("/updateFlightForAdmin")
    public ResponseEntity<Flight> updateFlightForAdmin(@Valid @RequestBody Flight flight) {
        logger.info("Updating flight for Admin");
        return flightService.updateFlightForAdmin(flight);
    }
}
