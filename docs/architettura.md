# Architettura

La piattaforma usa tre microservizi backend e un frontend Angular.

## claim-service

Gestisce i sinistri assicurativi. Espone API per creare, leggere e aggiornare i sinistri.

## customer-service

Gestisce l'anagrafica clienti e i dati di polizza.

## ai-agent-service

Coordina il flusso agentic:

1. riceve l'id del sinistro;
2. recupera il sinistro dal `claim-service`;
3. recupera il cliente dal `customer-service`;
4. controlla completezza dei dati;
5. genera checklist, priorità, riassunto e report finale.

Se `OPENAI_API_KEY` è presente, il servizio può chiamare OpenAI. Se non è presente, usa un motore locale simulato, utile per demo senza costi.
