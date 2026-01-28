# Event-Driven Architecture with Spring Cloud Stream & Kafka

This project demonstrates how to design a **reliable, resilient, event-driven system** using **Spring Cloud Stream**, **Kafka**, and **Spring Boot**, addressing **real production problems** such as:

- message loss
- retry handling
- dead-letter queues (DLQ)
- broker unavailability
- resilience and fault isolation
- observability and testing

The goal is not to show *how to send a message*, but **how to not lose it**.

---

##  Architectural Goals

- **Reliability** – no message must be lost
- **Resilience** – Kafka downtime must not crash the system
- **Separation of concerns** – each microservice has a single responsibility
- **Controlled retries** – no infinite loops or blind retries
- **Observability** – failures are visible and traceable
- **Production-ready testing**

---

##  System Architecture

The system is composed of **four independent microservices**, communicating only via Kafka:

- **Source Application**
- **Processor Application**
- **Sink Application**
- **DLQ Application**

Configuration is centralized using **Spring Cloud Config Server**, with dynamic refresh via **Spring Cloud Bus**.

Kafka Topics:
- `order-topic`
- `stock-topic`
- `order-topic.dlq`
- `stock-topic.dlq`
- `spring-cloud-bus`

---

##  Technologies Used

- **Spring Boot**
- **Spring Cloud Stream & Function**
- **Apache Kafka**
- **KafkaTemplate** (synchronous producer)
- **Spring Cloud Config Server**
- **Spring Cloud Bus**
- **Resilience4j (Circuit Breaker)**
- **Spring Scheduling**
- **MySQL**
- **Testcontainers**
- **Embedded Kafka**
- **Docker & Docker Compose**

---

##  Microservices Overview

### 1️ Configuration Server
- Centralized configuration
- Properties stored in GitHub
- Dynamic refresh via Kafka (Spring Cloud Bus)

---

### 2️ Source Application (HTTP → Kafka)

**Responsibilities:**
- Accept HTTP requests
- Persist events using the **Outbox pattern**
- Send messages to Kafka via a scheduled job
- Handle retry, parking, and failure logic
- Protect itself using **Circuit Breaker**

**Why KafkaTemplate here?**

The Source is the only **synchronous entry point** (HTTP).
We need **acknowledgment from Kafka** to be sure the message has been accepted.

KafkaTemplate allows:
- synchronous send
- waiting for broker acknowledgment
- controlled failure handling

Spring Cloud Stream is intentionally **not used here**.

---

### 3️ Processor Application (Kafka → Kafka)

**Responsibilities:**
- Consume events from `order-topic`
- Validate incoming messages
- Enrich data (e.g. stock availability)
- Publish to `stock-topic`

This service uses **Spring Cloud Stream**:
- automatic retry
- automatic DLQ handling
- no Kafka-specific code
- fully asynchronous and decoupled

---

### 4️ Sink Application (Kafka → DB)

**Responsibilities:**
- Consume enriched events
- Validate payload
- Persist data

Any error (validation, mapping, DB failure) results in:
 automatic message redirection to DLQ.

---

### 5️ DLQ Application

**Responsibilities:**
- Consume messages from DLQ topics
- Persist failed events
- Periodically log / notify failures
- Ready to be extended with email or alerting

---

##  Reliability Patterns

###  Outbox Pattern
Ensures that HTTP events are **never lost**, even if Kafka is temporarily unavailable.

###  Parking Pattern
Messages that fail repeatedly are parked for manual inspection.

###  Circuit Breaker
Protects the Source application from wasting resources when Kafka is unavailable.

---

##  Testing Strategy

- **Embedded Kafka** for producer-side tests
- **Spring Cloud Stream Test Binder** for Processor & Sink
- **Testcontainers** for full integration testing
- Realistic failure simulation (broker down, retries, DLQ)

---

##  How to Run the Project

### 1. Clone the repository
```bash
git clone <REPO_URL>

### 2. Start infrastructure
docker-compose up -d

### 3. Send an order
POST /api/source
{
  "name": "phone",
  "cost": 12.5
}

### 4. Inspect Kafka topics
docker exec -it kafka bash
kafka-console-consumer --bootstrap-server kafka:9092 --topic order-topic --from-beginning


### 5. Inspect MySQL
docker exec -it mysql bash
mysql -uroot -proot
show databases;
use appdb;
show tables;

