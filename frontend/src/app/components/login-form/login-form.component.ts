import { Component, OnInit, Input, Output, EventEmitter, OnDestroy, ChangeDetectorRef } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { Subscription, timer } from 'rxjs';

@Component({
  selector: 'app-login-form',
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.css']
})
export class LoginFormComponent implements OnInit, OnDestroy {
  @Input() visible: boolean = false;
  @Output() visibleChange = new EventEmitter<boolean>();

  loginForm: FormGroup;
  isLoading: boolean = false;
  errorMessage: string = '';
  showPassword: boolean = false;
  rememberMe: boolean = false;
  passwordStrength: 'weak' | 'medium' | 'strong' | null = null;

  private subscriptions: Subscription[] = [];

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {
    this.loginForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  ngOnInit(): void {
    // Set visible to true when used as a routed component
    this.visible = true;

    // Add smooth entrance animation
    if (this.visible) {
      setTimeout(() => {
        this.cdr.detectChanges();
      }, 100);
    }

    // Monitor password strength
    const passwordControl = this.loginForm.get('password');
    if (passwordControl) {
      const passwordSub = passwordControl.valueChanges.subscribe(value => {
        this.updatePasswordStrength(value);
      });
      this.subscriptions.push(passwordSub);
    }

    // Load remembered credentials
    this.loadRememberedCredentials();
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }

  onSubmit(): void {
    if (this.loginForm.valid && !this.isLoading) {
      this.isLoading = true;
      this.errorMessage = '';

      const { username, password } = this.loginForm.value;

      const loginSub = this.authService.login(username, password).subscribe({
        next: (token: any) => {
          // Handle remember me
          if (this.rememberMe) {
            this.saveCredentials(username);
          } else {
            this.clearSavedCredentials();
          }

          // Wait for user data to be loaded, then navigate based on role
          setTimeout(() => {
            this.isLoading = false;
            this.closeDialog();
            this.navigateBasedOnRole();
          }, 1000); // Increased timeout to ensure user data is loaded
        },
        error: (error: any) => {
          // Error is handled in auth service with proper messages
          // Add error animation delay
          setTimeout(() => {
            this.isLoading = false;
            this.shakeForm();
          }, 800);
        }
      });

      this.subscriptions.push(loginSub);
    } else {
      // Mark all fields as touched to show validation errors
      Object.keys(this.loginForm.controls).forEach(key => {
        this.loginForm.get(key)?.markAsTouched();
      });
    }
  }

  togglePassword(): void {
    this.showPassword = !this.showPassword;
  }

  closeDialog(): void {
    // Add smooth exit animation
    this.visible = false;
    this.visibleChange.emit(false);

    // Reset form after animation
    setTimeout(() => {
      this.resetForm();
    }, 300);
  }

  private updatePasswordStrength(password: string): void {
    if (!password) {
      this.passwordStrength = null;
      return;
    }

    let score = 0;

    // Length check
    if (password.length >= 8) score++;
    if (password.length >= 12) score++;

    // Character variety checks
    if (/[a-z]/.test(password)) score++;
    if (/[A-Z]/.test(password)) score++;
    if (/[0-9]/.test(password)) score++;
    if (/[^A-Za-z0-9]/.test(password)) score++;

    // Pattern checks
    if (!/(.)\1{2,}/.test(password)) score++; // No repeated characters
    if (!/123|abc|qwe/i.test(password)) score++; // No common sequences

    if (score <= 3) {
      this.passwordStrength = 'weak';
    } else if (score <= 6) {
      this.passwordStrength = 'medium';
    } else {
      this.passwordStrength = 'strong';
    }
  }

  private getErrorMessage(error: any): string {
    if (error.status === 401) {
      return 'Invalid username or password. Please try again.';
    } else if (error.status === 403) {
      return 'Your account has been suspended. Please contact support.';
    } else if (error.status === 429) {
      return 'Too many login attempts. Please wait a moment and try again.';
    } else if (error.status === 0) {
      return 'Unable to connect to server. Please check your internet connection.';
    } else {
      return 'An unexpected error occurred. Please try again later.';
    }
  }

  private shakeForm(): void {
    const formElement = document.querySelector('.login-card');
    if (formElement) {
      formElement.classList.add('animate-shake');
      setTimeout(() => {
        formElement.classList.remove('animate-shake');
      }, 600);
    }
  }

  private saveCredentials(username: string): void {
    if (typeof Storage !== 'undefined') {
      localStorage.setItem('aftas_remember_username', username);
      localStorage.setItem('aftas_remember_me', 'true');
    }
  }

  private loadRememberedCredentials(): void {
    if (typeof Storage !== 'undefined') {
      const rememberMe = localStorage.getItem('aftas_remember_me') === 'true';
      const savedUsername = localStorage.getItem('aftas_remember_username');

      if (rememberMe && savedUsername) {
        this.rememberMe = true;
        this.loginForm.patchValue({ username: savedUsername });
      }
    }
  }

  private clearSavedCredentials(): void {
    if (typeof Storage !== 'undefined') {
      localStorage.removeItem('aftas_remember_username');
      localStorage.removeItem('aftas_remember_me');
    }
  }

  private resetForm(): void {
    this.loginForm.reset();
    this.errorMessage = '';
    this.isLoading = false;
    this.showPassword = false;
    this.passwordStrength = null;
    this.rememberMe = false;
  }

  switchToRegister(): void {
    // This method can be used to switch to register form
    // For now, navigate to register route
    this.router.navigate(['/signup']);
  }

  private navigateBasedOnRole(): void {
    const user = this.authService.authenticatedUser.value;

    if (!user || !user.role) {
      // If user data not loaded yet, wait a bit more
      setTimeout(() => this.navigateBasedOnRole(), 500);
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
