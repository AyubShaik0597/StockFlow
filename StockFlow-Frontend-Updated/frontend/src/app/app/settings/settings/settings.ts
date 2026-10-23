import { ChangeDetectorRef, Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { SettingsService } from '../../core/services/settings';
import { Auth } from '../../core/services/auth';

@Component({ selector: 'app-settings', standalone: true, imports: [CommonModule, FormsModule, RouterLink], templateUrl: './settings.html', styleUrl: './settings.css' })
export class Settings implements OnInit {
  private settingsService = inject(SettingsService);
  private auth = inject(Auth);
  private router = inject(Router);
  private cdr = inject(ChangeDetectorRef);
  organizationName = '';
  defaultLowStockThreshold = 5;
  loading = false;
  saving = false;
  errorMessage = '';
  successMessage = '';

  ngOnInit(): void { this.loadSettings(); }
  loadSettings(): void {
    this.loading = true; this.errorMessage = '';
    this.settingsService.getSettings().subscribe({
      next: settings => { this.organizationName = settings.organizationName; this.defaultLowStockThreshold = settings.defaultLowStockThreshold ?? 5; localStorage.setItem('organizationName', this.organizationName); this.loading = false; this.cdr.detectChanges(); },
      error: (error: any) => { this.loading = false; this.errorMessage = error.error?.message || 'Unable to load settings.'; this.cdr.detectChanges(); }
    });
  }
  saveSettings(): void {
    this.errorMessage = ''; this.successMessage = '';
    if (!Number.isInteger(Number(this.defaultLowStockThreshold)) || Number(this.defaultLowStockThreshold) < 0) { this.errorMessage = 'Threshold must be a whole number greater than or equal to zero.'; return; }
    this.saving = true;
    this.settingsService.updateSettings(Number(this.defaultLowStockThreshold)).subscribe({
      next: settings => { this.defaultLowStockThreshold = settings.defaultLowStockThreshold; this.organizationName = settings.organizationName; this.successMessage = 'Settings saved successfully.'; this.saving = false; this.cdr.detectChanges(); },
      error: (error: any) => { this.errorMessage = error.error?.message || 'Unable to save settings.'; this.saving = false; this.cdr.detectChanges(); }
    });
  }
  logout(): void { this.auth.logout(); this.router.navigate(['/login']); }
}
