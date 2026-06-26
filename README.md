# Insurance Agentic Platform

![CI](https://github.com/m-monea/insurance-agentic-platform/actions/workflows/ci.yml/badge.svg)

 **Live Demo:** https://insurance-frontend-angular.onrender.com

AI-powered insurance claims management platform built with Java 21, Spring Boot, Angular 18, PostgreSQL, Docker, Render and Ollama/Llama 3.2.

## Live Services

Frontend  
https://insurance-frontend-angular.onrender.com

Customer Service  
https://insurance-customer-service.onrender.com/api/clienti

Claim Service  
https://insurance-claim-service.onrender.com/api/sinistri

AI Agent Service  
https://insurance-ai-agent-service.onrender.com/api/agente/storico

## Features

- Customer CRUD
- Claim CRUD
- AI-assisted claim analysis
- AI report history
- Dashboard statistics
- PostgreSQL persistence
- Dockerized services
- GitHub Actions CI
- Render deployment

## Architecture

```text
Angular Frontend
      |
      | REST
      |
Customer Service     Claim Service     AI Agent Service
      |                   |                  |
      +-------------------+------------------+
                          |
                     PostgreSQL


## Tech Stack

Java 21
Spring Boot 3.3
Angular 18
PostgreSQL
Docker
Docker Compose
GitHub Actions
Render
Ollama / Llama 3.2
🚀 Local Run
git clone https://github.com/m-monea/insurance-agentic-platform.git
cd insurance-agentic-platform
docker compose up --build

Frontend locale:

http://localhost:4200
🧪 CI

Every push to main runs:

Maven build
Angular build
Docker validation

## Status

Completed:

Microservices architecture
Frontend dashboard
CRUD clienti
CRUD sinistri
AI analysis
AI history
GitHub Actions
Render live deploy

## Next improvements:

JWT authentication
PDF reports
File upload
More automated tests
API Gateway stabilization