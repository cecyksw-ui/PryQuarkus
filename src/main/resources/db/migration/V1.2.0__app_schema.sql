-- Crea las tablas en el esquema arq_hex

CREATE TABLE arq_hex.gencatsenca (
    codcat BIGSERIAL PRIMARY KEY,
    catdesc VARCHAR(20),
    codusr VARCHAR(20),
    fechcrea TIMESTAMP,
    fechmod TIMESTAMP
);

CREATE TABLE arq_hex.gencatsdeta (
    codcat BIGINT NOT NULL,
    codcor BIGINT NOT NULL,
    cordesc VARCHAR(20),
    codusr VARCHAR(20),
    fechcrea TIMESTAMP,
    fechmod TIMESTAMP,
    PRIMARY KEY (codcat, codcor),
    FOREIGN KEY (codcat) REFERENCES arq_hex.gencatsenca (codcat)
);

-- ============================================
-- Tabla Persona
-- ============================================
CREATE TABLE arq_hex.persona (
    persona_id VARCHAR(10) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    genero VARCHAR(10),
    edad INTEGER,    
    direccion VARCHAR(150),
    telefono VARCHAR(20)
);

-- ============================================
-- Tabla Cliente (hereda de Persona)
-- ============================================
CREATE TABLE arq_hex.cliente (
    cliente_id SERIAL PRIMARY KEY,
    persona_id VARCHAR(10) UNIQUE NOT NULL,
    contrasena VARCHAR(50) NOT NULL,
    estado BOOLEAN NOT NULL DEFAULT TRUE,
    FOREIGN KEY (persona_id) REFERENCES arq_hex.persona(persona_id)
);

-- ============================================
-- Tabla Cuenta
-- ============================================
CREATE TABLE arq_hex.cuenta (
    cuenta_id SERIAL PRIMARY KEY,
    numero_cuenta VARCHAR(20) UNIQUE NOT NULL,
    tipo_cuenta VARCHAR(20),
    saldo_inicial NUMERIC(10,2) DEFAULT 0.00,
    estado BOOLEAN NOT NULL DEFAULT TRUE,
    cliente_id INTEGER NOT NULL,
    FOREIGN KEY (cliente_id) REFERENCES arq_hex.cliente(cliente_id)
);

-- ============================================
-- Tabla Movimiento
-- ============================================
CREATE TABLE arq_hex.movimiento (
    movimiento_id SERIAL PRIMARY KEY,
    fecha DATE NOT NULL DEFAULT CURRENT_DATE,
    tipo_movimiento VARCHAR(20) NOT NULL,
    valor NUMERIC(10,2) NOT NULL,
    saldo NUMERIC(10,2) NOT NULL,
    cuenta_id INTEGER NOT NULL,
    FOREIGN KEY (cuenta_id) REFERENCES arq_hex.cuenta(cuenta_id)
);

