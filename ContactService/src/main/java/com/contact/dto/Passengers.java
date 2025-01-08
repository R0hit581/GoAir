package com.contact.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a passenger associated with a booking, including details
 * such as personal information and preferences.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Passengers {
	
    /**
     * Unique identifier for the passenger.
     */
	private int passengerId;
	
    /**
     * Name of the passenger.
     */
	private String name;
	
    /**
     * Age of the passenger.
     */
	private String age;
	
    /**
     * Gender of the passenger.
     */
	private String gender;
	
    /**
     * Seat or service preference of the passenger.
     */
	private String preference;	
	
    /**
     * Identifier of the booking associated with the passenger.
     */
	private int bookingId;
}