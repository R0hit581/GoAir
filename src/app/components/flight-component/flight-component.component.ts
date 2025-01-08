import { ChangeDetectorRef, Component, OnInit } from '@angular/core';

import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule, NgForm } from '@angular/forms';
import { CommonModule, DatePipe } from '@angular/common';
import { Route, Router, RouterLink } from '@angular/router';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { FlightService } from '../../services/flight.service';
import { Flight } from '../../models/Flight';
import { DataService } from '../../services/data.service';
import { AuthService } from '../../services/auth.service';
import { AppComponent } from '../../app.component';

@Component({
  selector: 'app-flight',
  standalone: true,
  imports: [
    FormsModule,
    HttpClientModule,
    CommonModule,
    RouterLink,
    FontAwesomeModule,
  ],
  templateUrl: './flight-component.component.html',
  styleUrl: './flight-component.component.css',
  providers: [FlightService, FlightComponent, DatePipe],
})
export class FlightComponent implements OnInit {
  flights: Flight[] = [];
  searchedFlightList: Flight[] = [];
  selectedFilter = 'filters';

  filteredSources: string[] = [];
  filteredDestinations: string[] = [];
  search = {
    source: '',
    destination: '',
    date: '',
  };

  flag = false;
  constructor(
    private flightService: FlightService,

    private dataService: DataService,
    private authService: AuthService,
    private router: Router,
    private appComponent: AppComponent,

    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    if (this.authService.getToken()?.length === 0) {
      this.router.navigateByUrl('/login');
    }
    this.appComponent.isBookingsActive = false;
    this.loadFlights();
    this.appComponent.checkLogin();
    this.appComponent.setNav(true);
    this.cdr.detectChanges();
  }

  loadFlights() {
    this.flightService.getFlights().subscribe((data: Flight[]) => {
      this.flights = data;
    });
  }
  onSourceChange(value: string) {
    this.filteredSources = this.getUniqueSources().filter((source) =>
      source.toLowerCase().includes(value.toLowerCase())
    );
  }

  onDestinationChange(value: string) {
    this.filteredDestinations = this.getUniqueDestinations().filter(
      (destination) => destination.toLowerCase().includes(value.toLowerCase())
    );
  }

  selectSource(source: string) {
    this.search.source = source;
    this.filteredSources = [];
  }

  selectDestination(destination: string) {
    this.search.destination = destination;
    this.filteredDestinations = [];
  }

  getUniqueSources(): string[] {
    return [...new Set(this.flights.map((flight) => flight.source))];
  }

  getUniqueDestinations(): string[] {
    return [...new Set(this.flights.map((flight) => flight.destination))];
  }

  bookFlight(flight: Flight) {
    //console.log(flight);
    this.dataService.setFlight(flight);
  }

  onSearch(searchForm: NgForm) {
    if (searchForm.valid) {
      this.SearchFlightByLocationAndDate(
        this.search.source,
        this.search.destination,
        this.search.date
      );
    } else {
      // Trigger validation messages
      searchForm.form.markAllAsTouched();
    }
  }

  SearchFlightByLocationAndDate(
    source: string,
    destination: string,
    date: string
  ) {
    console.log(
      `Searching for flights from ${source} to ${destination} on ${date}`
    );

    this.flightService
      .getFlightsByRouteAndDate(source, destination, date)
      .subscribe((data: Flight[]) => {
        this.searchedFlightList = data;
        console.log(this.searchedFlightList);
        this.flights = this.searchedFlightList;
      });
  }
  sortFlight(sortCond: string) {
    switch (sortCond) {
      case 'fastest':
        console.log('here 1');
        this.flights.sort((b, a) => b.duration - a.duration);
        // this.loadFlights();
        break;
      case 'cheapest':
        console.log('here cheap');
        this.flights.sort((b, a) => b.economyPrice - a.economyPrice);
        // this.loadFlights();
        break;

      default:
        // Default sorting by flightNumber if it's a number
        this.flights.sort((b, a) => b.flightNumber - a.flightNumber);
        // this.loadFlights();
        break;
    }
  }
}
