# CRM Backend API

Comprehensive Enterprise CRM (Customer Relationship Management) RESTful API built with Java 21, Spring Boot, Spring Security (JWT), Spring Data JPA, and PostgreSQL.

## Core Features & Modules

- **Authentication (`/api/auth`)**: User registration, login with JWT tokens, refresh tokens, logout.
- **Users (`/api/users`)**: User management, role updates, account enabling/disabling.
- **Customers (`/api/customers`)**: Full CRUD operations for customer records, interest tracking, search functionality.
- **Leads (`/api/leads`)**: Lead tracking, status pipeline (NEW, CONTACTED, QUALIFIED, CONVERTED, etc.), team assignment.
- **Opportunities (`/api/opportunities`)**: Sales deal tracking across stages (PROSPECTING to CLOSED_WON), probability & revenue forecasting.
- **Tasks (`/api/tasks`)**: Operational task management with priority levels and due dates.
- **Tickets (`/api/tickets`)**: Support ticket management with customer linking and priority escalation.
- **Products (`/api/products`)**: Product catalog management, price lists, stock tracking.
- **Quotations (`/api/quotations`)**: Sales quote generation, valid dates, status tracking.
- **Invoices (`/api/invoices`)**: Billing & invoice management (UNPAID, PAID, OVERDUE tracking).
- **Dashboard (`/api/dashboard`)**: Aggregated metrics, pipeline totals, revenue metrics, and module summaries.

## Getting Started

### Requirements
- Java 21
- PostgreSQL Database (`jdbc:postgresql://localhost:5432/CRM`)
- Maven

### Run Application
```bash
./mvnw spring-boot:run
```

### Build & Test
```bash
./mvnw clean compile
./mvnw test
```
