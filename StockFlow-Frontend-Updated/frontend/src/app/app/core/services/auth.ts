import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

export interface AuthResponse {
  token: string;
  userId: number;
  organizationId: number;
  email: string;
  organizationName: string;
  tokenType: string;
}

@Injectable({ providedIn: 'root' })
export class Auth {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/auth';

  login(data: { email: string; password: string }): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, data).pipe(
      tap(response => this.storeSession(response))
    );
  }

  register(data: { email: string; password: string; organizationName: string }): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/register`, data).pipe(
      tap(response => this.storeSession(response))
    );
  }

  private storeSession(response: AuthResponse): void {
    localStorage.setItem('accessToken', response.token);
    localStorage.setItem('userEmail', response.email);
    localStorage.setItem('organizationName', response.organizationName);
  }

  logout(): void {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('userEmail');
    localStorage.removeItem('organizationName');
    localStorage.removeItem('refreshToken');
  }

  isLoggedIn(): boolean { return !!localStorage.getItem('accessToken'); }
  getToken(): string | null { return localStorage.getItem('accessToken'); }
}
