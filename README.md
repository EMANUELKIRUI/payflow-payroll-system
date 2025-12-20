# Payflow Payroll System

A full-stack payroll management system with React frontend and Spring Boot backend.

## Tech Stack

- **Frontend:** React 18, React Router
- **Backend:** Spring Boot 2.7, JPA, H2/PostgreSQL
- **Database:** H2 (dev), PostgreSQL (prod)
- **Security:** JWT, Spring Security
- **Deployment:** Docker, Docker Compose

## Quick Start

### Prerequisites

- Docker and Docker Compose
- Node.js 16+ (for local frontend development)
- Java 17 (for local backend development)

### Production Deployment

1. **Clone the repository:**
   ```bash
   git clone <repository-url>
   cd payflow-payroll-system
   ```

2. **Build and run with Docker Compose:**
   ```bash
   docker-compose up --build
   ```

3. **Access the application:**
   - Frontend: http://localhost
   - Backend API: http://localhost:8080

### Local Development

1. **Start the backend:**
   ```bash
   cd backend
   export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
   ./mvnw spring-boot:run
   ```

2. **Start the frontend:**
   ```bash
   cd frontend
   npm install
   npm start
   ```

3. **Access:**
   - Frontend: http://payflowpayroll.com:3000 (add to /etc/hosts: 127.0.0.1 payflowpayroll.com)
   - Backend: http://localhost:8080

## Default Users

- **Super Admin:** super@test.com / password
- **Employee:** admin@test.com / password

## API Documentation

- Swagger UI: http://localhost:8080/swagger-ui/index.html

## Environment Variables

### Backend

- `SPRING_PROFILES_ACTIVE`: `prod` for production
- `JWT_SECRET`: Custom JWT secret (default provided)
- `DB_PASSWORD`: Database password (default: prodpassword)
- `PORT`: Server port (default: 8080)

### Frontend

- `PORT`: Frontend port (default: 3000)
- `HOST`: Host domain (default: payflowpayroll.com)

## Database

- **Development:** H2 in-memory/file database
- **Production:** PostgreSQL via Docker Compose

## Security

- JWT tokens with 24-hour expiration
- CORS configured for custom domain
- Password encryption with BCrypt
- Role-based authorization

## Deployment Notes

- Frontend is served via Nginx in production
- Backend runs on port 8080
- Database migrations handled by Flyway
- Static files optimized and gzipped

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