export interface Flight {
  flightId: number;
  flightNumber: number;
  flightName: string;
  totalSeats: number;
  availableSeats: number;
  businessClassSeats: number;
  economyClassSeats: number;
  source: string;
  destination: string;
  takeOff: string;
  landing: string;
  flightDate: string;
  duration: number;
  businessPrice: number;
  economyPrice: number;
}
