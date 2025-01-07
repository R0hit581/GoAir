package com.booking.dto;

import com.booking.entities.Booking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Passengers {

    private int passengerId;
    private String name;
    private String age;
    private String gender;
    private String preference;    
    private int bookingId;

    private Booking booking;
}