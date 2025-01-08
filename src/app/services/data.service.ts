import { Injectable } from '@angular/core';
import { Flight } from '../models/Flight';
import { Booking } from '../models/Booking';

@Injectable({
  providedIn: 'root',
})
export class DataService {
  flightData: Flight;
  totalCost: number = 0;
  booking: Booking;

  constructor() {
    this.flightData = {
      flightId: 0,
      flightNumber: 0,
      flightName: '',
      totalSeats: 0,
      availableSeats: 0,
      businessClassSeats: 0,
      economyClassSeats: 0,
      source: '',
      destination: '',
      takeOff: '',
      landing: '',
      flightDate: '',
      duration: 0,
      businessPrice: 0,
      economyPrice: 0,
    };

    this.booking = {
      userName: '',
      refundAmount: 0,
      bookingId: 0,
      flightId: 0,
      noOfBooking: 0,
      businessSeats: 0,
      economySeats: 0,
      passengersList: [],
      bookingEmail: '',
    };
  }

  setFlight(flight: Flight): void {
    this.flightData = flight;
  }

  getFlight(): Flight {
    return this.flightData;
  }

  setBooking(book: Booking, cost: number): void {
    this.booking = book;
    this.totalCost = cost;
  }

  getBooking(): Booking {
    return this.booking;
  }

  getCost(): number {
    return this.totalCost;
  }
}
