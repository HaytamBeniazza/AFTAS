import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, throwError } from 'rxjs';
import { TUser } from '../model/TUser';
import { Router } from '@angular/router';
import { FormGroup } from '@angular/forms';
import { AlertService } from '../components/alerts/alert-service.service';
import { catchError, tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(
    private http: HttpClient,
    private router: Router,
    private alertService: AlertService
  ) {
    this.getUserByToken();
  }

  public url = 'http://localhost:8080/api/v1/auth';
  public authenticatedUser = new BehaviorSubject<TUser>({} as TUser);

  public login(username: string, password: string): Observable<any> {
    return this.http.post<any>(this.url + '/login', { username, password }).pipe(
      tap((response: any) => {
        // Success handling
        localStorage.setItem('token', response.token);
        this.getUserByToken();
        this.alertService.showMsg('🎣 Welcome back to AFTAS! Login successful.');
      }),
      catchError((error) => {
        // Enhanced error handling for login
        let errorMessage = this.getLoginErrorMessage(error);
        this.alertService.showMsg('❌ ' + errorMessage);
        return throwError(() => error);
      })
    );
  }

  getUserByToken() {
    const token = localStorage.getItem('token');
    if (!token) {
      return; // No token, skip authentication
    }

    const headers = new HttpHeaders().set('Authorization', 'Bearer ' + token);

    return this.http.post<TUser>(this.url + '/user', {}, { headers: headers }).subscribe(
      (user: TUser) => {
        this.authenticatedUser.next(user);
        // Don't navigate here - let the component handle navigation
      },
      (error) => {
        console.log('Token validation failed:', error);
        if (error.status === 403 || error.status === 401) {
          localStorage.removeItem('token');
          this.authenticatedUser.next({} as TUser);
        }
        // Fail silently for better UX
      }
    );
  }

  public logout() {
    localStorage.removeItem('token');
    this.authenticatedUser.next({} as TUser);
    this.alertService.showMsg('👋 You have been logged out successfully. See you soon!');
    this.router.navigate(['/']);
  }

  public register(registerForm: FormGroup): Observable<any> {
    return this.http.post<any>(this.url + '/signup', registerForm.value).pipe(
      tap((response: any) => {
        // Success handling
        localStorage.setItem('token', response.token);
        this.getUserByToken();
        this.alertService.showMsg('🎉 Welcome to AFTAS! Your account has been created successfully. Start your fishing journey!');
        // Navigate to homepage after successful registration
        setTimeout(() => {
          this.router.navigate(['/']);
        }, 2000);
      }),
      catchError((error) => {
        // Enhanced error handling for registration
        let errorMessage = this.getRegistrationErrorMessage(error);
        this.alertService.showMsg('❌ ' + errorMessage);
        return throwError(() => error);
      })
    );
  }

  private getLoginErrorMessage(error: any): string {
    switch (error.status) {
      case 401:
        return 'Invalid username or password. Please check your credentials and try again.';
      case 403:
        return 'Your account has been suspended or disabled. Please contact support.';
      case 429:
        return 'Too many login attempts. Please wait a few minutes before trying again.';
      case 404:
        return 'User not found. Please check your username or create a new account.';
      case 0:
        return 'Unable to connect to server. Please check your internet connection.';
      case 500:
        return 'Server error occurred. Please try again in a few moments.';
      default:
        return error.error?.message || 'Login failed. Please try again.';
    }
  }

  private getRegistrationErrorMessage(error: any): string {
    switch (error.status) {
      case 400:
        if (error.error?.message?.includes('username')) {
          return 'Username is invalid or already taken. Please choose a different username.';
        } else if (error.error?.message?.includes('password')) {
          return 'Password does not meet requirements. Please use at least 6 characters.';
        } else if (error.error?.message?.includes('email')) {
          return 'Email address is invalid or already registered.';
        } else {
          return 'Invalid registration data. Please check all fields and try again.';
        }
      case 409:
        return 'Username already exists. Please choose a different username.';
      case 422:
        return 'Registration data validation failed. Please check all required fields.';
      case 0:
        return 'Unable to connect to server. Please check your internet connection.';
      case 500:
        return 'Server error occurred during registration. Please try again later.';
      default:
        return error.error?.message || 'Registration failed. Please try again.';
    }
  }

  // Check if user has pending approval
  public isPendingApproval(): boolean {
    const user = this.authenticatedUser.value;
    return user && user.role === 'NONE';
  }

  // Check if user is approved
  public isApproved(): boolean {
    const user = this.authenticatedUser.value;
    return user && ['ADHERENT', 'MANAGER', 'JURY'].includes(user.role);
  }
}
