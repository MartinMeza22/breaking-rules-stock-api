# Breaking Rules Stock - API

API REST desarrollada con Spring Boot para la gestión básica de stock de productos.  
Proyecto orientado a practicar buenas prácticas de backend con arquitectura en capas y validaciones de negocio.

---

## Tecnologías
- Java 21
- Spring Boot 3
- Spring Web
- Spring Data JPA
- PostgreSQL
- Maven
- Lombok
- OpenAPI / Swagger
- JUnit 5 + Mockito

---

## Funcionalidades
- CRUD completo de productos
- Persistencia con JPA
- Validaciones de negocio en capa Service
- Uso de DTO para desacoplar entidad
- Manejo de precios con BigDecimal
- Filtro de productos por nombre
- Alerta de stock bajo configurable
- Exportación de productos a CSV
- Documentación interactiva con Swagger
- Logging para trazabilidad y debugging

- Arquitectura:
  - Controller
  - Service
  - Repository
  - DTO

---

## Conceptos aplicados
- Inyección de dependencias
- Separación de responsabilidades
- API RESTful
- JPA / Hibernate
- Manejo de entidades JPA
- Validaciones de negocio
- Testing unitario
- Documentación de API con OpenAPI
- Migración de base en memoria a persistente

---

## Base de Datos

El proyecto utiliza PostgreSQL como base de datos principal.

Configuración por defecto:

spring.datasource.url=jdbc:postgresql://localhost:5432/breaking_rules_db

spring.datasource.username=breaking_rules

spring.datasource.password=1234

La creación de tablas se realiza automáticamente mediante:

- spring.jpa.hibernate.ddl-auto=update

---

## ▶️ ¿Cómo ejecutar el proyecto?

```bash
git clone https://github.com/MartinMeza22/breaking-rules-stock.git
cd breaking-rules-stock
mvn spring-boot:run
```
---

Configurar PostgreSQL

```bash
CREATE DATABASE breaking_rules_stock;
```

Editar application.properties:

```bash
spring.datasource.url=jdbc:postgresql://localhost:5432/breaking_rules_stock
spring.datasource.username=tu_usuario
spring.datasource.password=tu_password
```
---

Ejecutar

```bash
mvn spring-boot:run
```

Swagger:
http://localhost:8080/swagger-ui.html

Consola H2:
http://localhost:8080/h2-console
