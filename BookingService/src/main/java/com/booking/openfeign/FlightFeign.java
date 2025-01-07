package com.booking.openfeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.booking.dto.Flight;

@FeignClient(name = "FLIGHT-SERVICE/flight")
public interface FlightFeign {

	@PutMapping("/update/{flightId}/{bookingId}")
    public Flight updateFlightById(@PathVariable("flightId") int flightId, @PathVariable("bookingId") int bookingId);

    @PostMapping("/set/{flightId}/{preference}")
    public ResponseEntity<Boolean> setFlightDetailsAfterBookingUpdate(@PathVariable("flightId") int flightId,
            @PathVariable("preference") String preference);

    @GetMapping("/get/{id}")
    public ResponseEntity<Flight> getFlightById(@PathVariable("id") int id);
}
