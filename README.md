# PryQuarkus - Arquitectura Hexagonal

Proyecto de prueba usando Quarkus, Maven, PostgreSQL para demostrar la **Arquitectura Hexagonal** (también conocida como Ports and Adapters).

## 📋 Descripción

Este es un mini-proyecto educativo que implementa un sistema CRUD de productos utilizando:
- **Quarkus**: Framework Java reactivo y cloud-native
- **Maven**: Gestión de dependencias y construcción
- **PostgreSQL**: Base de datos relacional
- **Arquitectura Hexagonal**: Patrón de diseño que separa la lógica de negocio de las preocupaciones técnicas

## 🏗️ Arquitectura Hexagonal

La arquitectura hexagonal divide la aplicación en tres capas principales:

### 1. **Dominio (Domain)** - El Núcleo
Contiene la lógica de negocio pura, independiente de cualquier framework o tecnología externa.

```
domain/
├── model/              # Entidades de dominio (Product)
└── port/
    ├── in/            # Puertos de entrada (Use Cases)
    │   ├── CreateProductUseCase
    │   ├── GetProductUseCase
    │   ├── UpdateProductUseCase
    │   └── DeleteProductUseCase
    └── out/           # Puertos de salida (Interfaces de repositorio)
        └── ProductRepository
```

**Características:**
- Sin dependencias de frameworks
- Modelos de dominio puros (POJOs)
- Define QUÉ puede hacer la aplicación (casos de uso)
- Define QUÉ necesita de sistemas externos (interfaces)

### 2. **Aplicación (Application)** - Orquestación
Implementa los casos de uso del dominio, coordinando las operaciones.

```
application/
└── service/
    └── ProductService  # Implementa todos los casos de uso
```

**Características:**
- Implementa las interfaces de los puertos de entrada
- Orquesta las operaciones del dominio
- Usa los puertos de salida para acceder a recursos externos

### 3. **Infraestructura (Infrastructure)** - Adaptadores
Contiene los adaptadores que conectan la aplicación con el mundo exterior.

```
infrastructure/
└── adapter/
    ├── in/            # Adaptadores de entrada (drivers)
    │   └── rest/
    │       └── ProductResource  # API REST
    └── out/           # Adaptadores de salida (driven)
        └── persistence/
            ├── ProductEntity    # Entidad JPA
            ├── ProductMapper    # Convertidor Domain ↔ Entity
            └── ProductRepositoryAdapter  # Implementación del repositorio
```

**Características:**
- **Adaptadores de entrada (IN)**: Exponen la funcionalidad (REST API, CLI, etc.)
- **Adaptadores de salida (OUT)**: Implementan los puertos de salida (Base de datos, APIs externas, etc.)
- Pueden ser reemplazados sin afectar el dominio

## 🎯 Beneficios de la Arquitectura Hexagonal

1. **Independencia de Frameworks**: El dominio no depende de Quarkus, JPA, etc.
2. **Testabilidad**: Fácil crear tests unitarios del dominio sin infraestructura
3. **Flexibilidad**: Cambiar PostgreSQL por MongoDB solo requiere un nuevo adaptador
4. **Mantenibilidad**: Separación clara de responsabilidades
5. **Desarrollo Paralelo**: Equipos pueden trabajar en diferentes capas simultáneamente

## 🚀 Comenzar

### Prerrequisitos

- Java 17 o superior
- Maven 3.8+
- PostgreSQL 12+
- Docker (opcional, para ejecutar PostgreSQL en contenedor)

### Configurar PostgreSQL

#### Opción 1: PostgreSQL local
```bash
# Crear base de datos
createdb products_db

# O usando psql
psql -U postgres
CREATE DATABASE products_db;
```

#### Opción 2: PostgreSQL con Docker
```bash
docker run --name postgres-quarkus \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -e POSTGRES_DB=products_db \
  -p 5432:5432 \
  -d postgres:15
```

### Ejecutar la aplicación

#### Modo desarrollo (con hot reload)
```bash
./mvnw quarkus:dev
```

#### Compilar y ejecutar
```bash
./mvnw clean package
java -jar target/quarkus-app/quarkus-run.jar
```

## 📚 API Endpoints

La aplicación expone los siguientes endpoints REST:

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/products` | Obtener todos los productos |
| GET | `/api/products/{id}` | Obtener un producto por ID |
| POST | `/api/products` | Crear un nuevo producto |
| PUT | `/api/products/{id}` | Actualizar un producto |
| DELETE | `/api/products/{id}` | Eliminar un producto |

### Documentación OpenAPI/Swagger

Una vez iniciada la aplicación, accede a:
- Swagger UI: http://localhost:8080/swagger-ui
- OpenAPI Spec: http://localhost:8080/openapi

### Ejemplos de uso

#### Crear un producto
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Tablet",
    "description": "10 inch tablet",
    "price": 299.99,
    "stock": 40
  }'
```

#### Obtener todos los productos
```bash
curl http://localhost:8080/api/products
```

#### Obtener un producto específico
```bash
curl http://localhost:8080/api/products/1
```

#### Actualizar un producto
```bash
curl -X PUT http://localhost:8080/api/products/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Laptop Pro",
    "description": "High performance laptop upgraded",
    "price": 1500.00,
    "stock": 8
  }'
```

#### Eliminar un producto
```bash
curl -X DELETE http://localhost:8080/api/products/1
```

## 🧪 Tests

Ejecutar todos los tests:
```bash
./mvnw test
```

Los tests incluyen:
- Tests de integración de la API REST
- Validación de operaciones CRUD
- Manejo de casos de error (404, etc.)

## 📦 Estructura del Proyecto

```
src/
├── main/
│   ├── java/com/cecyksw/
│   │   ├── domain/                    # Capa de Dominio
│   │   │   ├── model/
│   │   │   │   └── Product.java       # Entidad de dominio
│   │   │   └── port/
│   │   │       ├── in/                # Casos de uso (interfaces)
│   │   │       └── out/               # Contratos de salida (interfaces)
│   │   ├── application/               # Capa de Aplicación
│   │   │   └── service/
│   │   │       └── ProductService.java # Implementación de casos de uso
│   │   └── infrastructure/            # Capa de Infraestructura
│   │       └── adapter/
│   │           ├── in/
│   │           │   └── rest/          # Adaptador REST (entrada)
│   │           └── out/
│   │               └── persistence/   # Adaptador de persistencia (salida)
│   └── resources/
│       ├── application.properties     # Configuración de la aplicación
│       └── import.sql                 # Datos iniciales
└── test/
    └── java/com/cecyksw/
        └── infrastructure/
            └── adapter/
                └── in/
                    └── rest/
                        └── ProductResourceTest.java  # Tests de integración
```

## 🔧 Configuración

El archivo `application.properties` contiene:
- Configuración de PostgreSQL
- Configuración de Hibernate (DDL auto)
- Configuración de OpenAPI/Swagger
- Puerto HTTP (8080)

## 🎓 Conceptos Clave

### Puertos (Ports)
Interfaces que definen puntos de entrada y salida de la aplicación:
- **Puertos de Entrada (IN)**: Definen casos de uso (qué puede hacer la app)
- **Puertos de Salida (OUT)**: Definen qué necesita la app del exterior

### Adaptadores (Adapters)
Implementaciones concretas que conectan los puertos con tecnologías específicas:
- **Adaptadores de Entrada**: REST API, CLI, GraphQL, etc.
- **Adaptadores de Salida**: JPA/Hibernate, MongoDB, APIs REST externas, etc.

### Inversión de Dependencias
El dominio no depende de la infraestructura; la infraestructura depende del dominio.
Esto permite:
- Cambiar tecnologías sin afectar la lógica de negocio
- Testear el dominio sin necesidad de base de datos real

## 📝 Notas

- El proyecto usa Panache para simplificar JPA
- La base de datos se recrea en cada inicio (drop-and-create) para desarrollo
- Los datos de ejemplo se cargan desde `import.sql`

## 🤝 Contribuciones

Este es un proyecto educativo. Siéntete libre de:
- Agregar nuevos adaptadores (por ejemplo, GraphQL)
- Implementar nuevos casos de uso
- Mejorar los tests
- Agregar validaciones de dominio

## 📖 Referencias

- [Arquitectura Hexagonal (Alistair Cockburn)](https://alistair.cockburn.us/hexagonal-architecture/)
- [Quarkus Documentation](https://quarkus.io)
- [Ports and Adapters Pattern](https://netflixtechblog.com/ready-for-changes-with-hexagonal-architecture-b315ec967749)
