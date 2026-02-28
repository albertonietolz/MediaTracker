# MediaTracker

![MySQL](https://img.shields.io/badge/MySQL-4479A1?logo=mysql\&logoColor=white)
![MongoDB](https://img.shields.io/badge/MongoDB-47A248?logo=mongodb\&logoColor=white)
![Java](https://img.shields.io/badge/Java-007396?logo=java\&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?logo=springboot\&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-005C4B?logo=apachemaven\&logoColor=white)
![Swagger](https://img.shields.io/badge/Swagger-85EA2D?logo=swagger\&logoColor=black)

**Autor:** Alberto Nieto Lozano  
**Contexto:** Proyecto académico desarrollado para el CFGS Desarrollo de Aplicaciones Multiplataforma.  
Repositorio publicado como muestra técnica de arquitectura, modelado y diseño de API.

---

## 1. Descripción

MediaTracker es una API REST para la gestión de obras culturales y sus reseñas, con un sistema de trazabilidad que separa:

* Estado transaccional en base de datos relacional.
* Auditoría e historial de cambios en base documental.

El objetivo del proyecto es demostrar diseño multicapa, integración de tecnologías heterogéneas y aplicación de reglas de negocio coherentes.

---

## 2. Arquitectura

Arquitectura por capas:

* **Controller**: exposición de endpoints REST.
* **Service**: reglas de negocio y coordinación entre persistencias.
* **Repository**: acceso a datos con JPA y MongoRepository.
* **DTO**: contrato externo de la API.
* **Domain**: entidades JPA.
* **Mongo**: documentos y repositorios documentales.

Separación clara entre modelo de dominio y modelo de exposición.

---

## 3. Modelo de datos

### Persistencia relacional (MySQL + JPA)

Entidades principales:

* `Usuario`
* `Plataforma`
* `Obra`
* `Resena`

Relaciones:

* Obra N:1 Usuario
* Obra N:1 Plataforma
* Resena N:1 Usuario
* Resena N:1 Obra

Restricciones:

* Email único en Usuario.
* Nombre único en Plataforma.
* Validaciones de dominio en capa Service.
* Enumeraciones normalizadas para tipo y estado.

Consultas implementadas:

* Filtros combinados con paginación.
* JOIN para recuperación de obras con reseñas.
* Cálculo de media de nota por tipo.

---

### Persistencia documental (MongoDB)

Colecciones:

**eventos**

* Registro de acciones sobre entidades.
* Incluye metadata y payload reducido.

**historial_obras**

* Snapshot anterior y posterior en actualizaciones.
* Permite reconstrucción de cambios.

Consultas:

* Búsqueda por usuario.
* Búsqueda por entidad.
* Rango temporal.
* Métrica agregada de cambios por tipo de acción.

Decisión arquitectónica:

* SQL mantiene el estado actual consistente.
* Mongo conserva trazabilidad sin contaminar el modelo relacional con datos históricos.

---

## 4. Reglas de negocio

* Creación de obra requiere datos obligatorios validados.
* Validación de nota entre 0 y 10.
* Existencia previa del usuario creador.
* Creación automática de plataforma si no existe.
* Registro automático de evento en cada operación relevante.
* Snapshot completo en cada actualización de obra.

La lógica de validación reside en la capa Service.

---

## 5. Endpoints principales

### Gestión de dominio

* POST /usuarios

* GET /usuarios

* PUT /usuarios/{id}

* DELETE /usuarios/{id}

* POST /plataformas

* GET /plataformas

* PUT /plataformas/{id}

* DELETE /plataformas/{id}

* POST /obras

* GET /obras

* GET /obras/{id}

* PUT /obras/{id}

* DELETE /obras/{id}

* POST /resenas

* GET /resenas/obra/{obraId}

* PUT /resenas/{id}

* DELETE /resenas/{id}

### Consulta de trazabilidad

* GET /eventos/usuario/{userId}
* GET /eventos/entidad/{entityId}
* GET /eventos/rango
* GET /historial-obras/obra/{obraId}
* GET /historial-obras/usuario/{userId}
* GET /historial-obras/rango
* GET /historial-obras/metricas/cambios-por-accion

Documentación interactiva disponible en Swagger UI.

---

## 6. Requisitos y ejecución local

Requisitos:

* Java 21
* Maven 3.9+
* MySQL
* MongoDB

Base de datos SQL esperada:

* Nombre: mediatracker
* Configuración editable en `application.properties`.

Ejecución:

```bash
mvn clean spring-boot:run
```

Swagger:

```
http://localhost:8080/swagger-ui.html
```

---

## 7. Calidad técnica demostrada

* Separación de responsabilidades.
* Diseño coherente de dominio.
* Uso combinado de modelo relacional y documental.
* Registro estructurado de auditoría.
* DTOs para evitar exposición directa de entidades.
* Consultas personalizadas y agregaciones.

---

## 8. Líneas de evolución

* Integración de autenticación y autorización.
* Tests de integración.
* Gestión explícita de transacciones compuestas.
* Validaciones adicionales en capa de entrada.
* Contenerización con Docker.
