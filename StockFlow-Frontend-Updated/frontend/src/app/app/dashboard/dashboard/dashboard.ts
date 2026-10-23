import { ChangeDetectorRef, Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { DashboardService, DashboardSummary } from '../../core/services/dashboard';
import { Auth } from '../../core/services/auth';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css'
})
export class Dashboard implements OnInit {
  private dashboardService = inject(DashboardService);
  private auth = inject(Auth);
  private cdr = inject(ChangeDetectorRef);
  summary: DashboardSummary = { totalProducts: 0, totalQuantity: 0, lowStockCount: 0, lowStockProducts: [] };
  loading = false;
  errorMessage = '';
  organizationName = localStorage.getItem('organizationName') || 'Your organization';

  ngOnInit(): void { this.loadDashboard(); }

  loadDashboard(): void {
    this.loading = true;
    this.errorMessage = '';
    this.dashboardService.getSummary().subscribe({
      next: data => {
        this.summary = {
          totalProducts: data.totalProducts ?? 0,
          totalQuantity: data.totalQuantity ?? 0,
          lowStockCount: data.lowStockCount ?? data.lowStockProducts?.length ?? 0,
          lowStockProducts: data.lowStockProducts ?? []
        };
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.loading = false;
        this.errorMessage = 'Unable to load dashboard data. Please sign in again if your session has expired.';
        this.cdr.detectChanges();
      }
    });
  }

  logout(): void { this.auth.logout(); window.location.href = '/login'; }
}
