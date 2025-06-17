import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Injectable({
    providedIn: 'root'
})
export class LoggedInGuard implements CanActivate {

    constructor(private router: Router, private authservice: AuthService) { }

    canActivate(
        next: ActivatedRouteSnapshot,
        state: RouterStateSnapshot): boolean {
        const token = localStorage.getItem('token') || this.authservice.authenticatedUser.value.id;
        if (token) {
            // Redirect based on user role
            this.redirectBasedOnRole();
            return false;
        }
        return true;
    }

    private redirectBasedOnRole(): void {
        const user = this.authservice.authenticatedUser.value;

        if (!user || !user.role) {
            // If user data not loaded yet, redirect to homepage
            this.router.navigate(['/']);
            return;
        }

        switch (user.role) {
            case 'NONE':
                // User needs approval
                this.router.navigate(['/pending-approval']);
                break;
            case 'MANAGER':
                // Manager can access member management
                this.router.navigate(['/managemember']);
                break;
            case 'ADHERENT':
            case 'JURY':
                // Regular users go to competitions
                this.router.navigate(['/competition']);
                break;
            default:
                // Fallback to homepage
                this.router.navigate(['/']);
                break;
        }
    }
}
