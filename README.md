# Insurance Agentic Platform

Una piattaforma dimostrativa di gestione sinistri assicurativi basata su architettura a microservizi, con un agente AI locale che supporta l'operatore nell'analisi dei sinistri.

Il progetto è stato sviluppato come portfolio per mostrare competenze in Java, Spring Boot, Angular, Docker, PostgreSQL e AI generativa.

---

# Architettura

```
                +----------------------+
                |  Angular Frontend    |
                |      Port 4200       |
                +----------+-----------+
                           |
                           |
          -------------------------------------
          |                 |                 |
          |                 |                 |
+---------v------+ +--------v-------+ +-------v-------+
| Claim Service  | | CustomerService| | AI Agent      |
| Spring Boot    | | Spring Boot    | | Spring Boot   |
| Port 8081      | | Port 8082      | | Port 8083     |
+--------+-------+ +--------+-------+ +-------+-------+
         |                  |                 |
         +------------------+-----------------+
                            |
                    PostgreSQL 16
                      Port 5432
                            |
                         Ollama
                   Llama 3.2 Local AI
```

---

# Tecnologie

Backend

- Java 21
- Spring Boot 3.3
- Spring Web
- Spring WebFlux
- Spring Data JPA
- PostgreSQL
- Maven

Frontend

- Angular 18
- Standalone Components
- TypeScript
- HTML
- CSS

AI

- Ollama
- Llama 3.2
- Prompt Engineering

DevOps

- Docker
- Docker Compose

---

# Funzionalità

## Gestione clienti

- elenco clienti
- recupero cliente
- integrazione REST

## Gestione sinistri

- creazione sinistro
- ricerca sinistro
- validazione dati

## Agente AI

Analizza automaticamente:

- completezza del sinistro
- documentazione presente
- documenti mancanti
- priorità
- report finale

Il motore AI utilizza:

- Ollama
- Llama 3.2

con fallback automatico all'analisi locale nel caso in cui il modello non sia disponibile.

---

# Storico Analisi AI

Ogni analisi viene salvata nel database.

Sono disponibili:

- elenco analisi
- ricerca storico per sinistro
- eliminazione singola analisi
- eliminazione completa dello storico

---

# Dashboard

La dashboard Angular permette di:

- creare un nuovo sinistro
- analizzare un sinistro
- visualizzare clienti
- consultare lo storico
- eliminare analisi
- visualizzare badge di priorità
- leggere il report AI

---

# API

## Claim Service

```
GET    /api/sinistri
GET    /api/sinistri/{id}
POST   /api/sinistri
```

---

## Customer Service

```
GET    /api/clienti
GET    /api/clienti/{id}
```

---

## AI Agent

```
POST   /api/agente/analizza/{id}

GET    /api/agente/storico

GET    /api/agente/storico/{sinistroId}

DELETE /api/agente/storico/{id}

DELETE /api/agente/storico
```

---

# Avvio

## Clonare il repository

```bash
git clone https://github.com/TUO_USERNAME/insurance-agentic-platform.git
```

```
cd insurance-agentic-platform
```

---

## Avviare Ollama

Installare Ollama

https://ollama.com

Scaricare il modello

```bash
ollama pull llama3.2
```

Avviare

```bash
ollama serve
```

---

## Avvio piattaforma

```bash
docker compose up --build
```

---

# URL

Frontend

```
http://localhost:4200
```

Claim Service

```
http://localhost:8081
```

Customer Service

```
http://localhost:8082
```

AI Agent

```
http://localhost:8083
```

---

# Screenshot

Da aggiungere:

- Dashboard
- Analisi AI
- Storico
- Docker Containers

---

# Roadmap

- JWT Authentication
- Dashboard statistiche
- Grafici
- Ricerca avanzata
- Upload documenti
- PDF Report
- OCR documenti
- RAG con documentazione assicurativa
- AI multi-agent
- Deploy cloud
- HTTPS
- Dominio pubblico

---

# Struttura progetto

```
insurance-agentic-platform/

frontend-angular/

claim-service/

customer-service/

ai-agent-service/

docs/

docker-compose.yml
```

---