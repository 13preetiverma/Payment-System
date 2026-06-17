# PayPal Payment Microservices System

A Spring Boot microservices-based payment processing system that integrates with PayPal Sandbox APIs for secure online payments.

## Overview

This project demonstrates a payment workflow where a merchant application submits a payment request, validates the request, processes the payment, communicates with PayPal, and returns the result to the merchant.

### Features

✔ PayPal Order Creation and Capture API Integration
✔ OAuth 2.0 Secure Authentication
✔ Payment Status Lifecycle Management
✔ Redis-Based Access Token Caching
✔ Circuit Breaker Fault Tolerance
✔ Centralized Exception Handling
✔ Distributed Logging with Micrometer
✔ Spring JDBC + MySQL Persistence
✔ Factory & Builder Design Patterns
✔ AWS EC2, RDS, and Secrets Manager Deployment
✔ Unit Testing with JUnit & Mockito
✔ RESTful Microservices Architecture

---

# Architecture

<img width="1109" height="565" alt="WhatsApp Image 2026-06-14 at 21 52 39" src="https://github.com/user-attachments/assets/e08e5987-4b25-4514-a605-c2fb21b5397f" />

```text
Response Flow:
PayPal -> Provider -> Processing -> Validation -> Merchant
```

---

# Microservices

| Service                 | Responsibility                          |
| ----------------------- | --------------------------------------- |
| Eureka Server           | Load Balancing |
| Validation Service      | Validates incoming requests             |
| Processing Service      | Coordinates payment processing          |
| PayPal Provider Service | Communicates with PayPal APIs           |

---

# Microservices Communication

paypal project.drawio

# Service Port Mapping

| Service                 | Port |
| ----------------------- | ---- |
| Eureka Server           | 8087 |
| Validation Service      | 8081 |
| Processing Service      | 8082 |
| PayPal Provider Service | 8083 |


---

# Technology Stack

## Backend

* Java 17
* Spring Boot
* Spring JDBC
* Mysql
* PayPal Sandbox
* Lombok
* Maven
* Redis Cache
* Docker
* Docker Compose
  
---

# Project Structure

```text
paypal-payment-system/
│
├── eureka-service/
│
├── validation-service/
│
├── processing-service/
│
├── paypal-provider-service/
│
├── docker-compose.yml
│
├── README.md
│
└── .env.example
```

---

# Prerequisites

* Java 17+
* Maven 3.8+
* Docker
* Docker Compose
* Git

Verify installation:

```bash
java -version
mvn -version
docker --version
```

---

# Environment Configuration

Create a file named:

```text
.env
```

using the sample:

```text
.env.example
```

Example:

```env
DB_HOST=localhost
DB_PORT=3306
DB_NAME=payments_db
DB_USERNAME=root
DB_PASSWORD=password

PAYPAL_CLIENT_ID=YOUR_CLIENT_ID
PAYPAL_CLIENT_SECRET=YOUR_SECRET
```

---

# MySQL Setup

Create database:

```sql
CREATE DATABASE payments_db;
```

Optional user creation:

```sql
CREATE USER 'payments_user'@'localhost'
IDENTIFIED BY 'payments_password';

GRANT ALL PRIVILEGES
ON payments_db.*
TO 'payments_user'@'localhost';

FLUSH PRIVILEGES;
```

---

# Running the Project

## Build

```bash
mvn clean package
```

---

## Run Using Docker

Build and start:

```bash
docker-compose up --build
```

Detached mode:

```bash
docker-compose up -d
```

Stop services:

```bash
docker-compose down
```

---

## Run Manually

### Eureka Service

```bash
cd Eureka-service
mvn spring-boot:run
```

### Validation Service

```bash
cd validation-service
mvn spring-boot:run
```

### Processing Service

```bash
cd processing-service
mvn spring-boot:run
```

### PayPal Provider Service

```bash
cd paypal-provider-service
mvn spring-boot:run
```

---

# API Endpoints

## Create Payment

### Request

```http
POST /payments/create
```

### Sample Request

```json
{
  "userId": 111,
  "paymentMethodId": 1,
  "providerId": 1,
  "paymentTypeId": 1,
  "amount": 70.00,
  "currency": "USD",
  "merchantTransactionReference": "TXN-555"
}
```

### Success Response

```json
{
  "status": "SUCCESS",
  "transactionId": "PAYPAL-12345",
  "message": "Payment processed successfully"
}
```

---

## Get Payment Status

### Request

```http
GET /payments/{transactionId}
```

### Sample Response

```json
{
  "transactionId": "PAYPAL-12345",
  "status": "COMPLETED"
}
```

---

# Validation Rules

| Field                        | Validation     |
| ---------------------------- | -------------- |
| userId                       | Required       |
| paymentMethodId              | Must be 1      |
| providerId                   | Must be 1      |
| paymentTypeId                | Must be 1      |
| amount                       | Greater than 0 |
| currency                     | Required       |
| merchantTransactionReference | Required       |

---

# Error Handling

Example Error Response:

```json
{
  "status": "FAILED",
  "errorCode": "VALIDATION_ERROR",
  "message": "paymentMethodId must be 1"
}
```

---

# Security

The following files should never be committed:

```gitignore
.env
application.properties
application-dev.properties
```

Secrets such as:

* Database Passwords
* PayPal Client IDs
* PayPal Secrets
* API Keys

must be supplied through environment variables.

---

# Testing

Run unit tests:

```bash
mvn test
```

Run integration tests:

```bash
mvn verify
```

---

# Future Enhancements

* Stripe Integration
* Kafka Event Processing
* Redis Caching
* API Gateway
* Service Discovery
* JWT Authentication
* Kubernetes Deployment
* AWS Deployment

---

# Troubleshooting

## Port Already In Use

Windows:

```cmd
netstat -ano | findstr :8080
taskkill /PID <PID> /F
```

---

## Docker Logs

```bash
docker-compose logs -f
```

---

## Rebuild Containers

```bash
docker-compose down
docker-compose up --build
```

---

# Author

Priti Verma

Java Backend Developer | Spring Boot | Microservices | Docker

---

# License

This project is for educational and learning purposes.
