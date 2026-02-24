# Breaking Rules Stock - API

API REST desarrollada con Spring Boot para la gestión básica de stock de productos.  
Proyecto orientado a practicar buenas prácticas de backend con arquitectura en capas y validaciones de negocio.

---

## Tecnologías
- Java 21
- Spring Boot 3
- Spring Web
- Spring Data JPA
- H2 Database (entorno local)
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
- Consola H2 para testing

- Arquitectura en capas:
  - Controller
  - Service
  - Repository

---

## Conceptos aplicados
- Inyección de dependencias
- Separación de responsabilidades
- API RESTful
- Manejo de entidades JPA
- Validaciones de negocio
- Testing unitario
- Documentación de API con OpenAPI

---

## ▶️ ¿Cómo ejecutar el proyecto?

```bash
git clone https://github.com/MartinMeza22/breaking-rules-stock.git
cd breaking-rules-stock
mvn spring-boot:run
```

Swagger:
http://localhost:8080/swagger-ui.html

Consola H2:
http://localhost:8080/h2-console
