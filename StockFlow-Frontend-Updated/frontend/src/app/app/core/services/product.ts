import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface ProductItem {
  id: number;
  sku: string;
  name: string;
  description?: string | null;
  costPrice: number | null;
  sellingPrice: number | null;
  quantityOnHand: number;
  lowStockThreshold: number | null;
  createdAt?: string;
  updatedAt?: string;
}

export type ProductPayload = Omit<ProductItem, 'id' | 'createdAt' | 'updatedAt'>;

@Injectable({ providedIn: 'root' })
export class Product {
  private http = inject(HttpClient);
  private apiUrl = 'https://stockflow-1-22y8.onrender.com/api/products';

  getProducts(search = ''): Observable<ProductItem[]> {
    let params = new HttpParams();
    if (search.trim()) params = params.set('search', search.trim());
    return this.http.get<ProductItem[]>(this.apiUrl, { params });
  }
  getProduct(id: number): Observable<ProductItem> { return this.http.get<ProductItem>(`${this.apiUrl}/${id}`); }
  createProduct(data: ProductPayload): Observable<ProductItem> { return this.http.post<ProductItem>(this.apiUrl, data); }
  updateProduct(id: number, data: ProductPayload): Observable<ProductItem> { return this.http.put<ProductItem>(`${this.apiUrl}/${id}`, data); }
  deleteProduct(id: number): Observable<void> { return this.http.delete<void>(`${this.apiUrl}/${id}`); }
}

