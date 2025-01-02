package com.flight.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.flight.dto.Booking;


@FeignClient(name="BOOK-SERVICE/book")
public interface BookFeign {

    @GetMapping("/getByFlightId/{flightId}")
    public List<Booking> getBookingByFlightId(@PathVariable("flightId") int flightId);
}