CREATE SCHEMA IF NOT EXISTS incidentes_db;

CREATE TABLE IF NOT EXISTS incidentes_db.cat_roles (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    descripcion VARCHAR(200),
    actualizado_en TIMESTAMP,
    actualizado_por VARCHAR(100)
);

ALTER TABLE incidentes_db.cat_roles
ADD COLUMN IF NOT EXISTS creado_en TIMESTAMP;

ALTER TABLE incidentes_db.cat_roles
ADD COLUMN IF NOT EXISTS creado_por VARCHAR(100);

CREATE TABLE IF NOT EXISTS incidentes_db.usuarios (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    correo VARCHAR(150) NOT NULL UNIQUE,
    usuario VARCHAR(100) NOT NULL UNIQUE,
    contrasena VARCHAR(255) NOT NULL,
    rol_id INTEGER NOT NULL REFERENCES incidentes_db.cat_roles(id),
    creado_en TIMESTAMP NOT NULL DEFAULT NOW(),
    actualizado_en TIMESTAMP,
    creado_por VARCHAR(100) NOT NULL,
    actualizado_por VARCHAR(100),
    activo BOOLEAN DEFAULT TRUE
);

CREATE INDEX IF NOT EXISTS idx_usuarios_usuario ON incidentes_db.usuarios(usuario);
CREATE INDEX IF NOT EXISTS idx_usuarios_correo ON incidentes_db.usuarios(correo);
CREATE INDEX IF NOT EXISTS idx_usuarios_rol_id ON incidentes_db.usuarios(rol_id);

INSERT INTO incidentes_db.cat_roles (nombre, descripcion)
SELECT 'USER', 'Usuario basico'
WHERE NOT EXISTS (SELECT 1 FROM incidentes_db.cat_roles WHERE nombre = 'USER');

INSERT INTO incidentes_db.cat_roles (nombre, descripcion)
SELECT 'AGENT', 'Agente de soporte'
WHERE NOT EXISTS (SELECT 1 FROM incidentes_db.cat_roles WHERE nombre = 'AGENT');

INSERT INTO incidentes_db.cat_roles (nombre, descripcion)
SELECT 'ADMIN', 'Administrador'
WHERE NOT EXISTS (SELECT 1 FROM incidentes_db.cat_roles WHERE nombre = 'ADMIN');

INSERT INTO incidentes_db.usuarios (
    nombre,
    apellido,
    correo,
    usuario,
    contrasena,
    rol_id,
    creado_por
)
SELECT
    'Admin',
    'Sistema',
    'admin@local',
    'admin',
    '$2b$12$lgqe13BO6WK8ohboI44npua0Vg02iZtCNMhp5usakm5rq7.tAAOUO',
    r.id,
    'system'
FROM incidentes_db.cat_roles r
WHERE r.nombre = 'ADMIN'
  AND NOT EXISTS (SELECT 1 FROM incidentes_db.usuarios u WHERE u.usuario = 'admin');
