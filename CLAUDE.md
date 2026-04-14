# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```bash
# Run the application
./mvnw spring-boot:run

# Build (skip tests)
./mvnw clean package -DskipTests

# Run all tests
./mvnw test

# Run a single test class
./mvnw test -Dtest=ClassName

# Clean build artifacts
./mvnw clean
```

API documentation is available at `/swagger-ui.html` when the app is running.

## Architecture

**Stack:** Spring Boot 4.0.5, Java 21, MySQL, Spring Security + JWT, Spring Data JPA, Lombok

**Database:** MySQL on `localhost:3306`, database `talleros_db`. Hibernate DDL mode is `update` (auto-migrates schema on startup). Credentials are in `src/main/resources/application.properties`.

### Package Structure

All business modules live under `com.talleros.modules.*`, each following the same 4-layer pattern:

```
modules/
  auth/         — User entity, roles (ADMIN, MECANICO, RECEPCIONISTA), JWT auth
  cliente/      — Client management
  vehiculo/     — Vehicle management
  orden/        — Work orders (primary domain entity)
  repuesto/     — Parts/inventory
  turno/        — Appointments/shifts
  tenant/       — Tenant entity (core to all other modules)
config/         — SecurityConfig (JWT + Spring Security)
exception/      — GlobalExceptionHandler (scaffolded, not yet implemented)
multitenancy/   — Tenant isolation infrastructure
```

### Layered Pattern (per module)

Each module has: `Controller → Service → Repository → Model` + a `dto/` subpackage.

- **Controller**: `@RestController`, accepts `tenantId` via `@RequestParam`, returns `ResponseEntity`, validates with `@Valid`.
- **Service**: Business logic, maps entities ↔ DTOs using the builder pattern, throws `RuntimeException` for errors (custom exceptions not yet implemented).
- **Repository**: Extends `JpaRepository`, uses tenant-scoped query methods (e.g., `findAllByTenantId`, `existsByXAndTenantId`).
- **Model**: JPA entities with Lombok (`@Builder`, `@Getter`, `@Setter`), `@PrePersist`/`@PreUpdate` for audit timestamps.

### Multi-tenancy

Every entity has a `@ManyToOne` to `Tenant`. Tenant isolation is enforced manually at the repository/service layer — all queries must include `tenantId`. There is no middleware-level tenant filtering.

### Domain Relationships

```
Tenant (1) → (*) all entities
Cliente (1) → (*) Vehiculo
Vehiculo (1) → (*) OrdenDeTrabajo
OrdenDeTrabajo (1) → (*) Tarea
OrdenDeTrabajo (1) → (*) Repuesto (used in order)
```

### Known Scaffolding / Incomplete Areas

- `exception/GlobalExceptionHandler` exists but is empty — services currently throw raw `RuntimeException`.
- `SecurityConfig` is permissive (all requests allowed); JWT infrastructure is wired but auth filters are not enforced yet.
- JWT secret (`talleros_super_secret_key_2024_cambiame`) and DB credentials are hardcoded in properties — not suitable for production.

## Working Rules

- **Leer antes de escribir**: antes de modificar codigo, leer los archivos relevantes y entender la arquitectura. Si falta contexto, preguntar.
- **Respuestas cortas**: 1-3 oraciones. Sin preambulos ni resumen final. No narrar lo que se esta haciendo.
- **Editar, no reescribir**: usar Edit para cambios parciales. Write solo si el cambio supera el 80% del archivo.
- **No releer**: si un archivo ya fue leido en la conversacion, no volver a leerlo salvo que haya cambiado.
- **Validar antes de declarar hecho**: compilar o correr tests despues de cada cambio. No decir "listo" sin evidencia.
- **Sin halagos**: no usar frases como "Excelente pregunta" o "Gran idea". Ir directo al trabajo.
- **Minimo viable**: implementar solo lo que resuelve el problema. Sin abstracciones, helpers ni features no pedidos.
- **No debatir**: si el usuario indica como hacerlo, hacerlo asi. Mencionar un concern en 1 oracion como maximo y proceder.
- **Leer solo lo necesario**: usar `offset`/`limit` en Read. Si la ruta es conocida, usar Read directo sin Glob ni Grep previo.
- **No anticipar**: no describir el plan antes de ejecutarlo. El usuario ve los tool calls.
- **Paralelizar**: leer archivos independientes en un solo mensaje, no uno por uno.
- **No duplicar en respuesta**: si se edito un archivo, no copiar el resultado en texto. El usuario lo ve en el diff.
- **Agent como ultimo recurso**: usar Grep/Glob/Read directo para busquedas especificas. Agent solo para tareas amplias o complejas.
