# Guía Rápida de Inicio

Esta guía te ayudará a poner en marcha el proyecto en menos de 5 minutos.

## Paso 1: Requisitos Previos

Asegúrate de tener instalado:
- Java 17 o superior
- Maven (incluido en el proyecto via mvnw)
- Docker (opcional, pero recomendado para PostgreSQL)

Verifica tu instalación de Java:
```bash
java -version
```

## Paso 2: Iniciar PostgreSQL

### Opción A: Usando Docker (Recomendado)
```bash
docker-compose up -d
```

Espera unos segundos hasta que PostgreSQL esté listo. Verifica el estado:
```bash
docker-compose ps
```

### Opción B: PostgreSQL Local
Si ya tienes PostgreSQL instalado localmente:
```bash
createdb products_db
```

## Paso 3: Ejecutar la Aplicación

### Modo Desarrollo (Recomendado para pruebas)
```bash
./mvnw quarkus:dev
```

La aplicación estará disponible en:
- API REST: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui
- Dev UI: http://localhost:8080/q/dev

### Modo Producción
```bash
./mvnw clean package
java -jar target/quarkus-app/quarkus-run.jar
```

## Paso 4: Probar la API

### Usando curl

Listar todos los productos:
```bash
curl http://localhost:8080/api/products
```

Obtener un producto específico:
```bash
curl http://localhost:8080/api/products/1
```

Crear un nuevo producto:
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Nuevo Producto",
    "description": "Descripción del producto",
    "price": 199.99,
    "stock": 100
  }'
```

### Usando Swagger UI

Abre http://localhost:8080/swagger-ui en tu navegador y prueba todas las operaciones de forma interactiva.

## Paso 5: Ejecutar Tests

```bash
./mvnw test
```

Los tests usan una base de datos H2 en memoria, por lo que no necesitas PostgreSQL para ejecutarlos.

## Detener la Aplicación

### Si usas quarkus:dev
Presiona `q` en la terminal donde está corriendo.

### Si usas docker-compose
```bash
docker-compose down
```

## Solución de Problemas

### Error: Puerto 8080 en uso
Cambia el puerto en `src/main/resources/application.properties`:
```properties
quarkus.http.port=8081
```

### Error: No se puede conectar a PostgreSQL
Verifica que PostgreSQL esté corriendo:
```bash
docker-compose ps
# o
psql -U postgres -l
```

### Error: JAVA_HOME no configurado
```bash
export JAVA_HOME=/path/to/java
```

## Próximos Pasos

- Lee el [README.md](README.md) completo para entender la arquitectura
- Revisa [ARCHITECTURE.md](ARCHITECTURE.md) para detalles sobre el patrón hexagonal
- Explora el código fuente para aprender sobre la implementación

## Datos de Ejemplo

La aplicación viene con 5 productos de ejemplo:
1. Laptop - $1,200.00
2. Mouse - $25.50
3. Keyboard - $85.00
4. Monitor - $350.00
5. Headphones - $150.00

Estos se cargan automáticamente desde `src/main/resources/import.sql`.
