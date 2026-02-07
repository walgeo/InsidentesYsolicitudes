CREATE TABLE cat_prioridades (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(20) NOT NULL UNIQUE,
    creado_por VARCHAR(50) NOT NULL DEFAULT 'SYSTEM',
    actualizado_por VARCHAR(50),
    creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    actualizado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE cat_estados (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(20) NOT NULL UNIQUE,
    creado_por VARCHAR(50) NOT NULL DEFAULT 'SYSTEM',
    actualizado_por VARCHAR(50),
    creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    actualizado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE SEQUENCE IF NOT EXISTS ticket_id_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE tickets (
    id BIGINT PRIMARY KEY DEFAULT nextval('ticket_id_seq'),
    titulo VARCHAR(100) NOT NULL,
    descripcion TEXT,
    tipo VARCHAR(20) NOT NULL,
    aplicacion VARCHAR(50) NOT NULL,
    prioridad_id INT NOT NULL,
    estado_id INT NOT NULL,
    asignado_a VARCHAR(50),
    creado_por VARCHAR(50) NOT NULL,
    actualizado_por VARCHAR(50),
    creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    actualizado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_ticket_prioridad FOREIGN KEY (prioridad_id) REFERENCES cat_prioridades(id),
    CONSTRAINT fk_ticket_estado FOREIGN KEY (estado_id) REFERENCES cat_estados(id)
);

CREATE TABLE comentarios (
    id BIGSERIAL PRIMARY KEY,
    ticket_id BIGINT NOT NULL,
    mensaje TEXT NOT NULL,
    creado_por VARCHAR(50) NOT NULL,
    actualizado_por VARCHAR(50),
    creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    actualizado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_ticket_comentario FOREIGN KEY (ticket_id) REFERENCES tickets(id) ON DELETE CASCADE
);

INSERT INTO cat_prioridades (nombre, creado_por) VALUES 
('BAJA', 'SYSTEM'), ('MEDIA', 'SYSTEM'), ('ALTA', 'SYSTEM'), ('CRITICA', 'SYSTEM');

INSERT INTO cat_estados (nombre, creado_por) VALUES 
('ABIERTO', 'SYSTEM'), ('EN_PROGRESO', 'SYSTEM'), ('RESUELTO', 'SYSTEM'), ('CERRADO', 'SYSTEM');

CREATE INDEX idx_tickets_estado ON tickets(estado_id);
CREATE INDEX idx_tickets_aplicacion ON tickets(aplicacion);
CREATE INDEX idx_tickets_creado_en ON tickets(creado_en);