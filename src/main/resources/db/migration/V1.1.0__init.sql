-- Migración simplificada para PostgreSQL
-- Solo asegurar que el esquema arq_hex existe y tiene permisos

-- Crear el esquema si no existe
CREATE SCHEMA IF NOT EXISTS arq_hex;

-- Otorgar permisos al usuario postgres sobre el esquema
GRANT ALL ON SCHEMA arq_hex TO postgres;
GRANT ALL ON ALL TABLES IN SCHEMA arq_hex TO postgres;
GRANT ALL ON ALL SEQUENCES IN SCHEMA arq_hex TO postgres;