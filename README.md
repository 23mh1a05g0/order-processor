# 🛒 Event-Driven Order Fulfillment Service

A Spring Boot microservice that processes order events asynchronously using RabbitMQ and updates order status in a MySQL database.

This project demonstrates:

- Event-Driven Architecture (EDA)
- Asynchronous message processing
- Idempotent operations
- Dead Letter Queue handling
- Docker containerization
- Integration & Unit Testing
- Clean microservice design

---

## 🏗 Architecture Overview

OrderPlacedEvent → RabbitMQ → OrderProcessor Service → MySQL -> OrderProcessedEvent


---

## 🛠 Tech Stack

- Java 17
- Spring Boot 3
- Spring Data JPA
- RabbitMQ
- MySQL
- Docker & Docker Compose
- JUnit 5 & Mockito
- H2 (for testing)

---

## 🚀 Running the Application (Docker)

### 1️⃣ Build and Start Services

docker compose up --build


## This will start:

MySQL
RabbitMQ
Order Processor Service

## 🌐 Service URLs
# 🔹 Application
http://localhost:8080

# 🔹 Health Check
http://localhost:8080/actuator/health


## Expected response:

{
  "status": "UP"
}

# 🔹 RabbitMQ Management Console
http://localhost:15672


# Default credentials:

username: guest
password: guest

# 🔹 MySQL
localhost:3307


##  Credentials (from docker-compose):

username: root
password: root
database: order_db

# # 📬 Message Queues
# Exchange
order.events

# Routing Keys
order.placed
order.processed

# Queues
order.placed.queue
order.processed.queue
order.dlq

# 📨 Example OrderPlacedEvent
{
  "orderId": "123",
  "productId": "P100",
  "quantity": 2,
  "customerId": "C001",
  "timestamp": "2026-02-17T12:00:00Z"
}


## Publish this to:

# Exchange:

order.events
Routing Key:
order.placed

# The service will:

Save order in DB
Update status to PROCESSED
Publish OrderProcessedEvent

## 🧪 Running Tests
mvn clean test

# Includes:

Unit tests (Service Layer)
Integration tests (RabbitMQ + H2)

## 🔄 Event Flow

OrderPlacedEvent received
Idempotency check (by orderId)
Order saved/updated
Status set to PROCESSED
OrderProcessedEvent published
ACK message
Errors routed to DLQ

## 🧠 Key Design Concepts Implemented

Stateless microservice
JSON message serialization
Manual ACK handling
Dead Letter Queue (DLQ)
Transactional DB updates
Retry-ready design
Environment variable configuration
Structured logging

## 📦 Environment Variables
SPRING_DATASOURCE_URL
SPRING_DATASOURCE_USERNAME
SPRING_DATASOURCE_PASSWORD
SPRING_RABBITMQ_HOST
SPRING_RABBITMQ_PORT
SPRING_RABBITMQ_USERNAME
SPRING_RABBITMQ_PASSWORD

## 🐳 Docker Commands

# Stop services:

docker compose down

# Remove volumes:

docker compose down -v

## 📊 Observability

# Health Endpoint:

/actuator/health


## Logs:

docker compose logs -f

## 🏆 Learning Outcomes

# This project demonstrates:

Distributed system design
Event-driven architecture
Fault tolerance & idempotency
Production-ready Spring Boot configuration
Containerized microservices
Testing strategies for async systems

## 📌 Future Improvements

Add Retry with Exponential Backoff
Implement Circuit Breaker
Add Prometheus Metrics
Deploy to Kubernetes
Add Testcontainers
Implement Saga pattern


## ✅ All Working URLs (Final Checklist)
Service	URL
Health	        http://localhost:8080/health
RabbitMQ UI 	http://localhost:15672