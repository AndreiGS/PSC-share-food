import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap, BehaviorSubject } from 'rxjs';
import { User } from '../models/user.model';
import { API_ENDPOINTS } from '../config/api-config';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private currentUserSubject = new BehaviorSubject<User | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();

  constructor(
    private http: HttpClient,
    private router: Router
  ) {
    // Load user from localStorage on service initialization
    this.loadUserFromStorage();
  }

  private loadUserFromStorage(): void {
    const userJson = localStorage.getItem('user');
    if (userJson) {
      try {
        const user = JSON.parse(userJson);
        this.currentUserSubject.next(user);
      } catch (e) {
        console.error('Error parsing user data from localStorage', e);
        this.logout();
      }
    }
  }

  public processOAuthCallback(provider: string, code: string): Observable<any> {
    return this.http.post(`${API_ENDPOINTS.AUTH.OAUTH_CALLBACK}?provider=${provider}&code=${code}`, {}, {
      withCredentials: true
    }).pipe(
      tap((response: any) => {
        if (response) {
          // Store user data from response
          const user: User = {
            id: response.id,
            username: response.username,
            email: response.email,
            roles: Array.from(response.roles || []),
            authorities: Array.from(response.authorities || [])
          };

          this.setUserData(user);
        }
      })
    );
  }

  private setUserData(user: User): void {
    localStorage.setItem('user', JSON.stringify(user));
    localStorage.setItem('is_authenticated', 'true');
    this.currentUserSubject.next(user);
  }

  public logout(): void {
    // Call logout endpoint if needed
    this.http.post(API_ENDPOINTS.AUTH.LOGOUT, {}, { withCredentials: true })
      .subscribe({
        next: () => this.clearUserData(),
        error: () => this.clearUserData() // Still clear data on error
      });
  }

  private clearUserData(): void {
    localStorage.removeItem('user');
    localStorage.removeItem('is_authenticated');
    this.currentUserSubject.next(null);
    this.router.navigate(['/login']);
  }

  public isAuthenticated(): boolean {
    return localStorage.getItem('is_authenticated') === 'true';
  }

  public getCurrentUser(): User | null {
    return this.currentUserSubject.value;
  }

  public getUsername(): string | null {
    const user = this.getCurrentUser();
    return user ? user.username : null;
  }
}
