package com.flight.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

  
    private int bookingId;

    private int noOfBooking;
    private int flightId;
    private int businessSeats;
    private int economySeats;

    private LocalDate bookingDate;

    private double economyPrice;
    private double businessPrice;
    private double totalCost;
    private double refundAmount;

    private String bookingEmail;
    private String userName;

}