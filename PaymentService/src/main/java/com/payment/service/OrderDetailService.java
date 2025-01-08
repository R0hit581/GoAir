package com.payment.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.payment.dto.Booking;
import com.payment.entities.Payment;
import com.payment.entities.TransactionDetails;
import com.payment.repository.PaymentRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;

/**
 * Service to handle the creation and saving of transactions using Razorpay.
 */
@Service
public class OrderDetailService {

    // Constants for Razorpay keys and currency
	private static final String ORDER_PLACED = "Placed";
	private static final String KEY = "";        // Razorpay API Key
	private static final String KEY_SECRET = ""; // Razorpay API Secret
	private static final String CURRENCY = "INR";                         // Currency type (INR)

    // Injecting PaymentRepository for saving payment details
	@Autowired
	private PaymentRepository paymentRepository;

    /**
     * Creates a new transaction with Razorpay using the provided amount and booking details.
     * 
     * @param amount The amount for the transaction
     * @param booking The booking details
     * @return The transaction details created by Razorpay
     */
	public TransactionDetails createTransaction(Double amount, Booking booking) {
		try {
            // Prepare Razorpay order JSON with amount and currency
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("amount", (int) (amount * 100));  // Amount is in paise (100 paise = 1 INR)
			jsonObject.put("currency", CURRENCY);

            // Create a Razorpay client and initiate the order
			RazorpayClient razorpayClient = new RazorpayClient(KEY, KEY_SECRET);
			Order order = razorpayClient.orders.create(jsonObject);
			System.err.println(order);  // Print order details for debugging

            // Prepare transaction details and return it
			TransactionDetails transactionDetails = prepareTransactionDetails(order);
			System.err.println(transactionDetails);  // Print transaction details for debugging

			return transactionDetails;
		} catch (Exception e) {
			System.out.println(e.getMessage());  // Handle exception and print error message
		}
		return null;
	}

    /**
     * Prepares transaction details from the Razorpay order.
     * 
     * @param order The Razorpay order
     * @return A TransactionDetails object with the order's payment ID, currency, and amount
     */
	private TransactionDetails prepareTransactionDetails(Order order) {
		String paymentId = order.get("id");      // Extract payment ID from the Razorpay order
		String currency = order.get("currency"); // Extract currency from the Razorpay order
		Integer amount = (order.get("amount"));  // Extract amount from the Razorpay order

		// Create a TransactionDetails object and return it
		TransactionDetails transactionDetails = new TransactionDetails(paymentId, currency, amount, KEY);
		return transactionDetails;
	}

    /**
     * Saves the payment transaction to the database after the booking is made.
     * 
     * @param booking The booking details
     * @param totalamount The total amount of the transaction
     * @param paymentId The payment ID for the transaction
     */
	public void saveTransaction(Booking booking, int totalamount, String paymentId) {
		System.err.println(booking);  // Print booking details for debugging

        // Create a new Payment object and set its properties
		Payment pay = new Payment();
		pay.setAmount(totalamount);                   // Set the total amount for the payment
		pay.setBookingId(booking.getBookingId());     // Set the booking ID
		pay.setPaymentId(paymentId);                  // Set the payment ID
		pay.setIsBooked("Booked");                    // Mark as "Booked"
		pay.setUserName(booking.getUserName());       // Set the user's name

        // Save the payment details to the database using the paymentRepository
		paymentRepository.save(pay);
	}
}