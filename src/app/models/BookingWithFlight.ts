import { Booking } from './Booking';
import { Flight } from './Flight';

export class BookingWithFlight {
  constructor(public booking: Booking, public flight: Flight) {}
}
