-- Consultar usuarios
SELECT 'Usuarios en la base de datos:' AS resultado;
SELECT * FROM incidentes_db.usuarios;

-- Consulta para obtener el usuario admin y su contraseña
SELECT usuario, contrasena
FROM incidentes_db.usuarios
WHERE usuario = 'admin';

-- Consultar roles
SELECT 'Roles en la base de datos:' AS resultado;
SELECT * FROM incidentes_db.cat_roles;

