package com.flight.entities;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int flightId;

    @NotNull(message = "Flight number cannot be null")
    @Min(value = 1, message = "Flight number must be greater than 0")
    private int flightNumber;

    @NotEmpty(message = "Flight name cannot be empty")
    private String flightName;

    @NotNull(message = "Total seats cannot be null")
    @Min(value = 1, message = "Total seats must be at least 1")
    private int totalSeats;

    @NotNull(message = "Available seats cannot be null")
    @Min(value = 0, message = "Available seats cannot be negative")
    private int availableSeats;

    @NotNull(message = "Business class seats cannot be null")
    @Column(name = "business_class_seats")
    @Min(value = 0, message = "Business class seats cannot be negative")
    private int businessClassSeats;

    @NotNull(message = "Economy class seats cannot be null")
    @Column(name = "economy_class_seats")
    @Min(value = 0, message = "Economy class seats cannot be negative")
    private int economyClassSeats;

    @NotEmpty(message = "Source cannot be blank")
    private String source;

    @NotEmpty(message = "Destination cannot be blank")
    private String destination;

    @NotNull(message = "Take-off time cannot be null")
    private LocalTime takeOff;

    @NotNull(message = "Landing time cannot be null")
    private LocalTime landing;

    @NotNull(message = "Flight date cannot be null")
    private LocalDate flightDate;

    @Min(value = 0, message = "Duration cannot be negative")
    private double duration;

    @NotNull(message = "Business price cannot be null")
    @Min(value = 0, message = "Business price must be non-negative")
    private double businessPrice;

    @NotNull(message = "Economy price cannot be null")
    @Min(value = 0, message = "Economy price must be non-negative")
    private double economyPrice;

    // Uncomment and update the relationship if there are associated entities like bookings or passengers.
    // @OneToMany(mappedBy = "flight", cascade = CascadeType.ALL)
    // @JsonManagedReference
    // private List<Booking> bookings;

}