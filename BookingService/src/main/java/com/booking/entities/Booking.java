package com.booking.entities;

import java.time.LocalDate;
import java.util.List;

import com.booking.dto.Passengers;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int bookingId;    

    @NotNull(message = "Booking mail is required")
    private String bookingEmail;

    @NotNull(message = "No of bookings cannot be empty")
    private int noOfBooking;

    @NotNull(message = "Flight ID cannot be empty or Invalid flight ID")
    private int flightId;

    @NotNull(message = "Please specify business class seats; if not booking, specify as 0")
    @Column(name = "Business")
    private int businessSeats;

    @NotNull(message = "Please specify economy class seats; if not booking, specify as 0")
    @Column(name = "economy")
    private int economySeats;

    private LocalDate bookingDate;
    private double economyPrice;
    private double businessPrice;    
    private double totalCost;
    private double refundAmount;
    private String userName;

    @Transient
    private List<Passengers> passengersList;
}
