import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-register-form',
  templateUrl: './register-form.component.html',
  styleUrls: ['./register-form.component.css']
})
export class RegisterFormComponent {
  visible: boolean = true;
  showPassword: boolean = false;
  isLoading: boolean = false;

  showDialog() {
    this.visible = true;
  }

  // Registration form matching backend RegisterRequest
  signupForm: FormGroup;

  constructor(private fb: FormBuilder, private AuthService: AuthService, private router: Router) {
    this.signupForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      familyName: ['', [Validators.required, Validators.minLength(2)]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      accessionDate: ['', Validators.required],
      nationality: ['', Validators.required],
      indentityNumber: ['', [Validators.required, Validators.minLength(5)]],
      indentityDocumentType: ['CIN', Validators.required],
      agreeToTerms: [false, Validators.requiredTrue] // Frontend validation only
    });
  }

  onSubmit() {
    if (this.signupForm.valid && !this.isLoading) {
      this.isLoading = true;

      // Prepare data for backend (exclude agreeToTerms)
      const registerData = {
        username: this.signupForm.value.username,
        familyName: this.signupForm.value.familyName,
        password: this.signupForm.value.password,
        accessionDate: this.signupForm.value.accessionDate,
        nationality: this.signupForm.value.nationality,
        indentityNumber: this.signupForm.value.indentityNumber,
        indentityDocumentType: this.signupForm.value.indentityDocumentType
      };

      // Create a temporary form group for the backend call
      const backendForm = this.fb.group(registerData);

      // Use the new auth service with proper error handling
      this.AuthService.register(backendForm).subscribe({
        next: (response) => {
          // Success is handled in the auth service
          this.isLoading = false;
          // Form will be reset and user redirected by auth service
        },
        error: (error) => {
          // Error is handled in the auth service with proper messages
          this.isLoading = false;
          // Keep form data so user can correct errors
        }
      });
    } else {
      // Mark all fields as touched to show validation errors
      Object.keys(this.signupForm.controls).forEach(key => {
        this.signupForm.get(key)?.markAsTouched();
      });
    }
  }

  switchToLogin(): void {
    this.router.navigate(['/login']);
  }

  // Password strength calculation
  getPasswordStrength(): number {
    const password = this.signupForm.get('password')?.value || '';
    let strength = 0;

    if (password.length >= 8) strength += 1;
    if (/[a-z]/.test(password)) strength += 1;
    if (/[A-Z]/.test(password)) strength += 1;
    if (/[0-9]/.test(password)) strength += 1;

    return strength;
  }

  // Password strength text
  getPasswordStrengthText(): string {
    const strength = this.getPasswordStrength();
    if (strength === 0) return 'No password';
    if (strength === 1) return 'Weak';
    if (strength === 2) return 'Fair';
    if (strength === 3) return 'Good';
    return 'Strong';
  }

  // Password strength classes for styling
  getPasswordStrengthClasses(): string {
    const strength = this.getPasswordStrength();
    if (strength === 0) return 'bg-gray-300';
    if (strength === 1) return 'bg-red-500';
    if (strength === 2) return 'bg-yellow-500';
    if (strength === 3) return 'bg-blue-500';
    return 'bg-green-500';
  }
}
