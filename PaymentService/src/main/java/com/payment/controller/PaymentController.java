package com.payment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.payment.dto.Booking;
import com.payment.entities.TransactionDetails;
import com.payment.service.OrderDetailService;


/**
 * Controller for handling payment-related actions, including creating and saving transaction details.
 */
@RestController
@CrossOrigin("*")  // Allows cross-origin requests from any domain
public class PaymentController {
	
    @Autowired
    OrderDetailService orderDetailService;  // Service to handle order and payment processing

    /**
     * Creates a new transaction based on the amount and booking details.
     *
     * @param amount the payment amount.
     * @param booking the booking details.
     * @return the created transaction details.
     */
    @PostMapping({"/createTransaction/{amount}"})
    public TransactionDetails createTransaction(@PathVariable(name = "amount") Double amount, @RequestBody Booking booking) {
        return orderDetailService.createTransaction(amount, booking);  // Call service to create transaction
    }

    /**
     * Saves the transaction details after the payment is processed.
     *
     * @param booking the booking details.
     * @param totalamount the total amount for the transaction.
     * @param paymentId the unique payment identifier.
     */
    @PostMapping("/saveTransaction/{totalamount}/{paymentId}")
    public void saveTransaction(@RequestBody Booking booking, @PathVariable(name="totalamount") int totalamount, @PathVariable(name="paymentId") String paymentId) {
        System.err.print(booking);  // For debugging purposes, print booking details to console
        orderDetailService.saveTransaction(booking, totalamount, paymentId);  // Call service to save transaction details
    }
}