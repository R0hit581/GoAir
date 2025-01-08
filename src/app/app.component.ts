import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import {
  Router,
  RouterLink,
  RouterLinkActive,
  RouterOutlet,
} from '@angular/router';
import { AuthService } from './services/auth.service';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import { FlightComponent } from './components/flight-component/flight-component.component';
import { HttpClientModule } from '@angular/common/http';

@Component({
  selector: 'app-root',
  standalone: true,
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
  imports: [
    RouterOutlet, // To enable routing
    FlightComponent, // Include the flight component
    RouterLink, // For linking navigation
    CommonModule, // For common Angular directives
    FormsModule, // For handling forms
    RouterLinkActive, // For managing the active link state
  ],
})
export class AppComponent implements OnInit {
  // Boolean values to control login, logout, and navbar visibility
  isBookingsActive: boolean = false;
  title = 'goair';
  login = true;
  logout = false;
  nav: boolean = true; // Navbar visibility
  username: string = ''; // Store the username
  userEmail: string = this.authService.getUserEmail() || ''; // Fetch email from auth service
  user = {
    name: '',
    email: '',
    role: '',
    password: '',
    phone: '',
  };

  token: string | null = ''; // Store JWT token
  confirmpassword: string = ''; // Store confirmation password for update

  // Constructor that initializes AuthService, Router, and ChangeDetectorRef
  constructor(
    private authService: AuthService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {
    // Get user details based on email (fetched from authService)
    this.username = this.authService.getUserName()!;
    {
      this.authService.getUserEmail() &&
        this.authService
          .getUserByEmail(this.authService.getUserEmail()!)
          .subscribe((val) => {
            this.user = {
              email: val.email,
              role: 'USER',
              name: val.username,
              password: '',
              phone: val.phone,
            };
            console.log('_____');
            console.log(val);
            console.log('_____');
          });
    }
  }

  ngOnInit(): void {
    // Check if user has a valid token
    this.token = this.authService.getToken();
    if (this.token == null || this.token === '') {
      // If no token, navigate to login page
      this.login = true;
      this.logout = false;
      this.router.navigateByUrl('/login');
    } else {
      // If token exists, update UI based on role
      this.login = false;
      this.logout = true;
      const userRole = this.authService.getUserRole();
      if (userRole === 'ADMIN') {
        this.nav = false; // Hide navigation bar for admin
        this.router.navigateByUrl('/dashboard');
      } else if (userRole === 'USER') {
        this.nav = true; // Show navigation bar for user
        this.router.navigateByUrl('/flight');
      }
    }
    this.cdr.detectChanges();
  }

  // Function to toggle navigation visibility
  setNav(status: boolean) {
    this.nav = status;
    this.cdr.detectChanges();
  }

  // Navigate to booking page
  gotoshow() {
    if (
      this.authService.getToken() != null &&
      this.authService.getToken() != ''
    ) {
      this.isBookingsActive = true;
    }
    this.router.navigateByUrl('/showbookings');
  }

  // Set login state
  checkLogin() {
    this.logout = true;
    this.login = false;
  }

  // Set logout state and clear token
  checkLogout() {
    this.authService.setToken('');
    this.login = true;
    this.logout = false;
    this.username = '';
    this.isBookingsActive = false;
  }

  // Open the user update modal
  openUserModal() {
    const modal = document.getElementById('userModal');
    if (modal) {
      modal.style.display = 'block';
    }
  }

  // Close the user update modal
  closeUserModal() {
    const modal = document.getElementById('userModal');
    if (modal) {
      modal.style.display = 'none';
    }
  }

  // Update user details based on form submission
  updateUser(form: NgForm) {
    if (form.valid && this.confirmpassword === this.user.password) {
      this.authService.updateUser(this.user).subscribe(
        (response) => {
          // Handle successful update
          alert('User updated successfully');
          console.log(response);
          this.authService.setToken(response); // Update the token
          this.username = this.authService.getUserName()!; // Refresh username
          form.reset();
          this.closeUserModal();
        },
        (error) => {
          // Handle update error
          console.error('Error updating user', error);
          alert('Failed to update user');
        }
      );
    } else {
      alert('Passwords do not match');
    }
  }
}
