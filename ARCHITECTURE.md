# Arquitectura Hexagonal - Diagrama y Explicación

## Diagrama de la Arquitectura

```
┌─────────────────────────────────────────────────────────────────────┐
│                         CAPA DE INFRAESTRUCTURA                      │
│                         (Adaptadores de Entrada)                     │
│                                                                       │
│  ┌────────────────────────────────────────────────────────────────┐ │
│  │                    ProductResource (REST)                      │ │
│  │                   /api/products endpoints                      │ │
│  └─────────────────────────┬──────────────────────────────────────┘ │
│                            │                                          │
└────────────────────────────┼──────────────────────────────────────────┘
                             │ Usa
                             ▼
┌─────────────────────────────────────────────────────────────────────┐
│                        CAPA DE APLICACIÓN                            │
│                     (Lógica de Orquestación)                         │
│                                                                       │
│  ┌────────────────────────────────────────────────────────────────┐ │
│  │                      ProductService                            │ │
│  │                                                                │ │
│  │  Implementa:                                                   │ │
│  │  - CreateProductUseCase                                        │ │
│  │  - GetProductUseCase                                           │ │
│  │  - UpdateProductUseCase                                        │ │
│  │  - DeleteProductUseCase                                        │ │
│  └─────────────┬────────────────────────────────┬─────────────────┘ │
│                │                                │                    │
└────────────────┼────────────────────────────────┼────────────────────┘
                 │ Implementa                     │ Usa
                 ▼                                ▼
┌─────────────────────────────────────────────────────────────────────┐
│                          CAPA DE DOMINIO                             │
│                        (Núcleo de Negocio)                           │
│                                                                       │
│  ┌──────────────────────┐      ┌──────────────────────────────────┐ │
│  │    Puertos (IN)      │      │      Puertos (OUT)               │ │
│  │                      │      │                                  │ │
│  │  CreateProduct       │      │    ProductRepository             │ │
│  │  GetProduct          │      │                                  │ │
│  │  UpdateProduct       │      │    - save()                      │ │
│  │  DeleteProduct       │      │    - findById()                  │ │
│  │                      │      │    - findAll()                   │ │
│  └──────────────────────┘      │    - update()                    │ │
│                                │    - deleteById()                │ │
│  ┌──────────────────────┐      └──────────────────────────────────┘ │
│  │   Modelo de Dominio  │                     ▲                      │
│  │                      │                     │ Implementa           │
│  │      Product         │                     │                      │
│  │                      │                     │                      │
│  │  - id                │                     │                      │
│  │  - name              │                     │                      │
│  │  - description       │                     │                      │
│  │  - price             │                     │                      │
│  │  - stock             │                     │                      │
│  └──────────────────────┘                     │                      │
│                                               │                      │
└───────────────────────────────────────────────┼──────────────────────┘
                                                │
┌───────────────────────────────────────────────┼──────────────────────┐
│                       CAPA DE INFRAESTRUCTURA                        │
│                       (Adaptadores de Salida)                        │
│                                               │                      │
│  ┌────────────────────────────────────────────┴───────────────────┐ │
│  │              ProductRepositoryAdapter                          │ │
│  │                                                                │ │
│  │  Implementa: ProductRepository (puerto de salida)             │ │
│  │  Usa: ProductEntity (JPA/Hibernate)                           │ │
│  └────────────┬───────────────────────────────────────────────────┘ │
│               │ Usa                                                  │
│               ▼                                                      │
│  ┌─────────────────────────────────────────────────────────────────┐│
│  │              ProductEntity (JPA Entity)                         ││
│  │              Tabla: products en PostgreSQL                      ││
│  │                                                                 ││
│  │  + ProductMapper (convierte Entity ↔ Domain)                   ││
│  └─────────────────────────────────────────────────────────────────┘│
│                              │                                       │
└──────────────────────────────┼───────────────────────────────────────┘
                               │
                               ▼
                    ┌──────────────────────┐
                    │   PostgreSQL         │
                    │   Database           │
                    │   (products_db)      │
                    └──────────────────────┘
```

## Flujo de una Petición

### Ejemplo: Crear un Producto (POST /api/products)

1. **Entrada**: Cliente HTTP → `ProductResource` (Adaptador REST)
   ```
   POST /api/products
   { "name": "Laptop", "price": 1200.00, ... }
   ```

2. **Invocación del Caso de Uso**: `ProductResource` → `CreateProductUseCase` (Puerto IN)
   ```java
   createProductUseCase.createProduct(product);
   ```

3. **Ejecución del Servicio**: `ProductService` ejecuta la lógica
   ```java
   public Product createProduct(Product product) {
       return productRepository.save(product);
   }
   ```

4. **Llamada al Puerto de Salida**: `ProductService` → `ProductRepository` (Puerto OUT)
   ```java
   productRepository.save(product);
   ```

5. **Adaptador de Persistencia**: `ProductRepositoryAdapter` → `ProductEntity` (JPA)
   ```java
   ProductEntity entity = ProductMapper.toEntity(product);
   entity.persist();
   return ProductMapper.toDomain(entity);
   ```

6. **Base de Datos**: Panache/Hibernate → PostgreSQL
   ```sql
   INSERT INTO products (name, price, ...) VALUES (...);
   ```

7. **Respuesta**: Retorna por el mismo camino hasta el cliente HTTP

## Dependencias entre Capas

```
┌─────────────────────────────────────────────┐
│         DIRECCIÓN DE DEPENDENCIAS           │
└─────────────────────────────────────────────┘

  Infraestructura (IN)  ──────┐
                              │
                              ▼
                        Aplicación
                              │
                              ▼
  Infraestructura (OUT) ──▶ Dominio ◀── (el centro)
```

### Principio de Inversión de Dependencias

- El **Dominio** NO depende de nada (es puro Java)
- La **Aplicación** depende solo del Dominio
- La **Infraestructura** depende del Dominio y de la Aplicación
- Las dependencias apuntan hacia adentro (hacia el dominio)

## Beneficios Prácticos

### 1. Cambiar la Base de Datos
Para cambiar de PostgreSQL a MongoDB:
- ✅ Solo modificar `ProductRepositoryAdapter`
- ✅ Crear `ProductDocument` en lugar de `ProductEntity`
- ❌ NO tocar el dominio ni la aplicación ni el REST API

### 2. Agregar un Nuevo Adaptador de Entrada (GraphQL)
- ✅ Crear `ProductGraphQLResource`
- ✅ Inyectar los mismos casos de uso
- ❌ NO modificar nada del dominio ni servicios

### 3. Tests Unitarios
```java
// Test del dominio SIN base de datos
ProductRepository mockRepo = mock(ProductRepository.class);
ProductService service = new ProductService(mockRepo);

when(mockRepo.save(any())).thenReturn(product);
Product result = service.createProduct(product);

// El dominio se testea aisladamente
```

### 4. Desarrollo Paralelo
- Equipo A: Implementa el dominio y los puertos
- Equipo B: Implementa adaptadores REST
- Equipo C: Implementa adaptadores de persistencia

Todos trabajan simultáneamente con contratos claros (interfaces).

## Archivos Clave

| Archivo | Capa | Responsabilidad |
|---------|------|-----------------|
| `Product.java` | Dominio | Modelo de negocio puro |
| `*UseCase.java` | Dominio | Contratos de entrada |
| `ProductRepository.java` | Dominio | Contrato de salida |
| `ProductService.java` | Aplicación | Orquestación e implementación de casos de uso |
| `ProductResource.java` | Infraestructura (IN) | Adaptador REST API |
| `ProductRepositoryAdapter.java` | Infraestructura (OUT) | Adaptador JPA/Hibernate |
| `ProductEntity.java` | Infraestructura (OUT) | Modelo de persistencia |
| `ProductMapper.java` | Infraestructura (OUT) | Traductor Domain ↔ JPA |

## Antipatrones Evitados

❌ **Entidades de Dominio con anotaciones JPA**
```java
// MAL: Dominio acoplado a JPA
@Entity
public class Product {
    @Id @GeneratedValue
    private Long id;
}
```

✅ **Separación de modelos**
```java
// BIEN: Dominio puro + Entity separada
public class Product { }  // Sin anotaciones
@Entity
public class ProductEntity { }  // Solo en infraestructura
```

❌ **Servicio dependiendo de implementaciones concretas**
```java
// MAL
class ProductService {
    private JpaProductRepository repo; // Acoplado a JPA
}
```

✅ **Servicio dependiendo de interfaces (puertos)**
```java
// BIEN
class ProductService {
    private ProductRepository repo; // Interface del dominio
}
```
