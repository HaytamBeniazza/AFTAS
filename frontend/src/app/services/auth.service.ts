import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { TUser } from '../model/TUser';
import { Router } from '@angular/router';
import { FormGroup } from '@angular/forms';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http: HttpClient, private router: Router) {
    this.getUserByToken();
  }

  public url = 'http://localhost:8080/api/v1/auth';
  public authenticatedUser = new BehaviorSubject<TUser>({} as TUser);
  public login(username: string, password: string) {
    return this.http.post<string>(this.url + '/login', { username, password });
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
        this.router.navigate(['/']);
      },
      (error) => {
        console.log('Authentication failed:', error);
        if (error.status === 403 || error.status === 401) {
          localStorage.removeItem('token');
        }
        // Don't redirect or throw errors - just fail silently for better UX
      }
    );
  }

  public logout() {
    localStorage.removeItem('token');
    this.authenticatedUser.next({} as TUser);
  }
  public register(registerForm : FormGroup) {
    return this.http.post(this.url + '/signup', registerForm.value).subscribe(
      (data: any) => {
        localStorage.setItem('token', data.token);
        this.getUserByToken();
      }
    );
  }
}
