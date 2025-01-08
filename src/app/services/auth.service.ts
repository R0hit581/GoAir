import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private readonly jwtTokenKey = 'jwtToken';
  private apiUrl = 'http://localhost:8086/';

  constructor(private http: HttpClient) {}

  // Check if sessionStorage is available (for client-side only)
  private isBrowser(): boolean {
    return (
      typeof window !== 'undefined' &&
      typeof window.sessionStorage !== 'undefined'
    );
  }

  setToken(token: string): void {
    if (this.isBrowser()) {
      sessionStorage.setItem(this.jwtTokenKey, token);
    }
  }

  getToken(): string | null {
    if (this.isBrowser()) {
      const token = sessionStorage.getItem(this.jwtTokenKey);
      if (token && this.isTokenExpired(token)) {
        this.clearToken();
        return '';
      }
      return token;
    }
    return null; // Return null for SSR
  }

  clearToken(): void {
    if (this.isBrowser()) {
      sessionStorage.removeItem(this.jwtTokenKey);
    }
  }

  isTokenExpired(token: string): boolean {
    if (!token) {
      return true;
    }
    const tokenPayload = JSON.parse(atob(token.split('.')[1]));
    const expiry = new Date(tokenPayload.exp * 1000);
    return expiry <= new Date();
  }

  getUserRole(): string | null {
    const token = this.getToken();
    if (!token) {
      return null;
    }
    const payload = JSON.parse(atob(token.split('.')[1]));
    return payload.role;
  }

  getUserEmail(): string | null {
    const token = this.getToken();
    if (!token) {
      return null;
    }
    const payload = JSON.parse(atob(token.split('.')[1]));
    return payload.email;
  }

  getUserName(): string | null {
    const token = this.getToken();
    if (!token) {
      return null;
    }
    const payload = JSON.parse(atob(token.split('.')[1]));
    return payload.sub;
  }

  getUserByEmail(email: string): Observable<any> {
    const tkn = this.getToken();
    if (!tkn) {
      throw new Error('Token is missing or expired');
    }
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        Authorization: 'Bearer ' + tkn,
      }),
    };
    return this.http.get<any>(
      this.apiUrl + 'api/user/getUser/' + email,
      httpOptions
    );
  }

  updateUser(user: {
    name: string;
    phone: string;
    password: string;
    email: string;
    role: string;
  }): Observable<string> {
    const tkn = this.getToken();
    if (!tkn) {
      throw new Error('Token is missing or expired');
    }
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        Authorization: 'Bearer ' + tkn,
      }),
      responseType: 'text' as 'json',
    };

    return this.http.put<string>(
      this.apiUrl + 'api/user/updateUser',
      user,
      httpOptions
    );
  }
}
