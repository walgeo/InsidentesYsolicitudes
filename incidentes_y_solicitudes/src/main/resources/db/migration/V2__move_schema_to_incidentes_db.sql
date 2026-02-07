CREATE SCHEMA IF NOT EXISTS incidentes_db;

ALTER TABLE IF EXISTS public.cat_prioridades SET SCHEMA incidentes_db;
ALTER TABLE IF EXISTS public.cat_estados SET SCHEMA incidentes_db;
ALTER TABLE IF EXISTS public.tickets SET SCHEMA incidentes_db;
ALTER TABLE IF EXISTS public.comentarios SET SCHEMA incidentes_db;
ALTER SEQUENCE IF EXISTS public.ticket_id_seq SET SCHEMA incidentes_db;
ALTER SEQUENCE IF EXISTS public.comentarios_id_seq SET SCHEMA incidentes_db;

