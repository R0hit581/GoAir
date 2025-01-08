package com.contact.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.contact.dto.Booking;
import com.contact.entites.Contact;
import com.contact.service.EmailService;


/**
 * Controller to handle contact and email-related requests.
 */
@RestController
@CrossOrigin("*")
public class ContactController {

    @Autowired
    private EmailService emailService;

    /**
     * Handles the contact form submission and sends an email with the provided details.
     *
     * @param form the contact form details.
     */
    @PostMapping("/contact")
    public void contact(@RequestBody Contact form) {
        System.err.println("here");
        String to = "rohitkushwaha22081@gmail.com";  // Replace with your email
        String subject = form.getSubject();
        String text = "Name: " + form.getName() + "\nMailSentFrom: " + form.getEmail() + "\nMessage: " + form.getMessage();
        emailService.sendEmail(to, subject, text);
        // No return value as the response is implicit.
    }

    /**
     * Sends a booking confirmation email to the user.
     *
     * @param booking the booking details.
     * @return a success message.
     */
    @PostMapping("/bookingConfirmation")
    public String bookingMail(@RequestBody Booking booking) {
        String to = booking.getBookingEmail();  // Email of the user.
        String subject = "Booking Confirmation";
        String text = String.format(
                "Hello %s,\n\n" +
                        "Thank you for your booking. Here are your booking details:\n\n" +
                        "Booking ID: %d\n" +
                        "Number of Bookings: %d\n" +
                        "Flight ID: %d\n" +
                        "Business Seats: %d\n" +
                        "Economy Seats: %d\n" +
                        "Total Cost: %d\n\n" +
                        "We look forward to serving you.\n\n" +
                        "Best regards,\n" +
                        "Thanks for using GoA!r Airline Services",
                        booking.getUserName(),
                        booking.getBookingId(),
                        booking.getNoOfBooking(),
                        booking.getFlightId(),
                        booking.getBusinessSeats(),
                        booking.getEconomySeats(),
                        (int) booking.getTotalCost()
        );
        emailService.sendEmail(to, subject, text);
        return "Message sent successfully";
    }

    /**
     * Sends an OTP email to the specified email address.
     *
     * @param email the recipient's email address.
     * @param otp   the OTP to send.
     * @return a response entity containing a success message.
     */
    @PostMapping("/sendOtp/{email}/{otp}")
    public ResponseEntity<Map<String, String>> sendOtp(@PathVariable("email") String email, @PathVariable("otp") int otp) {
        String to = email;  // Recipient's email address.
        String subject = "OTP Verification";
        String text = "Your otp is " + otp;
        emailService.sendEmail(to, subject, text);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "OTP sent successfully");
        
        return ResponseEntity.ok(response);
    }
}