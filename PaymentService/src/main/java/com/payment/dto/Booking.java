package com.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a booking made by a user, including details like flight, seats, and user information.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    /**
     * Unique identifier for the booking.
     */
    private int bookingId;
    
    /**
     * ID of the flight associated with this booking.
     */
    private int flightId;

    /**
     * Number of bookings made in this transaction.
     */
    private int noOfBooking;

    /**
     * Number of business class seats booked.
     */
    private int businessSeats;

    /**
     * Number of economy class seats booked.
     */
    private int economySeats;

    /**
     * Email address of the user who made the booking.
     */
    private String bookingEmail;

    /**
     * Name of the user who made the booking.
     */
    private String userName;
}