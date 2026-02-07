-- H2-compatible seed data for tests
SET SCHEMA incidentes_db;

INSERT INTO tickets (titulo, descripcion, tipo, aplicacion, prioridad_id, estado_id, creado_por, creado_en, actualizado_en)
SELECT 'Error de login en producción', 'Los usuarios no pueden autenticarse en el sistema', 'INCIDENTE', 'AUTH_SERVICE',
       (SELECT id FROM cat_prioridades WHERE nombre = 'ALTA'),
       (SELECT id FROM cat_estados WHERE nombre = 'ABIERTO'),
       'admin@altamira.com', CURRENT_TIMESTAMP - INTERVAL '2' DAY, CURRENT_TIMESTAMP - INTERVAL '2' DAY
WHERE NOT EXISTS (SELECT 1 FROM tickets WHERE titulo = 'Error de login en producción');

INSERT INTO tickets (titulo, descripcion, tipo, aplicacion, prioridad_id, estado_id, creado_por, creado_en, actualizado_en)
SELECT 'Solicitud de nueva funcionalidad de reportes', 'Se requiere módulo de reportes mensuales', 'SOLICITUD', 'REPORT_SERVICE',
       (SELECT id FROM cat_prioridades WHERE nombre = 'MEDIA'),
       (SELECT id FROM cat_estados WHERE nombre = 'EN_PROGRESO'),
       'user@altamira.com', CURRENT_TIMESTAMP - INTERVAL '5' DAY, CURRENT_TIMESTAMP - INTERVAL '1' DAY
WHERE NOT EXISTS (SELECT 1 FROM tickets WHERE titulo = 'Solicitud de nueva funcionalidad de reportes');

INSERT INTO tickets (titulo, descripcion, tipo, aplicacion, prioridad_id, estado_id, creado_por, creado_en, actualizado_en)
SELECT 'Sistema caído en producción', 'El sistema principal no responde', 'INCIDENTE', 'CORE_SERVICE',
       (SELECT id FROM cat_prioridades WHERE nombre = 'CRITICA'),
       (SELECT id FROM cat_estados WHERE nombre = 'RESUELTO'),
       'ops@altamira.com', CURRENT_TIMESTAMP - INTERVAL '10' HOUR, CURRENT_TIMESTAMP - INTERVAL '2' HOUR
WHERE NOT EXISTS (SELECT 1 FROM tickets WHERE titulo = 'Sistema caído en producción');

INSERT INTO comentarios (ticket_id, mensaje, creado_por, creado_en)
SELECT t.id, 'Investigando el problema de autenticación', 'admin@altamira.com', CURRENT_TIMESTAMP - INTERVAL '1' DAY
FROM tickets t
WHERE t.titulo = 'Error de login en producción'
AND NOT EXISTS (SELECT 1 FROM comentarios c WHERE c.ticket_id = t.id AND c.mensaje = 'Investigando el problema de autenticación');

