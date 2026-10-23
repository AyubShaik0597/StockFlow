
import { Routes } from '@angular/router';
import { Login } from './app/auth/login/login';
import { Register } from './app/auth/register/register';
import { authGuard } from './app/core/guards/auth-guard';

import { ProductForm } from './app/products/product-form/product-form';
import { ProductList } from './app/products/product-list/product-list';
import { Settings } from './app/settings/settings/settings';
import { Dashboard } from './app/dashboard/dashboard/dashboard';



export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },

  { path: 'login', component: Login },
  { path: 'register', component: Register },

  {
    path: 'dashboard',
    component: Dashboard,
    canActivate: [authGuard]
  },
  {
    path: 'products',
    component: ProductList,
    canActivate: [authGuard]
  },
  {
    path: 'products/new',
    component: ProductForm,
    canActivate: [authGuard]
  },
  {
    path: 'products/edit/:id',
    component: ProductForm,
    canActivate: [authGuard]
  },
  {
    path: 'settings',
    component: Settings,
    canActivate: [authGuard]
  },

  { path: '**', redirectTo: 'login' }
];

