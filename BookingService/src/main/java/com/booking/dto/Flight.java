package com.booking.dto;
import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Flight {

    private int flightId;
    private int flightNumber;
    private String flightName;
    private int totalSeats;
    private int availableSeats;
    private String source;
    private String destination;
    private LocalDate flightDate;
    private double duration;
    private int economyClassSeats;
    private int businessClassSeats;    
    private double businessPrice;    
    private double economyPrice;
    private LocalTime takeOff;
    private LocalTime landing;
}
