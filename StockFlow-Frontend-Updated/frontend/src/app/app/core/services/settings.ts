import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface OrganizationSettings {
  organizationName: string;
  defaultLowStockThreshold: number;
}

@Injectable({ providedIn: 'root' })
export class SettingsService {
  private http = inject(HttpClient);
  private apiUrl = 'https://stockflow-1-22y8.onrender.com/api/settings';

  getSettings(): Observable<OrganizationSettings> {
    return this.http.get<OrganizationSettings>(this.apiUrl);
  }

  updateSettings(defaultLowStockThreshold: number): Observable<OrganizationSettings> {
    return this.http.put<OrganizationSettings>(this.apiUrl, { defaultLowStockThreshold });
  }
}

