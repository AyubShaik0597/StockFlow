import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { Auth } from '../../core/services/auth';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './register.html',
  styleUrl: './register.css'
})
export class Register {
  private auth = inject(Auth);
  private router = inject(Router);
  email = '';
  password = '';
  confirmPassword = '';
  organizationName = '';
  errorMessage = '';
  loading = false;

  onRegister(): void {
    this.errorMessage = '';
    if (this.password.length < 8) {
      this.errorMessage = 'Password must contain at least 8 characters.';
      return;
    }
    if (this.password !== this.confirmPassword) {
      this.errorMessage = 'Passwords do not match.';
      return;
    }
    this.loading = true;
    this.auth.register({
      email: this.email.trim(),
      password: this.password,
      organizationName: this.organizationName.trim()
    }).subscribe({
      next: () => {
        this.loading = false;
        this.router.navigate(['/dashboard']);
      },
      error: (error: any) => {
        this.loading = false;
        this.errorMessage = error.error?.message || error.error?.details || 'Registration failed. Please check your details.';
      }
    });
  }
}
