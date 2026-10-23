import { ChangeDetectorRef, Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, ActivatedRoute, RouterLink } from '@angular/router';
import { Product, ProductItem, ProductPayload } from '../../core/services/product';
import { Auth } from '../../core/services/auth';
import { SettingsService } from '../../core/services/settings';

@Component({ selector: 'app-product-form', standalone: true, imports: [CommonModule, FormsModule, RouterLink], templateUrl: './product-form.html', styleUrl: './product-form.css' })
export class ProductForm implements OnInit {
  private productService = inject(Product);
  private settingsService = inject(SettingsService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private auth = inject(Auth);
  private cdr = inject(ChangeDetectorRef);
  product: ProductPayload = { name: '', sku: '', description: '', costPrice: null, sellingPrice: null, quantityOnHand: 0, lowStockThreshold: 5 };
  isEditMode = false;
  productId: number | null = null;
  loading = false;
  errorMessage = '';

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id !== null) {
      const parsedId = Number(id);
      if (!Number.isInteger(parsedId) || parsedId <= 0) { this.errorMessage = 'Invalid product ID.'; return; }
      this.productId = parsedId;
      this.isEditMode = true;
      this.loadProduct(parsedId);
    } else {
      this.settingsService.getSettings().subscribe({
        next: settings => { this.product.lowStockThreshold = settings.defaultLowStockThreshold ?? 5; this.cdr.detectChanges(); },
        error: () => { /* Keep the assessment default if settings cannot be loaded. */ }
      });
    }
  }

  loadProduct(id: number): void {
    this.loading = true;
    this.productService.getProduct(id).subscribe({
      next: (data: ProductItem) => {
        this.product = { name: data.name, sku: data.sku, description: data.description ?? '', costPrice: data.costPrice, sellingPrice: data.sellingPrice, quantityOnHand: data.quantityOnHand, lowStockThreshold: data.lowStockThreshold ?? 5 };
        this.loading = false; this.cdr.detectChanges();
      },
      error: (error: any) => { this.errorMessage = error.error?.message || 'Failed to load product details.'; this.loading = false; this.cdr.detectChanges(); }
    });
  }

  saveProduct(): void {
    this.errorMessage = '';
    const name = this.product.name.trim();
    const sku = this.product.sku.trim();
    const quantity = Number(this.product.quantityOnHand);
    const threshold = Number(this.product.lowStockThreshold);
    const prices = [this.product.costPrice, this.product.sellingPrice];
    if (!name || !sku) { this.errorMessage = 'Product name and SKU are required.'; return; }
    if (!Number.isInteger(quantity) || quantity < 0) { this.errorMessage = 'Quantity must be a whole number greater than or equal to zero.'; return; }
    if (!Number.isInteger(threshold) || threshold < 0) { this.errorMessage = 'Low-stock threshold must be a whole number greater than or equal to zero.'; return; }
    if (prices.some(price => price !== null && (!Number.isFinite(Number(price)) || Number(price) < 0 || Math.round(Number(price) * 100) !== Number(price) * 100))) { this.errorMessage = 'Prices must be nonnegative and contain no more than two decimal places.'; return; }
    this.loading = true;
    const payload: ProductPayload = { ...this.product, name, sku, description: this.product.description?.trim() ?? '', quantityOnHand: quantity, lowStockThreshold: threshold, costPrice: this.product.costPrice === null || this.product.costPrice === ('' as any) ? null : Number(this.product.costPrice), sellingPrice: this.product.sellingPrice === null || this.product.sellingPrice === ('' as any) ? null : Number(this.product.sellingPrice) };
    const request = this.isEditMode && this.productId !== null ? this.productService.updateProduct(this.productId, payload) : this.productService.createProduct(payload);
    request.subscribe({
      next: () => { this.loading = false; this.cdr.detectChanges(); this.router.navigate(['/products']); },
      error: (error: any) => { this.errorMessage = error.error?.message || error.error?.details || 'Unable to save product. Check the SKU and field values.'; this.loading = false; this.cdr.detectChanges(); }
    });
  }

  logout(): void { this.auth.logout(); this.router.navigate(['/login']); }
}
