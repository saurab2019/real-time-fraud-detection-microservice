# Real-Time Fraud Detection Microservice

A production-inspired backend microservice built using **Java** and **Spring Boot** that evaluates financial transactions in real time and determines whether they should be approved, require additional verification, or be blocked based on configurable fraud detection rules.

---

## Overview

Financial institutions process millions of transactions every day. This project simulates a real-time fraud detection engine that evaluates incoming transactions against multiple fraud rules, calculates a risk score, and generates a fraud decision.

The system is designed using modern backend engineering practices including event-driven architecture, Redis-based rate limiting, Kafka event publishing, Flyway database migrations, and a pluggable rule engine.

---

## Features

### Fraud Detection Rules

#### Amount Threshold Rule
Detects unusually large transactions.
- Transaction Amount > ₹50,000
- Adds configurable risk score

#### Velocity Rule (Redis Sliding Window)
Detects excessive transaction activity within a short period.
- More than 5 transactions within 60 seconds
- Uses Redis Sorted Sets (ZSET) for sliding window implementation

#### Geo Anomaly Rule
Detects suspicious country changes.
- Previous Country: India → Current Country: Brazil
- Adds configurable risk score when anomaly is detected

---

## Risk Scoring & Decision Engine

Fraud rules contribute to an aggregated fraud score.

| Score Range | Decision       |
|-------------|----------------|
| 0 – 30      | `APPROVED`     |
| 31 – 70     | `OTP_REQUIRED` |
| 71+         | `BLOCKED`      |

The decision engine is fully configurable through application properties.

---

## REST APIs

### Create Transaction

```http
POST /transactions
```

**Request:**

```json
{
  "userId": 101,
  "amount": 75000,
  "country": "India",
  "merchantId": "M001"
}
```

**Response:**

```json
{
  "transactionId": "TXN-123",
  "score": 65,
  "decision": "OTP_REQUIRED"
}
```

---

### Get Transaction Result

```http
GET /transactions/{transactionId}
```

---

## Architecture

```
                 Spring Boot
                      │
                      ▼
            Fraud Detection Engine
                      │
        ┌─────────────┼─────────────┐
        ▼             ▼             ▼
 PostgreSQL        Redis       Rule Engine
                      │
                      ▼
                Risk Scoring
                      │
                      ▼
                 Decision
                      │
                      ▼
                    Kafka
```

---

## Technology Stack

| Technology         | Purpose                          |
|--------------------|----------------------------------|
| Java 21            | Programming Language             |
| Spring Boot 3      | Backend Framework                |
| PostgreSQL         | Transaction Persistence          |
| Redis              | Sliding Window Velocity Tracking |
| Apache Kafka       | Event Publishing                 |
| Spring Data JPA    | Data Access Layer                |
| Flyway             | Database Versioning              |
| Docker Compose     | Local Infrastructure             |
| Maven              | Build Tool                       |
| Lombok             | Boilerplate Reduction            |
| OpenAPI / Swagger  | API Documentation                |

---

## Database Design

### `transactions`
Stores incoming transactions.

| Column           |
|------------------|
| `transaction_id` |
| `user_id`        |
| `amount`         |
| `country`        |
| `merchant_id`    |
| `created_at`     |

### `fraud_results`
Stores fraud evaluation results.

| Column           |
|------------------|
| `transaction_id` |
| `fraud_score`    |
| `decision`       |
| `processed_at`   |

### `audit_logs`
Stores fired fraud rules.

| Column           |
|------------------|
| `transaction_id` |
| `rule_name`      |
| `rule_score`     |
| `reason`         |
| `created_at`     |

---

## Event-Driven Architecture

After fraud evaluation, the service publishes events to Kafka.

**Topic:**

```
fraud-decisions
```

**Sample Event:**

```json
{
  "transactionId": "TXN-123",
  "score": 65,
  "decision": "OTP_REQUIRED",
  "createdAt": "2026-06-03T08:40:10"
}
```

**Potential Consumers:**
- Audit Service
- Analytics Service
- Notification Service
- Reporting Service

---

## Rule Engine Design

The fraud detection engine follows the **Strategy Pattern**.

```
IFraudRule
    │
    ├── AmountThresholdRule
    ├── VelocityRule
    └── GeoAnomalyRule
```

New rules can be added without modifying the fraud engine, making the system extensible and compliant with the **Open/Closed Principle**.

---

## Exception Handling

Global exception handling is implemented using `@RestControllerAdvice`.

- Validation error handling
- Business exception handling
- Consistent API error responses
- Structured logging

---

## Running Locally

### Prerequisites
- Java 21
- Docker Desktop
- Maven

### Start Infrastructure

```bash
docker compose up -d
```

Starts: **PostgreSQL**, **Redis**, and **Kafka**

### Run Application

```bash
./mvnw spring-boot:run
```

---

## Future Improvements

- Transactional Outbox Pattern
- Dead Letter Queue (DLQ)
- Rule Configuration from Database
- Grafana & Prometheus Metrics
- Distributed Tracing
- Kafka Consumers
- Fraud Rule Versioning
- Machine Learning Based Fraud Scoring
- Kubernetes Deployment

---

## Key Backend Concepts Demonstrated

- Microservice Design
- Event-Driven Architecture
- Redis Sliding Window Algorithms
- Kafka Producers
- Strategy Design Pattern
- Spring Boot Best Practices
- Database Migrations with Flyway
- REST API Design
- Exception Handling
- Dockerized Local Development
- Extensible Rule Engine

---

## Learning Outcomes

This project was built to explore how real-world financial systems evaluate transactions, apply fraud detection rules, persist decisions, and publish events for downstream processing using a modern Java backend stack.
