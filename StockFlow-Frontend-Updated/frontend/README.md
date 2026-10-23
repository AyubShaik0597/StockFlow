# StockFlow Angular Frontend (Assessment MVP)

Angular standalone frontend for the StockFlow inventory MVP. It integrates with the existing Java Spring Boot backend running at `http://localhost:8080`.

## Included assessment flows

- Organization signup with email, password, password confirmation, and organization name
- Login and JWT bearer-token attachment on API requests
- Protected dashboard, products, and settings routes
- Dashboard values loaded from `GET /api/dashboard`
- Product list/create/edit/delete integrated with `/api/products`
- Product search by name or SKU using `GET /api/products?search=...`
- Low-stock indicators and low-stock dashboard table
- Organization default threshold settings through `GET/PUT /api/settings`
- Client-side required-field, quantity, threshold, and price validation
- Responsive navigation and logout

## Requirements

- Node.js supported by your installed Angular CLI (Node 20.19+ or 22.12+ is recommended for Angular 21)
- npm
- MySQL and the StockFlow Spring Boot backend

## Run on Windows PowerShell

1. Start MySQL and ensure the backend is configured for the `stockflow_db` database.
2. Start the Spring Boot backend on port `8080` and confirm it starts successfully.
3. Open this frontend folder in PowerShell.
4. Install frontend dependencies on your own machine (do not copy `node_modules` from another operating system):

   ```powershell
   npm ci
   ```

5. Start Angular:

   ```powershell
   npm start
   ```

6. Open `http://localhost:4200`.

The backend's CORS configuration should allow `http://localhost:4200`.

## API contract used

| Feature | Endpoint |
|---|---|
| Register | `POST /api/auth/register` |
| Login | `POST /api/auth/login` |
| Dashboard | `GET /api/dashboard` |
| List/search products | `GET /api/products?search=...` |
| Create product | `POST /api/products` |
| Get product | `GET /api/products/{id}` |
| Update product | `PUT /api/products/{id}` |
| Delete product | `DELETE /api/products/{id}` |
| Read settings | `GET /api/settings` |
| Update settings | `PUT /api/settings` |

Authenticated API calls include `Authorization: Bearer <token>`.

## Notes

- Signup/login responses store the JWT in browser `localStorage`, consistent with the current assessment backend.
- Product-level low-stock thresholds are stored with each product. The organization default threshold is loaded as the suggested value when creating a new product. Existing products are not silently changed when the organization default changes.
- Inventory value is not shown on the dashboard because the assessment API does not currently expose an inventory-value metric; the dashboard focuses on the specified product count, total quantity, and low-stock list.
- Run a browser-based end-to-end test against your local backend before submission. A TypeScript compile check passed during preparation, but a full Angular production build could not be completed in this environment because the uploaded `node_modules` contains Windows-native dependencies while the build environment is Linux. Running `npm ci` on Windows installs the correct native packages.
