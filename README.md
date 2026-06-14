# Real-Time Fraud Detection Microservice

A production-inspired backend microservice built using **Java** and **Spring Boot** that evaluates financial transactions in real time and determines whether they should be approved, require additional verification, or be blocked based on configurable fraud detection rules.

## Highlights

- Pluggable Fraud Rule Engine (Strategy Pattern)
- Redis Sliding Window Velocity Detection
- Kafka Event Publishing & Consumption
- PostgreSQL Persistence Layer
- Flyway Database Migrations
- Global Exception Handling
- OpenAPI / Swagger Documentation
- Spring Boot Actuator Monitoring
- Comprehensive Unit & Controller Testing
- Docker Compose Local Infrastructure

## Overview

Financial institutions process millions of transactions every day. This project simulates a real-time fraud detection engine that evaluates incoming transactions against multiple fraud rules, calculates a risk score, and generates a fraud decision.

The system is designed using modern backend engineering practices including event-driven architecture, Redis-based rate limiting, Kafka event publishing, Flyway database migrations, and a pluggable rule engine.

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

## Architecture flow
```
Client
│
▼
TransactionController
│
▼
TransactionService
│
▼
FraudDetectionEngine
│
├── AmountThresholdRule
├── VelocityRule (Redis)
└── GeoAnomalyRule (Redis)
│
▼
DecisionService
│
▼
PostgreSQL
│
▼
Kafka Producer
│
▼
fraud-decisions Topic
│
▼
Kafka Consumer
```

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


## Risk Scoring & Decision Engine

Fraud rules contribute to an aggregated fraud score.

| Score Range | Decision       |
|-------------|----------------|
| 0 – 30      | `APPROVED`     |
| 31 – 70     | `OTP_REQUIRED` |
| 71+         | `BLOCKED`      |

The decision engine is fully configurable through application properties.


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

### Get Transaction Result

```http
GET /transactions/{transactionId}
```

## API Documentation

Interactive API documentation is available via Swagger UI.

```text
http://localhost:8080/swagger-ui/index.html
```
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

Current Consumers:

- FraudDecisionConsumer

The consumer subscribes to the fraud-decisions topic and processes fraud decision events asynchronously.

This demonstrates event-driven communication between independent services.

## Configuration Driven Rules

Fraud rules are externally configurable through application.yml.

Examples:

```yaml
fraud:
  rules:
    amount-threshold:
      threshold: 50000
      score: 40

    velocity:
      max-transactions: 5
      window-seconds: 60
      score: 30
```   

## Testing

The project includes comprehensive automated tests covering:

### Unit Tests

- Fraud Rules
    - AmountThresholdRuleTest
    - VelocityRuleTest
    - GeoAnomalyRuleTest

- Services
    - DecisionServiceTest
    - FraudResultServiceTest
    - AuditLogServiceTest
    - TransactionServiceTest

- Engine
    - FraudDetectionEngineTest

- Kafka Components
    - FraudEventPublisherServiceTest
    - FraudDecisionConsumerTest

- Redis Components
    - VelocityTrackerServiceTest
    - GeoLocationServiceTest

### Controller Tests

- TransactionControllerTest

Testing stack:

- JUnit 5
- Mockito
- Spring MockMvc

Run tests:

```bash
mvn test
```
Coverage includes:

- Business rules
- Services
- Controllers
- Kafka components
- Redis components
## Exception Handling

Global exception handling is implemented using `@RestControllerAdvice`.

- Validation error handling
- Business exception handling
- Consistent API error responses
- Structured logging

## Monitoring & Observability

Spring Boot Actuator is enabled.

Available endpoints:

| Endpoint |
|-----------|
| /actuator/health |
| /actuator/info |
| /actuator/metrics |

Examples:

http://localhost:8080/actuator/health

Actuator provides:

- Application health status
- JVM metrics
- HTTP request metrics
- Runtime diagnostics

## Logging

The service uses structured request-based logging.

Logged events include:

- Transaction received
- Fraud evaluation results
- Kafka publishing status
- Kafka consumer processing
- Exception handling

Logs are written to dedicated application log files for easier troubleshooting and auditing.

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

## Future Enhancements

- Transactional Outbox Pattern
- Dead Letter Queue (DLQ)
- Rule configuration from database
- Distributed tracing
- Testcontainers Integration
- ML-based risk scoring
