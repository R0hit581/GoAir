package com.booking.openfeign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.booking.dto.Passengers;


@FeignClient(name = "PASSENGER-SERVICE/passenger")
public interface PassengersFeign {

    @PostMapping("/add/{bookingId}")
    public List<Passengers> addPassengersByBookingId(@RequestBody List<Passengers> passengersList,
                                                     @PathVariable("bookingId") int bookingId);

    @GetMapping("/get/{bookingId}")
    public List<Passengers> getPassengersListByBookingId(@PathVariable("bookingId") int bookingId);

    @DeleteMapping("/deleteById/{passengerId}")
    public ResponseEntity<List<String>> deletePassengerByPassengerId(@PathVariable("passengerId") int passengerId);
}