
import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Injectable({
    providedIn: 'root'
})
export class AuthGuard implements CanActivate {

    constructor(private router: Router, private authservice : AuthService) { }

    canActivate(
        next: ActivatedRouteSnapshot,
        state: RouterStateSnapshot): boolean {
        const token = localStorage.getItem('token') || this.authservice.authenticatedUser.value.id;
        if (!token) {
            this.router.navigate(['/login']);
            return false;
        }
        if(this.authservice.authenticatedUser.value.role === 'NONE'){
            this.router.navigate(['/error']);
            return false;
        }
        return true;
    }
}
