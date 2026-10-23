# StockFlow Backend (Java 21 / Spring Boot / MySQL)

This version keeps the existing StockFlow structure and adds consistent API errors, stricter product input validation, configurable JWT signing secret, and removes temporary JWT debug output.

## Requirements
- JDK 21
- MySQL 8.x (or compatible)
- MySQL database named `stockflow_db`

## Local setup
1. Create the database once in MySQL:
   ```sql
   CREATE DATABASE stockflow_db;
   ```
2. Set database credentials if they differ from the local defaults. In PowerShell:
   ```powershell
   $env:DB_USERNAME = "root"
   $env:DB_PASSWORD = "your_mysql_password"
   ```
3. Set a long, private JWT secret for your environment. Do not commit production secrets:
   ```powershell
   $env:JWT_SECRET = "replace-this-with-a-long-private-secret-for-your-local-run"
   ```
4. From the folder containing `pom.xml`, run:
   ```powershell
   .\mvnw.cmd spring-boot:run
   ```
   On Linux/macOS: `./mvnw spring-boot:run`.

The default local configuration points to `localhost:3306/stockflow_db`, runs on port `8080`, and allows the Angular dev server at `http://localhost:4200`. Hibernate uses `ddl-auto=update`; back up important data before changing entity mappings.

## API endpoints
- `POST /api/auth/register` — create organization and first user
- `POST /api/auth/login` — authenticate and return JWT
- `GET /api/products?search=term` — list products, optionally search name/SKU
- `POST /api/products` — create product
- `GET /api/products/{id}` — get product in current organization
- `PUT /api/products/{id}` — update product in current organization
- `DELETE /api/products/{id}` — delete product in current organization
- `GET /api/dashboard` — total products, total quantity and low-stock products
- `GET /api/settings` — current organization's settings
- `PUT /api/settings` — update default low-stock threshold

All endpoints except `/api/auth/**` require `Authorization: Bearer <token>`. Product queries and mutations are scoped to the organization associated with the authenticated user. A product with a null threshold uses the organization's default threshold for low-stock calculations.

## Notes
- Signup currently expects `email`, `password`, and `organizationName`. Password confirmation is a frontend validation concern unless the API contract is intentionally extended.
- Passwords are stored using BCrypt.
- Duplicate SKU is only prohibited within the same organization.
- Error responses use JSON fields such as `timestamp`, `status`, `error`, `message`, and (for validation errors) `fieldErrors`.
- Do not expose production database credentials or JWT secrets in source control.
