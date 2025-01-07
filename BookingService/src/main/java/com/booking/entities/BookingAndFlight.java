package com.booking.entities;

import com.booking.dto.Flight;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookingAndFlight {
	private Flight flight;
	private Booking booking;
}
