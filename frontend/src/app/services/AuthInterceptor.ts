import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpHandler, HttpRequest, HttpErrorResponse } from '@angular/common/http';
import { catchError } from 'rxjs/operators';
import { throwError } from 'rxjs';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  intercept(req: HttpRequest<any>, next: HttpHandler) {
    const token = localStorage.getItem('token');

    // Don't add Authorization header to auth endpoints (login, signup)
    const isAuthEndpoint = req.url.includes('/api/v1/auth/login') ||
                          req.url.includes('/api/v1/auth/signup');

    if (token && !isAuthEndpoint) {
      req = req.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`
        }
      });
    }

    return next.handle(req).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 403) {
          // localStorage.removeItem('token');
        }
        return throwError(error);
      })
    );
  }
}
