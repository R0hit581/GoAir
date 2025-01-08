import { Passenger } from './Passenger';
export interface Booking {
  bookingId: number;
  flightId: number;
  noOfBooking: number;
  businessSeats: number;
  economySeats: number;
  passengersList: Passenger[];
  bookingEmail: string;
  userName: string;
  refundAmount: number;
}
