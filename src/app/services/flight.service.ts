import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, throwError } from 'rxjs';
import { ContactForm } from '../models/ContactForm';
import { AuthService } from './auth.service';
import { TransactionDetails } from '../models/TransactionDetails';
import { BookingWithFlight } from '../models/BookingWithFlight';
import { Flight } from '../models/Flight';
import { Booking } from '../models/Booking';

@Injectable({
  providedIn: 'root',
})
export class FlightService {
  private baseUrlForFlight = 'http://localhost:8086/flight'; // Adjust the URL as per your Spring Boot backend
  private baseUrlForBooking = 'http://localhost:8086/book';

  constructor(private http: HttpClient, private authService: AuthService) {}

  // Fetch all flights
  getFlights(): Observable<Flight[]> {
    const tkn = this.authService.getToken();
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        Authorization: 'Bearer ' + tkn,
      }),
    };
    return this.http.get<Flight[]>(
      `${this.baseUrlForFlight}/getAll`,
      httpOptions
    );
  }

  // Fetch flight by ID
  getFlightById(id: number): Observable<Flight> {
    const tkn = this.authService.getToken();
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        Authorization: 'Bearer ' + tkn,
      }),
    };
    return this.http.get<Flight>(`${this.baseUrlForFlight}/${id}`, httpOptions);
  }

  // Fetch flights by source, destination, and date
  getFlightsByRouteAndDate(
    source: string,
    destination: string,
    date: string
  ): Observable<Flight[]> {
    const tkn = this.authService.getToken();
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        Authorization: 'Bearer ' + tkn,
      }),
    };
    const url = `${this.baseUrlForFlight}/get/${source}/${destination}/${date}`;
    return this.http.get<Flight[]>(url, httpOptions);
  }

  // Add a booking
  addBooking(booking: Booking): Observable<Booking> {
    const tkn = this.authService.getToken();
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        Authorization: 'Bearer ' + tkn,
      }),
    };
    const url = `${this.baseUrlForBooking}/add`;
    return this.http
      .post<Booking>(url, booking, httpOptions)
      .pipe(catchError(this.errorHandler));
  }

  // Create a booking transaction
  createBookingTransaction(
    booking: Booking,
    amount: number
  ): Observable<TransactionDetails> {
    return this.http.post<TransactionDetails>(
      'http://localhost:8087/createTransaction/' + amount,
      booking
    );
  }

  // Error handler
  errorHandler(error: any) {
    let errorMessage = '';
    if (error.error instanceof ErrorEvent) {
      // Client-side error
      errorMessage = error.error.message;
    } else {
      // Server-side error
      errorMessage = `Error Code: ${error.status}\nMessage: ${error.message}`;
    }
    console.log(errorMessage);
    return throwError(() => error);
  }

  // Fetch bookings by email
  getBookingsByEmail(email: string): Observable<Booking[]> {
    const tkn = this.authService.getToken();
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        Authorization: 'Bearer ' + tkn,
      }),
    };
    return this.http.get<Array<Booking>>(
      this.baseUrlForBooking + '/getBookingsByEmail/' + email,
      httpOptions
    );
  }

  // Delete passenger by ID
  deletePassengerById(id: number): Observable<string> {
    const tkn = this.authService.getToken();
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        Authorization: 'Bearer ' + tkn,
      }),
      responseType: 'text' as 'json',
    };
    return this.http
      .delete<string>(
        this.baseUrlForBooking + '/deleteByPassengerId/' + id,
        httpOptions
      )
      .pipe(catchError(this.errorHandler));
  }

  // Add a new flight
  addFlight(flight: Flight): Observable<Flight> {
    const tkn = this.authService.getToken();
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        Authorization: 'Bearer ' + tkn,
      }),
    };
    return this.http.post<Flight>(
      this.baseUrlForFlight + '/add',
      flight,
      httpOptions
    );
  }

  // Fetch flights for admin
  getFlightsForAdmin(): Observable<Flight[]> {
    const tkn = this.authService.getToken();
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        Authorization: 'Bearer ' + tkn,
      }),
    };
    return this.http.get<Flight[]>(
      `${this.baseUrlForFlight}/getAllForAdmin`,
      httpOptions
    );
  }

  // Send contact form data
  sendContactForm(form: ContactForm): Observable<void> {
    return this.http.post<void>('http://localhost:8088/contact', form);
  }

  // Save a transaction
  saveTransaction(
    booking: Booking,
    amount: number,
    paymentId: string
  ): Observable<Booking> {
    return this.http.post<Booking>(
      'http://localhost:8087/saveTransaction/' + amount + '/' + paymentId,
      booking
    );
  }

  // Update flight details
  updateFlight(flight: Flight): Observable<Flight> {
    const tkn = this.authService.getToken();
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        Authorization: 'Bearer ' + tkn,
      }),
    };
    return this.http.put<Flight>(
      this.baseUrlForFlight + '/updateFlightForAdmin',
      flight,
      httpOptions
    );
  }

  // Fetch bookings with flight details by email
  getBookingsWithFlightByEmail(email: string): Observable<BookingWithFlight[]> {
    const tkn = this.authService.getToken();
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        Authorization: 'Bearer ' + tkn,
      }),
    };
    return this.http.get<BookingWithFlight[]>(
      this.baseUrlForBooking + '/getBookingsWithFlightByEmail/' + email,
      httpOptions
    );
  }
}
