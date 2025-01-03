package com.passenger.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Passengers {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int passengerId;

	@NotEmpty(message = "Name cannot be empty")
	private String name;

	@NotNull(message = "Age cannot be null")
	private String age;  // Keeping as String based on your original design

	@NotEmpty(message = "Gender cannot be empty")
	private String gender;

	private String preference;

	@NotNull(message = "Booking ID cannot be null")
	private int bookingId;  // Retained as a simple integer without any relation mapping

}