package com.contact.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a booking made for a flight, including details such as 
 * booking ID, flight ID, seat allocation, and costs.
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
     * Number of bookings made in this transaction.
     */
	private int noOfBooking;
	
    /**
     * Identifier of the flight associated with the booking.
     */
	private int flightId;
	
    /**
     * Number of business class seats booked.
     */
	private int businessSeats;
	
    /**
     * Number of economy class seats booked.
     */
	private int economySeats;
	
    /**
     * Email of the user who made the booking.
     */
	private String bookingEmail;
	
    /**
     * Name of the user who made the booking.
     */
	private String userName;
	
    /**
     * Date on which the booking was made.
     */
	private LocalDate bookingDate;
	
    /**
     * Total cost of the booking.
     */
	private double totalCost;
	
    /**
     * Refund amount in case of cancellation or adjustments.
     */
	private double refundAmount;
}