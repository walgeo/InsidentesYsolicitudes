-- Script para insertar datos de prueba directamente

-- Conectarse al schema correcto
SET search_path TO incidentes_db;

-- Verificar que las tablas existen
SELECT table_name FROM information_schema.tables WHERE table_schema = 'incidentes_db';

-- Verificar datos en cat_prioridades
SELECT * FROM cat_prioridades;

-- Verificar datos en cat_estados
SELECT * FROM cat_estados;

-- Limpiar tickets existentes (opcional)
-- DELETE FROM comentarios;
-- DELETE FROM tickets;

-- Insertar tickets de prueba
INSERT INTO tickets (titulo, descripcion, tipo, aplicacion, prioridad_id, estado_id, creado_por, creado_en, actualizado_en)
VALUES
('Error de login en producción', 'Los usuarios no pueden autenticarse en el sistema', 'INCIDENTE', 'AUTH_SERVICE', 3, 1, 'admin@altamira.com', CURRENT_TIMESTAMP - INTERVAL '2 days', CURRENT_TIMESTAMP - INTERVAL '2 days'),
('Solicitud de nueva funcionalidad de reportes', 'Se requiere módulo de reportes mensuales', 'SOLICITUD', 'REPORT_SERVICE', 2, 2, 'user@altamira.com', CURRENT_TIMESTAMP - INTERVAL '5 days', CURRENT_TIMESTAMP - INTERVAL '1 day'),
('Optimizar consulta lenta en dashboard', 'El dashboard tarda más de 10 segundos en cargar', 'INCIDENTE', 'DASHBOARD_APP', 2, 2, 'dev@altamira.com', CURRENT_TIMESTAMP - INTERVAL '1 day', CURRENT_TIMESTAMP - INTERVAL '1 hour'),
('Sistema caído en producción', 'El sistema principal no responde', 'INCIDENTE', 'CORE_SERVICE', 4, 3, 'ops@altamira.com', CURRENT_TIMESTAMP - INTERVAL '10 hours', CURRENT_TIMESTAMP - INTERVAL '2 hours'),
('Actualizar versión de librerías', 'Actualizar Spring Boot a última versión', 'SOLICITUD', 'CORE_SERVICE', 1, 1, 'dev@altamira.com', CURRENT_TIMESTAMP - INTERVAL '7 days', CURRENT_TIMESTAMP - INTERVAL '7 days')
ON CONFLICT DO NOTHING;

-- Verificar que se insertaron
SELECT COUNT(*) as total_tickets FROM tickets;
SELECT id, titulo, tipo, prioridad_id, estado_id FROM tickets;

