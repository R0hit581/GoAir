package com.contact.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a flight with its details such as ID, name, seats, route, 
 * pricing, and other related information.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Flight {

    /**
     * Unique identifier for the flight.
     */
    private int flightId;

    /**
     * Name of the flight.
     */
    private String flightName;

    /**
     * Total number of seats available on the flight.
     */
    private int totalSeats;

    /**
     * Number of currently available seats.
     */
    private int availableSeats;

    /**
     * Source location from where the flight departs.
     */
    private String source;

    /**
     * Destination location where the flight arrives.
     */
    private String destination;

    /**
     * Date on which the flight is scheduled.
     */
    private LocalDate flightDate;

    /**
     * Duration of the flight in hours.
     */
    private double duration;

    /**
     * Number of seats allocated for economy class.
     */
    private int economyClassSeats;

    /**
     * Number of seats allocated for business class.
     */
    private int businessClassSeats;

    /**
     * Price for a business class ticket.
     */
    private double businessPrice;

    /**
     * Price for an economy class ticket.
     */
    private double economyPrice;
}