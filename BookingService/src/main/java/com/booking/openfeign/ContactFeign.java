package com.booking.openfeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.booking.entities.Booking;

@FeignClient(name = "ContactUs-Service", url = "http://localhost:8088")
public interface ContactFeign {

    @PostMapping("/bookingConfirmation")
    public String bookingMail(@RequestBody Booking booking);
}