import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private isAuthenticatedSubject = new BehaviorSubject<boolean>(this.hasToken());
  public isAuthenticated$ = this.isAuthenticatedSubject.asObservable();

  constructor(
    private http: HttpClient,
    private router: Router
  ) {}

  // Check if user is authenticated
  isAuthenticated(): boolean {
    return this.hasToken();
  }

  // Store authentication data
  setAuthData(data: any): void {
    localStorage.setItem('auth_data', JSON.stringify(data));
    this.isAuthenticatedSubject.next(true);
  }

  // Get authentication data
  getAuthData(): any {
    const authData = localStorage.getItem('auth_data');
    return authData ? JSON.parse(authData) : null;
  }

  // Clear authentication data
  logout(): void {
    localStorage.removeItem('auth_data');
    this.isAuthenticatedSubject.next(false);
    this.router.navigate(['/']);
  }

  // Helper method to check if token exists
  private hasToken(): boolean {
    return !!localStorage.getItem('auth_data');
  }
}
