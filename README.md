# Payflow Payroll System

## Overview
Payflow is a comprehensive payroll management system designed for multi-company environments. It provides secure, efficient payroll processing with role-based access control, audit logging, and PDF payslip generation.

## Features

### 1. Authentication & Security
✅ JWT-Based Authentication

Secure login using JSON Web Tokens (JWT)

Stateless authentication (no server sessions)

Token validation on every protected API request

✅ Role-Based Access Control (RBAC)

Supported roles:

SUPERADMIN – system-wide control

HR – employee & payroll management

FINANCE – payroll & tax operations

EMPLOYEE – view own payslips

Access is enforced using:

@PreAuthorize("hasAnyRole('HR','FINANCE')")

### 2. Multi-Company (Multi-Tenant) Support

Each company is onboarded independently

Employees, payrolls, and users belong to a specific company

Data isolation between companies

SUPERADMIN can manage all companies

### 3. User Management
Features:

User registration and login

Users linked to roles and companies

Account enable/disable

Password encryption using BCrypt

### 4. Employee Management
HR Capabilities:

Create, update, and delete employees

Assign employees to departments and companies

Store personal and payroll-related employee data

Employee Capabilities:

View own payroll information

Download personal payslips

### 5. Payroll Processing
Payroll Generation

Generate payroll for individual employees

Automatically calculates:

Basic salary

Tax deductions

Net salary

Payroll Logic Includes:

Salary computation

Tax calculation

Net pay calculation

Pay date tracking

### 6. Payslip Management
Payslip Features:

View payslips via API

Download payslips as PDF

Payslips include:

Employee ID

Salary

Tax

Net salary

Payment date

### 7. PDF Generation

✅ Automatically generates downloadable payslips

Uses a dedicated PayslipPdfService

Ready for integration with real PDF libraries (OpenPDF / iText)

### 8. Tax Management

Store and manage tax rules

Apply deductions during payroll generation

Centralized tax configuration (extensible)

### 9. Salary Management

Maintain salary records

Support for future:

Allowances

Bonuses

Deductions

Overtime

### 10. Audit Logging
✅ Audit Features:

Tracks system actions such as:

Payroll generation

User login

Employee creation

Records:

Action performed

User who performed it

Timestamp

Ensures accountability and compliance.

### 11. API Documentation (Swagger / OpenAPI)

✅ Auto-generated REST API documentation

Accessible via:

/swagger-ui.html


Easy API testing for developers & integrators

### 12. Database & Persistence
Database Support:

PostgreSQL / MySQL

JPA & Hibernate ORM

Flyway DB migrations (V1__init.sql)

Schema Includes:

Users

Roles

Companies

Employees

Payslips

Audit logs

### 13. Dockerized Deployment
✅ Docker Support:

Backend container

Database container

docker-compose.yml for local & production use

Benefits:

Easy setup

Consistent environments

Cloud-ready

### 14. Frontend (Scaffolded)
Pages:

Login Page

Dashboard

Payroll View

Payslip Download

Ready to integrate with:

React / Angular / Vue

REST APIs secured by JWT

### 15. System Architecture

RESTful API

Layered architecture:

Controller

Service

Repository

Clean package separation

Easy to extend and maintain

### 16. Future-Ready Enhancements

Planned / Extensible:

Monthly payroll runs

Email payslip delivery

Multiple tax slabs

Reports & analytics

Multi-currency support

Cloud deployment (AWS/GCP)

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- Node.js 16+ and npm
- H2 Database (embedded, no separate setup required)

## Installation

### Backend

1. Navigate to the backend directory:
   ```bash
   cd backend
   ```

2. Install dependencies and build:
   ```bash
   mvn clean install
   ```

### Frontend

1. Navigate to the frontend directory:
   ```bash
   cd frontend
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

## Running the Application

### Backend

1. From the backend directory:
   ```bash
   mvn spring-boot:run
   ```

   The backend will start on `http://localhost:8080` (or configured port).

### Frontend

1. From the frontend directory:
   ```bash
   npm start
   ```

   The frontend will start on `http://localhost:3000`.

## API Documentation

API documentation is available via Swagger UI at:
- `http://localhost:8080/swagger-ui.html`

## Database

The application uses H2 in-memory database for development. Data is persisted in `backend/testdb.mv.db`.

For production, configure PostgreSQL or MySQL in `application.properties`.

## Default Users

- Email: admin@test.com
- Password: password
- Role: SUPERADMIN

## Architecture Diagrams

See [diagrams.md](diagrams.md) for system architecture and flow diagrams.