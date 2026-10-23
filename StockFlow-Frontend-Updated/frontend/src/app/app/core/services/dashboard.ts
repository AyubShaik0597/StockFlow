import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ProductItem } from './product';

export interface DashboardSummary {
  totalProducts: number;
  totalQuantity: number;
  lowStockCount: number;
  lowStockProducts: ProductItem[];
}

@Injectable({ providedIn: 'root' })
export class DashboardService {
  private http = inject(HttpClient);
  private apiUrl = 'https://stockflow-1-22y8.onrender.com/api/dashboard';

  getSummary(): Observable<DashboardSummary> {
    return this.http.get<DashboardSummary>(this.apiUrl);
  }
}

