
import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Injectable({
    providedIn: 'root'
})
export class ManagerAuthGuard implements CanActivate {

    constructor(private router: Router, private authservice : AuthService) { }

    canActivate(
        next: ActivatedRouteSnapshot,
        state: RouterStateSnapshot): boolean {
        const ismanager = this.authservice.authenticatedUser.value.role === 'MANAGER';
        if (!ismanager) {
            this.router.navigate(['/']);
            return false;
        }
        return true;
    }
}
