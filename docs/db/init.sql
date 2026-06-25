CREATE TABLE IF NOT EXISTS clienti (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(120) NOT NULL,
    cognome VARCHAR(120) NOT NULL,
    email VARCHAR(180) NOT NULL,
    telefono VARCHAR(50),
    numero_polizza VARCHAR(80) NOT NULL
);

CREATE TABLE IF NOT EXISTS sinistri (
    id BIGSERIAL PRIMARY KEY,
    cliente_id BIGINT NOT NULL,
    tipo VARCHAR(60) NOT NULL,
    descrizione TEXT NOT NULL,
    documenti_presenti TEXT,
    importo_stimato NUMERIC(12, 2),
    stato VARCHAR(60) NOT NULL DEFAULT 'APERTO',
    creato_il TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO clienti (nome, cognome, email, telefono, numero_polizza)
SELECT 'Mario', 'Rossi', 'mario.rossi@example.com', '+39 333 111 2222', 'POL-AUTO-001'
WHERE NOT EXISTS (SELECT 1 FROM clienti WHERE numero_polizza = 'POL-AUTO-001');

INSERT INTO clienti (nome, cognome, email, telefono, numero_polizza)
SELECT 'Giulia', 'Bianchi', 'giulia.bianchi@example.com', '+39 333 222 3333', 'POL-CASA-002'
WHERE NOT EXISTS (SELECT 1 FROM clienti WHERE numero_polizza = 'POL-CASA-002');

INSERT INTO sinistri (cliente_id, tipo, descrizione, documenti_presenti, importo_stimato, stato)
SELECT 1, 'AUTO', 'Tamponamento con danno al paraurti posteriore.', 'foto danno, denuncia', 1200.00, 'APERTO'
WHERE NOT EXISTS (SELECT 1 FROM sinistri WHERE descrizione LIKE 'Tamponamento%');
