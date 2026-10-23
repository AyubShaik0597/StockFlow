import { ChangeDetectorRef, Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { Product, ProductItem } from '../../core/services/product';
import { Auth } from '../../core/services/auth';

@Component({
  selector: 'app-product-list', standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './product-list.html', styleUrl: './product-list.css'
})
export class ProductList implements OnInit {
  private productService = inject(Product);
  private cdr = inject(ChangeDetectorRef);
  private auth = inject(Auth);
  private router = inject(Router);
  products: ProductItem[] = [];
  loading = false;
  errorMessage = '';
  searchTerm = '';

  ngOnInit(): void { this.loadProducts(); }

  loadProducts(): void {
    this.loading = true;
    this.errorMessage = '';
    this.productService.getProducts(this.searchTerm.trim()).subscribe({
      next: data => { this.products = Array.isArray(data) ? data : []; this.loading = false; this.cdr.detectChanges(); },
      error: () => { this.errorMessage = 'Unable to load products. Please try again.'; this.loading = false; this.cdr.detectChanges(); }
    });
  }

  onSearch(): void { this.loadProducts(); }
  clearSearch(): void { this.searchTerm = ''; this.loadProducts(); }

  deleteProduct(id: number): void {
    if (!confirm('Are you sure you want to delete this product? This action cannot be undone.')) return;
    this.productService.deleteProduct(id).subscribe({
      next: () => this.loadProducts(),
      error: (error: any) => { this.errorMessage = error.error?.message || 'Unable to delete product.'; this.cdr.detectChanges(); }
    });
  }

  isLowStock(product: ProductItem): boolean { return product.quantityOnHand <= (product.lowStockThreshold ?? 5); }
  logout(): void { this.auth.logout(); this.router.navigate(['/login']); }
}
