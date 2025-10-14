# 🏦 Sistema Bancario con Quarkus y Arquitectura Hexagonal

Un sistema bancario completo desarrollado con **Java 21**, **Quarkus**, **PostgreSQL** y **Arquitectura Hexagonal**. Este proyecto demuestra mejores prácticas de desarrollo moderno, incluyendo APIs REST, gestión transaccional y pruebas automatizadas.

---

## 📋 **Tabla de Contenidos**

1. [🎯 ¿Qué es este proyecto?](#-qué-es-este-proyecto)
2. [🏗️ Arquitectura del Sistema](#️-arquitectura-del-sistema)
3. [🛠️ Tecnologías Utilizadas](#️-tecnologías-utilizadas)
4. [📁 Estructura del Proyecto](#-estructura-del-proyecto)
5. [⚡ Guía de Inicio Rápido](#-guía-de-inicio-rápido)
6. [🐳 Uso con Docker](#-uso-con-docker)
7. [🧪 Pruebas y Testing](#-pruebas-y-testing)
8. [📚 APIs Disponibles](#-apis-disponibles)
9. [🗄️ Base de Datos](#️-base-de-datos)
10. [🔧 Configuración Avanzada](#-configuración-avanzada)
11. [📊 Monitoreo y Debugging](#-monitoreo-y-debugging)
12. [🤝 Contribuir al Proyecto](#-contribuir-al-proyecto)
13. [❓ Troubleshooting](#-troubleshooting)

---

## 🎯 **¿Qué es este proyecto?**

Este es un **sistema bancario demo** que permite:

### ✅ **Funcionalidades Principales**
- **👤 Gestión de Personas**: Crear y administrar información personal
- **💳 Gestión de Clientes**: Convertir personas en clientes bancarios
- **🏧 Gestión de Cuentas**: Crear cuentas de ahorro y corrientes
- **💰 Movimientos Bancarios**: Realizar depósitos, retiros y consultar historial
- **📊 Reportes**: Generar estados de cuenta y reportes financieros

### 🎓 **¿Por qué es útil para aprender?**
- **Arquitectura Hexagonal**: Separación clara entre lógica de negocio e infraestructura
- **APIs REST modernas**: Endpoints bien documentados con OpenAPI/Swagger
- **Tecnologías actuales**: Java 21, Quarkus supersónico, PostgreSQL
- **Mejores prácticas**: Testing, Docker, CI/CD ready
- **Código limpio**: SOLID principles, Domain-Driven Design

---

## 🏗️ **Arquitectura del Sistema**

### 📐 **Arquitectura Hexagonal (Ports & Adapters)**

```
┌─────────────────────────────────────────────────────────┐
│                    🌐 ADAPTADORES                       │
├─────────────────────────────────────────────────────────┤
│  REST APIs    │  Database  │  Logger  │  External APIs  │
│  (Controllers)│  (JPA)     │  (Logs)  │  (Servicios)    │
└─────────────────┬───────────────────────────────────────┘
                  │
┌─────────────────┴───────────────────────────────────────┐
│                   ⚙️ PUERTOS                            │
├─────────────────────────────────────────────────────────┤
│  PersonaUseCase │ CuentaUseCase │ MovimientoUseCase     │
│  ClienteUseCase │ Repositories  │ Logging Ports         │
└─────────────────┬───────────────────────────────────────┘
                  │
┌─────────────────┴───────────────────────────────────────┐
│                  🧠 DOMINIO                             │
├─────────────────────────────────────────────────────────┤
│  Persona        │  Cliente     │  Cuenta               │
│  Movimiento     │  Catalogo    │  Reglas de Negocio    │
└─────────────────────────────────────────────────────────┘
```

### 🎯 **Beneficios de esta Arquitectura**
- **🔄 Independencia**: La lógica de negocio no depende de frameworks externos
- **🧪 Testeable**: Fácil testing con mocks y stubs
- **🔧 Mantenible**: Cambios en infraestructura no afectan el negocio
- **📈 Escalable**: Fácil agregar nuevos adaptadores

---

## 🛠️ **Tecnologías Utilizadas**

### ⚡ **Backend Core**
| Tecnología | Versión | Propósito |
|------------|---------|-----------|
| **Java** | 21 LTS | Lenguaje principal |
| **Quarkus** | 3.23.3 | Framework supersónico |
| **Maven** | 3.9+ | Gestión de dependencias |
| **PostgreSQL** | 15 | Base de datos |

### 📦 **Dependencias Principales**
| Librería | Propósito |
|----------|-----------|
| **Hibernate ORM Panache** | ORM simplificado |
| **RESTEasy Reactive** | APIs REST reactivas |
| **Flyway** | Migraciones de BD |
| **OpenAPI/Swagger** | Documentación de API |
| **MapStruct** | Mapeo de objetos |
| **JUnit 5 + Mockito** | Testing |

### 🐳 **Infraestructura**
- **Docker** & **Docker Compose** - Containerización
- **PgAdmin** - Administración de base de datos
- **Health Checks** - Monitoreo de aplicación

---

## 📁 **Estructura del Proyecto**

```
src/main/java/com/resolutions/
├── 🎯 model/                           # 🧠 DOMINIO
│   ├── Persona.java                    # Entidad persona base
│   ├── Cliente.java                    # Cliente bancario
│   ├── Cuenta.java                     # Cuenta bancaria
│   ├── Movimiento.java                 # Transacciones
│   └── Catalogo.java                   # Configuraciones
│
├── 🚪 application/                     # ⚙️ CASOS DE USO
│   ├── ports/in/                       # Puertos de entrada
│   │   ├── PersonaUseCase.java
│   │   ├── ClienteUseCase.java
│   │   ├── CuentaUseCase.java
│   │   └── MovimientoUseCase.java
│   ├── ports/out/                      # Puertos de salida
│   │   ├── PersonaRepositoryPort.java
│   │   ├── ClienteRepositoryPort.java
│   │   └── CuentaRepositoryPort.java
│   └── useCases/                       # Implementación de lógica
│       ├── PersonaUseCaseImpl.java
│       ├── ClienteUseCaseImpl.java
│       ├── CuentaUseCaseImpl.java
│       └── MovimientoUseCaseImpl.java
│
└── 🔌 adapters/                        # 🌐 ADAPTADORES
    ├── in/rest/                        # Controllers REST
    │   ├── PersonaResource.java        # API /api/personas
    │   ├── ClienteResource.java        # API /api/clientes
    │   ├── CuentaResource.java         # API /api/cuentas
    │   └── MovimientoResource.java     # API /api/movimientos
    └── out/persistence/                # Repositorios JPA
        ├── PersonaJpaRepository.java
        ├── ClienteJpaRepository.java
        ├── CuentaJpaRepository.java
        └── MovimientoJpaRepository.java
```

### 📊 **Base de Datos**
```
src/main/resources/
├── db/migration/                       # 🔄 Migraciones Flyway
│   ├── V1.0.0__create_schema.sql      # Creación del esquema
│   ├── V1.1.0__init.sql               # Inicialización
│   └── V1.2.0__app_schema.sql         # Tablas principales
└── application.properties             # ⚙️ Configuración
```

---

## ⚡ **Guía de Inicio Rápido**

### 📋 **Prerrequisitos**

Antes de comenzar, asegúrate de tener instalado:

| Software | Versión Mínima | Comando de Verificación |
|----------|----------------|-------------------------|
| **Java** | 21+ | `java -version` |
| **Maven** | 3.8+ | `mvn -version` |
| **Docker** | 20+ | `docker --version` |
| **Git** | 2.30+ | `git --version` |

### 🚀 **Opción 1: Inicio Rápido con Docker (Recomendado)**

```bash
# 1. Clonar el repositorio
git clone https://github.com/cecyksw-ui/PryQuarkus.git
cd PryQuarkus

# 2. Levantar todo el sistema con Docker
docker-compose up -d

# 3. Verificar que todo esté funcionando
curl http://localhost:8080/q/health
```

**🎉 ¡Listo!** El sistema estará disponible en:
- **API**: http://localhost:8080/api
- **Swagger UI**: http://localhost:8080/q/swagger-ui
- **Health Checks**: http://localhost:8080/q/health
- **PgAdmin**: http://localhost:5050 (admin@example.com / admin123)

### 🛠️ **Opción 2: Desarrollo Local**

```bash
# 1. Clonar el repositorio
git clone https://github.com/cecyksw-ui/PryQuarkus.git
cd PryQuarkus

# 2. Levantar solo PostgreSQL
docker-compose up -d postgres

# 3. Ejecutar la aplicación en modo desarrollo
# Windows:
.\mvnw.cmd quarkus:dev

# Linux/Mac:
./mvnw quarkus:dev
```

### ✅ **Verificar la Instalación**

```bash
# Verificar estado de la aplicación
curl http://localhost:8080/q/health

# Verificar la base de datos
curl http://localhost:8080/q/health/ready

# Ver documentación de API
# Abrir en navegador: http://localhost:8080/q/swagger-ui
```

---

## 🐳 **Uso con Docker**

### 🏃‍♂️ **Comandos Básicos**

```bash
# Levantar todo el sistema
docker-compose up -d

# Ver logs en tiempo real
docker-compose logs -f

# Ver logs de un servicio específico
docker-compose logs -f quarkus-app
docker-compose logs -f postgres

# Detener el sistema
docker-compose down

# Detener y limpiar volúmenes (¡CUIDADO! Borra los datos)
docker-compose down -v
```

### 🔧 **Comandos de Desarrollo**

```bash
# Rebuilding la aplicación
docker-compose up --build

# Ejecutar solo la base de datos para desarrollo local
docker-compose up -d postgres pgadmin

# Escalar la aplicación (múltiples instancias)
docker-compose up --scale quarkus-app=3
```

### 🐛 **Comandos de Debugging**

```bash
# Ejecutar con debug habilitado
docker-compose up -d
# La aplicación estará disponible para debug en puerto 5005

# Acceder al contenedor
docker-compose exec quarkus-app sh

# Ver recursos del sistema
docker-compose ps
docker stats
```

### ✅ **Script de Verificación de Docker**

```powershell
# Windows PowerShell
.\verify-docker.ps1
```

Este script verifica:
- ✅ Docker está instalado y corriendo
- ✅ Docker Compose está disponible
- ✅ Puertos necesarios están libres
- ✅ Recursos del sistema suficientes

---

## 🧪 **Pruebas y Testing**

### 📊 **Resumen de Pruebas**

El proyecto incluye **57 pruebas unitarias** que cubren:

| Componente | Número de Pruebas | Cobertura |
|------------|-------------------|-----------|
| **ClienteUseCaseImplTest** | 10 | Lógica de negocio de clientes |
| **CuentaUseCaseImplTest** | 16 | Lógica de negocio de cuentas |
| **ClienteResourceTest** | 15 | Endpoints REST de clientes |
| **CuentaResourceTest** | 16 | Endpoints REST de cuentas |

### 🚀 **Ejecutar Pruebas**

#### **Todas las Pruebas**
```bash
# Windows
.\mvnw.cmd test

# Linux/Mac
./mvnw test
```

#### **Pruebas Específicas**
```bash
# Solo pruebas de lógica de negocio (Use Cases)
.\mvnw.cmd test -Dtest="*UseCaseImplTest"

# Solo pruebas de API REST (Resources)
.\mvnw.cmd test -Dtest="*ResourceTest"

# Una clase específica
.\mvnw.cmd test -Dtest="ClienteUseCaseImplTest"

# Un método específico
.\mvnw.cmd test -Dtest="ClienteUseCaseImplTest#testCreateCliente_Success"
```

#### **Pruebas con Cobertura**
```bash
.\mvnw.cmd test jacoco:report

# Ver reporte en: target/site/jacoco/index.html
```

### 📝 **Interpretación de Resultados**

#### ✅ **Resultado Exitoso**
```
[INFO] Results:
[INFO] 
[INFO] Tests run: 57, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] BUILD SUCCESS
```

#### ❌ **Resultado con Errores**
```
[ERROR] Tests run: 57, Failures: 1, Errors: 0, Skipped: 0
[ERROR] 
[ERROR] BUILD FAILURE
```

Para más detalles sobre las pruebas, consulta: [INSTRUCCIONES_PRUEBAS.md](INSTRUCCIONES_PRUEBAS.md)

---

## 📚 **APIs Disponibles**

### 🌐 **URLs Principales**

| Servicio | URL Base | Descripción |
|----------|----------|-------------|
| **APIs** | http://localhost:8080/api | Endpoints principales |
| **Swagger UI** | http://localhost:8080/q/swagger-ui | Documentación interactiva |
| **OpenAPI** | http://localhost:8080/q/openapi | Especificación OpenAPI |
| **Health** | http://localhost:8080/q/health | Estado de la aplicación |

### 👤 **API de Personas** (`/api/personas`)

```bash
# Crear persona
curl -X POST http://localhost:8080/api/personas \
  -H "Content-Type: application/json" \
  -d '{
    "personaId": "12345678",
    "nombre": "Juan Pérez",
    "genero": "M",
    "edad": 30,
    "direccion": "Calle 123, Ciudad",
    "telefono": "555-1234"
  }'

# Obtener todas las personas
curl http://localhost:8080/api/personas

# Obtener persona por ID
curl http://localhost:8080/api/personas/12345678

# Actualizar persona
curl -X PUT http://localhost:8080/api/personas/12345678 \
  -H "Content-Type: application/json" \
  -d '{"nombre": "Juan Carlos Pérez", "edad": 31}'

# Eliminar persona
curl -X DELETE http://localhost:8080/api/personas/12345678
```

### 💳 **API de Clientes** (`/api/clientes`)

```bash
# Crear cliente (requiere persona existente)
curl -X POST http://localhost:8080/api/clientes \
  -H "Content-Type: application/json" \
  -d '{
    "personaId": "12345678",
    "contrasena": "password123",
    "estado": true
  }'

# Obtener todos los clientes
curl http://localhost:8080/api/clientes

# Obtener clientes activos
curl "http://localhost:8080/api/clientes?estado=true"

# Obtener cliente por ID
curl http://localhost:8080/api/clientes/1

# Actualizar cliente
curl -X PUT http://localhost:8080/api/clientes/1 \
  -H "Content-Type: application/json" \
  -d '{"contrasena": "newPassword123", "estado": true}'

# Eliminar cliente
curl -X DELETE http://localhost:8080/api/clientes/1
```

### 🏧 **API de Cuentas** (`/api/cuentas`)

```bash
# Crear cuenta
curl -X POST http://localhost:8080/api/cuentas \
  -H "Content-Type: application/json" \
  -d '{
    "numeroCuenta": "1001",
    "tipoCuenta": "AHORRO",
    "saldoInicial": 1000.00,
    "clienteId": 1
  }'

# Obtener todas las cuentas
curl http://localhost:8080/api/cuentas

# Obtener cuentas por cliente
curl "http://localhost:8080/api/cuentas?clienteId=1"

# Obtener cuenta por ID
curl http://localhost:8080/api/cuentas/1

# Obtener cuenta por número
curl http://localhost:8080/api/cuentas/numero/1001

# Actualizar cuenta
curl -X PUT http://localhost:8080/api/cuentas/1 \
  -H "Content-Type: application/json" \
  -d '{"tipoCuenta": "CORRIENTE", "saldoInicial": 1500.00}'

# Eliminar cuenta
curl -X DELETE http://localhost:8080/api/cuentas/1
```

### 💰 **API de Movimientos** (`/api/movimientos`)

```bash
# Crear movimiento (depósito)
curl -X POST http://localhost:8080/api/movimientos \
  -H "Content-Type: application/json" \
  -d '{
    "tipoMovimiento": "CREDITO",
    "valor": 500.00,
    "saldo": 1500.00,
    "cuentaId": 1
  }'

# Crear movimiento (retiro)
curl -X POST http://localhost:8080/api/movimientos \
  -H "Content-Type: application/json" \
  -d '{
    "tipoMovimiento": "DEBITO",
    "valor": -200.00,
    "saldo": 1300.00,
    "cuentaId": 1
  }'

# Obtener todos los movimientos
curl http://localhost:8080/api/movimientos

# Obtener movimientos por cuenta
curl "http://localhost:8080/api/movimientos?cuentaId=1"

# Obtener movimientos por rango de fechas
curl "http://localhost:8080/api/movimientos?fechaInicio=2024-01-01&fechaFin=2024-12-31"

# Obtener movimiento por ID
curl http://localhost:8080/api/movimientos/1

# Actualizar movimiento
curl -X PUT http://localhost:8080/api/movimientos/1 \
  -H "Content-Type: application/json" \
  -d '{
    "tipoMovimiento": "CREDITO",
    "valor": 600.00,
    "saldo": 1600.00
  }'

# Eliminar movimiento
curl -X DELETE http://localhost:8080/api/movimientos/1
```

### 📊 **API de Reportes** (`/reportes`)

```bash
# Estado de cuenta
curl "http://localhost:8080/reportes/estado_cuenta?clienteId=1&fechaInicio=2024-01-01&fechaFin=2024-12-31"
```

### 📋 **Colección de Postman**

El proyecto incluye una colección completa de Postman en `postman_colections/PryQuarkus.json` con:
- ✅ Todos los endpoints documentados
- ✅ Casos de prueba exitosos
- ✅ Casos de prueba de error
- ✅ Variables de entorno configuradas

---

## 🗄️ **Base de Datos**

### 📊 **Esquema de Base de Datos**

El sistema utiliza PostgreSQL con el esquema `arq_hex`:

```sql
-- 👤 Tabla Persona (entidad base)
CREATE TABLE arq_hex.persona (
    persona_id VARCHAR(10) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    genero VARCHAR(10),
    edad INTEGER,
    direccion VARCHAR(150),
    telefono VARCHAR(20)
);

-- 💳 Tabla Cliente (hereda de Persona)
CREATE TABLE arq_hex.cliente (
    cliente_id SERIAL PRIMARY KEY,
    persona_id VARCHAR(10) UNIQUE REFERENCES arq_hex.persona(persona_id),
    contrasena VARCHAR(50) NOT NULL,
    estado BOOLEAN DEFAULT TRUE
);

-- 🏧 Tabla Cuenta
CREATE TABLE arq_hex.cuenta (
    cuenta_id SERIAL PRIMARY KEY,
    numero_cuenta VARCHAR(20) UNIQUE NOT NULL,
    tipo_cuenta VARCHAR(20),
    saldo_inicial NUMERIC(10,2) DEFAULT 0.00,
    estado BOOLEAN DEFAULT TRUE,
    cliente_id INTEGER REFERENCES arq_hex.cliente(cliente_id)
);

-- 💰 Tabla Movimiento
CREATE TABLE arq_hex.movimiento (
    movimiento_id SERIAL PRIMARY KEY,
    fecha DATE DEFAULT CURRENT_DATE,
    tipo_movimiento VARCHAR(20) NOT NULL,
    valor NUMERIC(10,2) NOT NULL,
    saldo NUMERIC(10,2) NOT NULL,
    cuenta_id INTEGER REFERENCES arq_hex.cuenta(cuenta_id)
);
```

### 🔄 **Migraciones con Flyway**

Las migraciones se ejecutan automáticamente al iniciar la aplicación:

```bash
# Ver estado de migraciones
.\mvnw.cmd flyway:info

# Ejecutar migraciones manualmente
.\mvnw.cmd flyway:migrate

# Reparar historial de migraciones
.\mvnw.cmd flyway:repair

# Limpiar base de datos (¡CUIDADO!)
.\mvnw.cmd flyway:clean
```

### 🔍 **Verificar Base de Datos**

```bash
# Ejecutar script de verificación
psql -U postgres -d prueba -f verify_db.sql

# O desde Docker
docker-compose exec postgres psql -U postgres -d prueba -f /verify_db.sql
```

### 🛠️ **Administración con PgAdmin**

1. **Acceder a PgAdmin**: http://localhost:5050
2. **Credenciales**:
   - Email: `admin@example.com`
   - Password: `admin123`
3. **Conectar a la base de datos**:
   - Host: `postgres` (dentro de Docker) o `localhost`
   - Port: `5432`
   - Database: `prueba`
   - Username: `postgres`
   - Password: `postgres`

---

## 🔧 **Configuración Avanzada**

### ⚙️ **Variables de Entorno**

| Variable | Descripción | Valor por Defecto |
|----------|-------------|-------------------|
| `QUARKUS_HTTP_PORT` | Puerto de la aplicación | `8080` |
| `QUARKUS_DATASOURCE_JDBC_URL` | URL de la base de datos | `jdbc:postgresql://localhost:5432/prueba` |
| `QUARKUS_DATASOURCE_USERNAME` | Usuario de BD | `postgres` |
| `QUARKUS_DATASOURCE_PASSWORD` | Contraseña de BD | `postgres` |
| `QUARKUS_LOG_LEVEL` | Nivel de logs | `INFO` |

### 📝 **Perfiles de Configuración**

#### **Desarrollo** (`application.properties`)
```properties
# Puerto local
quarkus.http.port=8080

# Base de datos local
quarkus.datasource.jdbc.url=jdbc:postgresql://localhost:5432/prueba

# Logs detallados
quarkus.log.level=INFO
quarkus.log.category."com.resolutions".level=DEBUG
quarkus.hibernate-orm.log.sql=true
```

#### **Docker** (automático)
```properties
# Las variables se configuran automáticamente en docker-compose.yml
%docker.quarkus.datasource.jdbc.url=jdbc:postgresql://postgres:5432/prueba
%docker.quarkus.http.host=0.0.0.0
```

#### **Producción** (variables de entorno)
```bash
export QUARKUS_HTTP_PORT=8080
export QUARKUS_DATASOURCE_JDBC_URL=jdbc:postgresql://prod-db:5432/banking
export QUARKUS_LOG_LEVEL=WARN
```

### 🚀 **Modos de Ejecución**

#### **Desarrollo con Hot Reload**
```bash
.\mvnw.cmd quarkus:dev
# La aplicación se recarga automáticamente al cambiar código
```

#### **Producción JVM**
```bash
.\mvnw.cmd clean package
java -jar target/quarkus-app/quarkus-run.jar
```

#### **Compilación Nativa** (requiere GraalVM)
```bash
.\mvnw.cmd clean package -Dnative
./target/hexagonal-arquitecture-1.0.0-SNAPSHOT-runner
```

---

## 📊 **Monitoreo y Debugging**

### 🏥 **Health Checks**

```bash
# Estado general de la aplicación
curl http://localhost:8080/q/health
# Respuesta: {"status": "UP"}

# Estado de disponibilidad (readiness)
curl http://localhost:8080/q/health/ready
# Verifica si la app está lista para recibir tráfico

# Estado de vida (liveness)
curl http://localhost:8080/q/health/live
# Verifica si la app está funcionando
```

### 📈 **Métricas**

```bash
# Métricas de la aplicación
curl http://localhost:8080/q/metrics

# Métricas en formato Prometheus
curl -H "Accept: application/openmetrics-text" http://localhost:8080/q/metrics
```

### 📋 **Logs**

#### **Logs en Desarrollo**
```bash
# Los logs aparecen directamente en la consola
.\mvnw.cmd quarkus:dev
```

#### **Logs en Docker**
```bash
# Ver logs de todos los servicios
docker-compose logs

# Logs en tiempo real
docker-compose logs -f

# Logs de un servicio específico
docker-compose logs -f quarkus-app
docker-compose logs -f postgres

# Logs de las últimas 100 líneas
docker-compose logs --tail=100 quarkus-app
```

### 🐛 **Debugging**

#### **Debug en Desarrollo**
```bash
# Habilitar debug en puerto 5005
.\mvnw.cmd quarkus:dev -Ddebug=5005

# Configurar tu IDE para conectar al puerto 5005
```

#### **Debug en Docker**
```bash
# El docker-compose.yml ya incluye configuración de debug
docker-compose up -d
# Debug disponible en puerto 5005
```

#### **Comandos de Diagnóstico**
```bash
# Verificar conectividad de red
curl -v http://localhost:8080/q/health

# Verificar recursos del sistema
docker stats

# Inspeccionar contenedores
docker-compose ps
docker-compose exec quarkus-app env
```

---

## 🤝 **Contribuir al Proyecto**

### 🔄 **Workflow de Git**

#### **1. Fork y Clone**
```bash
# Fork el proyecto en GitHub, luego:
git clone https://github.com/TU_USUARIO/PryQuarkus.git
cd PryQuarkus
```

#### **2. Configurar Remotes**
```bash
# Agregar repositorio original como upstream
git remote add upstream https://github.com/cecyksw-ui/PryQuarkus.git

# Verificar remotes
git remote -v
```

#### **3. Crear Branch de Feature**
```bash
# Crear y cambiar a nueva branch
git checkout -b feature/nueva-funcionalidad

# O para bug fixes
git checkout -b fix/nombre-del-bug
```

#### **4. Desarrollo**
```bash
# Hacer cambios y commits
git add .
git commit -m "feat: agregar endpoint para transferencias"

# Sincronizar con upstream
git fetch upstream
git rebase upstream/main
```

#### **5. Push y Pull Request**
```bash
# Subir cambios
git push origin feature/nueva-funcionalidad

# Crear Pull Request desde GitHub
```

### 📝 **Convenciones de Commits**

Usamos [Conventional Commits](https://www.conventionalcommits.org/):

```bash
# Features nuevos
git commit -m "feat: agregar endpoint para transferencias bancarias"

# Bug fixes
git commit -m "fix: corregir validación de saldo insuficiente"

# Documentación
git commit -m "docs: actualizar README con ejemplos de API"

# Refactoring
git commit -m "refactor: simplificar lógica de cálculo de intereses"

# Tests
git commit -m "test: agregar pruebas para MovimientoUseCase"

# Configuración
git commit -m "ci: configurar GitHub Actions para despliegue"
```

### 🧪 **Antes de Hacer Push**

```bash
# 1. Ejecutar todas las pruebas
.\mvnw.cmd test

# 2. Verificar estilo de código
.\mvnw.cmd compile

# 3. Verificar que la aplicación inicia correctamente
.\mvnw.cmd quarkus:dev
```

### 📋 **Checklist para Pull Requests**

- [ ] ✅ Todas las pruebas pasan
- [ ] ✅ Código sigue las convenciones del proyecto
- [ ] ✅ Documentación actualizada si es necesario
- [ ] ✅ Commits siguen convención conventional commits
- [ ] ✅ Branch está actualizada con main
- [ ] ✅ PR tiene descripción clara del cambio

### 👥 **Roles del Proyecto**

| Rol | Responsabilidades |
|-----|------------------|
| **Maintainer** | Revisar PRs, gestionar releases |
| **Contributor** | Desarrollar features, arreglar bugs |
| **Reviewer** | Revisar código, sugerir mejoras |

---

## ❓ **Troubleshooting**

### 🚨 **Problemas Comunes**

#### **Error: Puerto 8080 ocupado**
```bash
# Ver qué proceso usa el puerto
netstat -ano | findstr :8080

# Cambiar puerto en application.properties
quarkus.http.port=8082

# O usar variable de entorno
set QUARKUS_HTTP_PORT=8082
.\mvnw.cmd quarkus:dev
```

#### **Error: No se puede conectar a PostgreSQL**
```bash
# Verificar que PostgreSQL está corriendo
docker-compose ps postgres

# Ver logs de PostgreSQL
docker-compose logs postgres

# Reiniciar servicios
docker-compose restart postgres
docker-compose restart quarkus-app

# Verificar conectividad manual
psql -h localhost -p 5432 -U postgres -d prueba
```

#### **Error: OutOfMemoryError**
```bash
# Aumentar memoria para Maven
set MAVEN_OPTS=-Xmx2g
.\mvnw.cmd quarkus:dev

# Para Docker
docker-compose up -d
# El docker-compose.yml ya tiene límites de memoria configurados
```

#### **Error: Docker no inicia**
```bash
# Verificar Docker está corriendo
docker version

# Verificar Docker Compose
docker-compose version

# Ejecutar script de verificación
.\verify-docker.ps1

# Limpiar contenedores y volúmenes
docker-compose down -v
docker system prune -f
```

### 🔄 **Comandos de Reset**

#### **Reset Completo del Proyecto**
```bash
# Limpiar todo y empezar de nuevo
docker-compose down -v
.\mvnw.cmd clean
docker system prune -f
docker-compose up --build -d
```

#### **Reset Solo Base de Datos**
```bash
# Limpiar y recrear esquema
.\mvnw.cmd flyway:clean flyway:migrate

# O con Docker
docker-compose down
docker volume rm pryquarkus_postgres_data
docker-compose up -d
```

#### **Reset Solo Aplicación**
```bash
# Recompilar aplicación
.\mvnw.cmd clean package
docker-compose up --build quarkus-app
```

### 📞 **Obtener Ayuda**

Si tienes problemas:

1. **📚 Consulta la documentación**: Revisa este README completo
2. **🧪 Ejecuta las pruebas**: `.\mvnw.cmd test`
3. **🔍 Verifica logs**: `docker-compose logs -f`
4. **🏥 Check health**: `curl http://localhost:8080/q/health`
5. **🐛 Issues de GitHub**: [Reportar problema](https://github.com/cecyksw-ui/PryQuarkus/issues)

### 📊 **Comandos de Diagnóstico**

```bash
# Información del sistema
.\mvnw.cmd --version
java -version
docker --version
docker-compose --version

# Estado de la aplicación
curl http://localhost:8080/q/health
curl http://localhost:8080/q/health/ready
curl http://localhost:8080/q/health/live

# Estado de Docker
docker-compose ps
docker stats --no-stream

# Logs detallados
docker-compose logs --tail=50 quarkus-app
```

---

## 📞 **Contacto y Soporte**

### 🔗 **Enlaces Importantes**

| Recurso | URL |
|---------|-----|
| **📂 Repositorio** | https://github.com/cecyksw-ui/PryQuarkus |
| **🐛 Issues** | https://github.com/cecyksw-ui/PryQuarkus/issues |
| **📚 Wiki** | https://github.com/cecyksw-ui/PryQuarkus/wiki |
| **🔧 Discussions** | https://github.com/cecyksw-ui/PryQuarkus/discussions |

### 👥 **Equipo de Desarrollo**

- **👤 Maintainer**: Cecilia Yánez (@cecyksw-ui)
- **📧 Email**: dev@resolutions.com
- **🌐 Organización**: Resolutions

### 📋 **Reportar Issues**

Al reportar problemas, incluye:

1. **🐛 Descripción del problema**
2. **📱 Versión de Java**: `java -version`
3. **🐳 Versión de Docker**: `docker --version`
4. **💻 Sistema operativo**
5. **📋 Logs relevantes**
6. **🔄 Pasos para reproducir**

---

## � **Créditos y Referencias**

### 📚 **Inspiración del Proyecto**

Este proyecto fue desarrollado tomando como base y referencia el excelente trabajo de **Ronald Sanchez**, específicamente su implementación de arquitectura hexagonal con Quarkus:

- **📋 Proyecto Original**: [hexagonal-arquitecture](https://github.com/ronaldsanchez/hexagonal-arquitecture)
- **👨‍💻 Autor**: Ronald Sanchez (@ronaldsanchez)
- **🏗️ Concepto Base**: Implementación de Ports & Adapters con Quarkus y Java

### 🎯 **¿Qué Tomamos del Proyecto Original?**

- **🏛️ Estructura Arquitectónica**: La organización de packages siguiendo el patrón hexagonal
- **📁 Distribución de Capas**: Separación clara entre `adapters`, `application` y `model`
- **⚙️ Configuración Base**: Setup inicial de Quarkus con las dependencias fundamentales
- **🔧 Patrones de Diseño**: Implementación de puertos y adaptadores

### 🚀 **Nuestras Contribuciones y Extensiones**

Partiendo de la base sólida del proyecto de Ronald, hemos desarrollado y expandido:

- **🏦 Dominio Bancario Completo**: Sistema integral de gestión bancaria
- **💳 Modelos de Negocio**: Personas, Clientes, Cuentas, Movimientos y Reportes
- **🧪 Suite de Testing**: 57 pruebas unitarias y de integración
- **🐳 Containerización**: Docker Compose completo con PostgreSQL y PgAdmin
- **📊 Migraciones de BD**: Sistema completo con Flyway
- **📖 Documentación Extensa**: README detallado y APIs documentadas
- **🔍 Monitoreo**: Health checks y métricas
- **⚡ Performance**: Optimizaciones y mejoras de rendimiento

### � **Herramientas de Desarrollo Asistidas por IA**

En el desarrollo de este proyecto se utilizaron herramientas de **Inteligencia Artificial** como asistentes de desarrollo, específicamente:

- **🧠 GitHub Copilot**: Para asistencia en la generación de código, documentación y resolución de problemas
- **💡 Análisis de Código**: Sugerencias de mejores prácticas y optimizaciones
- **📝 Documentación**: Colaboración en la redacción de documentación técnica y README
- **🧪 Testing**: Asistencia en la creación de casos de prueba y scenarios de testing
- **🔍 Code Review**: Análisis y sugerencias de mejoras en la calidad del código

> ⚠️ **Transparencia**: El uso de IA fue complementario al conocimiento técnico del desarrollador, manteniendo siempre el control y revisión humana de todas las implementaciones y decisiones arquitectónicas.

### �🤝 **Agradecimientos**

Queremos expresar nuestro sincero agradecimiento a:

**Ronald Sanchez** por su proyecto base:
- 🎓 **Educación**: Proporcionar un ejemplo claro de arquitectura hexagonal
- 🛠️ **Código Base**: Estructura inicial que sirvió como fundamento sólido
- 💡 **Inspiración**: Demostrar las mejores prácticas con Quarkus
- 🌟 **Comunidad**: Contribuir al ecosistema open source de Java/Quarkus

**Comunidad Open Source y Herramientas IA**:
- 🤖 **GitHub Copilot**: Por asistir en el proceso de desarrollo y documentación
- 👥 **Comunidad Java/Quarkus**: Por las librerías, frameworks y conocimiento compartido
- 📚 **Documentación y Tutoriales**: Recursos educativos que facilitaron el aprendizaje

> 💬 **Nota**: Este proyecto representa una evolución y expansión del concepto original, desarrollado para fines educativos y demostrativos en el contexto de sistemas bancarios, con el apoyo de herramientas modernas de desarrollo.

---

## �📄 **Licencia**

Este proyecto está licenciado bajo **Apache License 2.0**.

```
Copyright 2024 Resolutions

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

---

## 🎯 **¿Qué Aprender de Este Proyecto?**

### 🏗️ **Arquitectura y Diseño**
- ✅ **Arquitectura Hexagonal**: Separación clara de responsabilidades
- ✅ **Domain-Driven Design**: Modelado del dominio bancario
- ✅ **SOLID Principles**: Código mantenible y extensible
- ✅ **Clean Code**: Código limpio y legible

### 💻 **Tecnologías Modernas**
- ✅ **Java 21**: Features más recientes del lenguaje
- ✅ **Quarkus**: Framework supersónico para microservicios
- ✅ **Reactive Programming**: APIs no bloqueantes
- ✅ **JPA/Hibernate**: ORM moderno

### 🧪 **Testing y Calidad**
- ✅ **Unit Testing**: 57 pruebas unitarias
- ✅ **Test-Driven Development**: Desarrollo dirigido por pruebas
- ✅ **Mocking**: Uso de Mockito para aislamiento
- ✅ **Integration Testing**: Pruebas de integración

### 🐳 **DevOps y Deployment**
- ✅ **Docker**: Containerización completa
- ✅ **Docker Compose**: Orquestación de servicios
- ✅ **Database Migrations**: Flyway para evolución de BD
- ✅ **Health Checks**: Monitoreo y observabilidad

### 🌐 **APIs y Documentación**
- ✅ **REST APIs**: Endpoints bien diseñados
- ✅ **OpenAPI/Swagger**: Documentación automática
- ✅ **API Versioning**: Evolución de APIs
- ✅ **Error Handling**: Manejo robusto de errores

---

> 💡 **¡Tip!** Este proyecto es ideal para aprender arquitectura hexagonal, Quarkus y mejores prácticas de desarrollo Java moderno. Úsalo como base para tus propios proyectos o como referencia de estudio.

---

**🎉 ¡Feliz codificación!** 🚀