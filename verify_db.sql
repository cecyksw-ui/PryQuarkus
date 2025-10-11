-- Script para verificar el estado de la base de datos
SELECT version();

-- Verificar si la base de datos 'prueba' existe
SELECT datname FROM pg_database WHERE datname = 'prueba';

-- Verificar si el esquema 'arq_hex' existe
SELECT schema_name FROM information_schema.schemata WHERE schema_name = 'arq_hex';

-- Verificar tablas en el esquema arq_hex
SELECT table_name 
FROM information_schema.tables 
WHERE table_schema = 'arq_hex';

-- Verificar el estado de flyway_schema_history
SELECT * FROM flyway_schema_history ORDER BY installed_rank;