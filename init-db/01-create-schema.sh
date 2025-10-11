#!/bin/bash
set -e

# Crear el esquema arq_hex si no existe
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    CREATE SCHEMA IF NOT EXISTS arq_hex;
    GRANT ALL ON SCHEMA arq_hex TO $POSTGRES_USER;
EOSQL

echo "Esquema arq_hex creado exitosamente"