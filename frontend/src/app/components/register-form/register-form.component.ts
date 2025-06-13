import { Component } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
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

  showDialog() {
    this.visible = true;
  }

  // Keep both loginForm and signupForm for compatibility
  loginForm: FormGroup;
  signupForm: FormGroup;

  constructor(private fb: FormBuilder, private AuthService: AuthService, private router: Router) {
    this.loginForm = this.fb.group({
      username: [''],
      name: [''],
      familyName: [''],
      password: [''],
      nationality: ['Morocco'],
      accessionDate: [''],
      indentityNumber: [''],
      indentityDocumentType: ['CIN'],
      agreeToTerms: [false]
    });

    // signupForm points to the same form for compatibility
    this.signupForm = this.loginForm;
  }

  onSubmit() {
    this.AuthService.register(this.loginForm);
  }

  switchToLogin(): void {
    // Switch to login form
    this.router.navigate(['/login']);
  }

  // Password strength calculation
  getPasswordStrength(): number {
    const password = this.loginForm.get('password')?.value || '';
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
