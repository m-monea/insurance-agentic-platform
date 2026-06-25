# Insurance Agentic Platform

An AI-powered insurance claims management platform built with **Java 21**, **Spring Boot**, **Angular 18**, **PostgreSQL**, **Docker**, and **Ollama (Llama 3.2)**.

The project demonstrates a modern microservices architecture where an AI agent assists insurance operators in analyzing claims, identifying missing information, estimating claim priority, and generating detailed reports.

---

# Screenshots

> *(Coming soon after deployment)*

---

# Features

## Customer Management

- Create customers
- View customer list
- Delete customers

## Claim Management

- Create insurance claims
- View all claims
- Delete claims
- Estimate claim amount
- Upload claim documents (ready for extension)

## AI Agent

- AI analysis using **Ollama + Llama 3.2**
- Automatic report generation
- Missing information detection
- Suggested documents
- Priority estimation
- AI analysis history stored in PostgreSQL
- Delete analysis history

## Dashboard

- Customer statistics
- Claims statistics
- Total estimated amount
- AI reports

---

# Architecture

```
                +----------------------+
                |  Angular Frontend    |
                +----------+-----------+
                           |
               REST APIs
                           |
        +------------------+------------------+
        |                                     |
+--------------------+              +--------------------+
| Customer Service   |              | Claim Service      |
+--------------------+              +--------------------+
        |                                     |
        +------------------+------------------+
                           |
                  PostgreSQL Database
                           |
                    +----------------+
                    | AI Agent       |
                    | Spring Boot    |
                    | Ollama Llama3  |
                    +----------------+
```

---

# Tech Stack

## Backend

- Java 21
- Spring Boot 3
- Spring Web
- Spring Data JPA
- PostgreSQL

## Frontend

- Angular 18
- TypeScript
- CSS

## AI

- Ollama
- Llama 3.2

## DevOps

- Docker
- Docker Compose
- GitHub Actions

---

# Project Structure

```
insurance-agentic-platform
│
├── ai-agent-service
├── claim-service
├── customer-service
├── frontend-angular
├── docs
├── docker-compose.yml
└── README.md
```

---

# Getting Started

## Clone repository

```bash
git clone https://github.com/m-monea/insurance-agentic-platform.git

cd insurance-agentic-platform
```

---

## Start Ollama

```bash
ollama serve
```

Download the model:

```bash
ollama pull llama3.2
```

---

## Start PostgreSQL and services

```bash
docker compose up --build
```

or

```bash
docker compose up -d
```

---

## Start Angular (development)

```bash
cd frontend-angular

npm install

ng serve
```

---

# Services

| Service | URL |

| Frontend | http://localhost:4200 |
| Claim Service | http://localhost:8081 |
| Customer Service | http://localhost:8082 |
| AI Agent | http://localhost:8083 |
| Ollama | http://localhost:11434 |

---

# AI Workflow

1. Create a customer
2. Create a claim
3. Open AI Analysis
4. Select claim
5. AI retrieves:

- customer
- claim
- estimated amount
- available documents

6. AI generates

- summary
- missing information
- suggested documents
- priority
- final report

7. Analysis is stored in PostgreSQL.

---

# Current Status

## Completed

- Microservices architecture
- Angular frontend
- Customer CRUD
- Claim CRUD
- Delete customers
- Delete claims
- Dashboard
- AI local analysis
- Ollama integration
- AI history
- Delete AI history
- PostgreSQL persistence
- Docker
- Docker Compose
- GitHub
- GitHub Actions CI

---

## In Progress

- PDF report generation
- JWT Authentication
- Document upload
- Remote deployment
- Automated tests

---

# CI/CD

Every push automatically runs:

- Maven Build
- Maven Verify
- Angular Build
- Docker Image Build

using **GitHub Actions**.

---

# Future Improvements

- JWT Authentication
- Role-based authorization
- PDF reports
- File uploads
- OpenAPI / Swagger
- Kubernetes deployment
- Monitoring (Prometheus + Grafana)
- Redis cache
- AI model switching
- RAG with company documents
