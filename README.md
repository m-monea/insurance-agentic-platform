#  Insurance Agentic Platform

[![CI](https://github.com/m-monea/insurance-agentic-platform/actions/workflows/ci.yml/badge.svg)](https://github.com/m-monea/insurance-agentic-platform/actions/workflows/ci.yml)

A modern **AI-powered insurance claims management platform** built with **Java 21, Spring Boot, Angular, PostgreSQL, Docker and Ollama**.

The platform demonstrates a microservices architecture where customers, insurance claims and AI-assisted claim analysis are managed through independent Spring Boot services.

---

# Live Demo

### Frontend

https://insurance-frontend-angular.onrender.com

### Services

Customer Service

https://insurance-customer-service.onrender.com

Claim Service

https://insurance-claim-service.onrender.com

AI Agent Service

https://insurance-ai-agent-service.onrender.com

API Gateway

https://insurance-api-gateway-xv9w.onrender.com

---

# Architecture

```
                    Angular Frontend
                           │
                           ▼
                 API Gateway (Spring)
                           │
      ┌────────────────────┼────────────────────┐
      ▼                    ▼                    ▼
Customer Service     Claim Service       AI Agent Service
      │                    │                    │
      └──────────────┬─────┴────────────────────┘
                     ▼
               PostgreSQL Database

               Optional Local Ollama
               (Llama 3.2)
```

---

# Features

## Customer Management

- Create customer
- Edit customer
- Delete customer
- Search by surname
- PostgreSQL persistence

## Claim Management

- Create claim
- Edit claim
- Delete claim
- Filter by customer
- Estimated damage amount

## AI Agent

- Automatic claim analysis
- Priority calculation
- Missing information detection
- Suggested required documents
- AI analysis history
- Local fallback analysis
- Optional Ollama integration

## Dashboard

- Total customers
- Total claims
- Claims by category
- Priority statistics
- Estimated total value
- Average claim value

---

# Tech Stack

Backend

- Java 21
- Spring Boot 3
- Spring Web
- Spring Data JPA
- Spring WebFlux
- Maven

Frontend

- Angular 18
- TypeScript
- HTML
- CSS

Database

- PostgreSQL

AI

- Ollama
- Llama 3.2
- Local heuristic fallback

DevOps

- Docker
- Render
- GitHub Actions

Testing

- JUnit 5
- Spring Boot Test
- MockMvc

---

# Microservices

## customer-service

Manages insurance customers.

Endpoints

```
GET     /api/clienti
GET     /api/clienti/{id}
POST    /api/clienti
PUT     /api/clienti/{id}
DELETE  /api/clienti/{id}
DELETE  /api/clienti
```

---

## claim-service

Manages insurance claims.

Endpoints

```
GET     /api/sinistri
GET     /api/sinistri/{id}
POST    /api/sinistri
PUT     /api/sinistri/{id}
DELETE  /api/sinistri/{id}
DELETE  /api/sinistri
```

---

## ai-agent-service

AI-powered insurance analysis.

Endpoints

```
POST    /api/agente/analizza/{id}
GET     /api/agente/storico
GET     /api/agente/storico/{id}
DELETE  /api/agente/storico/{id}
DELETE  /api/agente/storico
```

---

## api-gateway-service

Spring Cloud Gateway exposing all backend services.

---

# Running Locally

Clone repository

```bash
git clone https://github.com/m-monea/insurance-agentic-platform.git
```

Start PostgreSQL

Configure application.yml files.

Run

```bash
mvn spring-boot:run
```

Frontend

```bash
cd frontend-angular
npm install
ng serve
```

---

# Docker

Each microservice includes its own Dockerfile.

Example

```bash
docker build -t customer-service .
docker run customer-service
```

---

# Testing

Run tests

```bash
mvn clean test
```

Included tests

- Spring Boot context loading
- Customer REST Controller
- Claim REST Controller

---

# CI/CD

GitHub Actions automatically executes

- Maven Build
- Unit Tests

on every push and pull request.

---

# Project Structure

```
insurance-agentic-platform

├── customer-service
├── claim-service
├── ai-agent-service
├── api-gateway-service
├── frontend-angular
├── .github
└── README.md
```

---

# Future Improvements

- JWT Authentication
- Role Based Authorization
- Swagger / OpenAPI
- Kafka Event Bus
- Redis Cache
- OCR for uploaded documents
- Fraud Detection AI
- Kubernetes deployment
- Monitoring with Prometheus
- OpenTelemetry tracing
