# MediaTracker

![MySQL](https://img.shields.io/badge/MySQL-4479A1?logo=mysql\&logoColor=white)
![MongoDB](https://img.shields.io/badge/MongoDB-47A248?logo=mongodb\&logoColor=white)
![Java](https://img.shields.io/badge/Java-007396?logo=java\&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?logo=springboot\&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-005C4B?logo=apachemaven\&logoColor=white)
![Swagger](https://img.shields.io/badge/Swagger-85EA2D?logo=swagger\&logoColor=black)

**Author:** Alberto Nieto Lozano  
**Context:** Project developed as an assignment for the Higher Vocational Training in Multiplatform Application Development.  
Repository published as a technical showcase of architecture, data modeling, and API design.

---

## 1. Description

MediaTracker is a REST API for managing cultural works and their reviews, with a traceability system that separates:

* Transactional state in a relational database.
* Audit log and change history in a document store.

The goal of the project is to demonstrate multilayer design, integration of heterogeneous technologies, and application of consistent business rules.

---

## 2. Architecture

Layered architecture:

* **Controller**: REST endpoint exposure.
* **Service**: business rules and coordination between persistence layers.
* **Repository**: data access via JPA and MongoRepository.
* **DTO**: external API contract.
* **Domain**: JPA entities.
* **Mongo**: documents and document repositories.

Clear separation between the domain model and the exposure model.

---

## 3. Data model

### Relational persistence (MySQL + JPA)

Main entities:

* `Usuario`
* `Plataforma`
* `Obra`
* `Resena`

Relationships:

* Obra N:1 Usuario
* Obra N:1 Plataforma
* Resena N:1 Usuario
* Resena N:1 Obra

Constraints:

* Unique email in Usuario.
* Unique name in Plataforma.
* Domain validations in the Service layer.
* Normalized enumerations for type and status.

Implemented queries:

* Combined filters with pagination.
* JOIN for retrieving works with reviews.
* Average score calculation by type.

---

### Document persistence (MongoDB)

Collections:

**eventos**

* Log of actions performed on entities.
* Includes metadata and a reduced payload.

**historial_obras**

* Before and after snapshot on updates.
* Allows change reconstruction.

Queries:

* Search by user.
* Search by entity.
* Time range.
* Aggregated metric of changes by action type.

Architectural decision:

* SQL maintains the current consistent state.
* Mongo preserves traceability without polluting the relational model with historical data.

---

## 4. Business rules

* Work creation requires validated mandatory fields.
* Score validation between 0 and 10.
* Prior existence of the creating user.
* Automatic platform creation if it does not exist.
* Automatic event logging for each relevant operation.
* Full snapshot on every work update.

Validation logic resides in the Service layer.

---

## 5. Main endpoints

### Domain management

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

### Traceability queries

* GET /eventos/usuario/{userId}
* GET /eventos/entidad/{entityId}
* GET /eventos/rango
* GET /historial-obras/obra/{obraId}
* GET /historial-obras/usuario/{userId}
* GET /historial-obras/rango
* GET /historial-obras/metricas/cambios-por-accion

Interactive documentation available in Swagger UI.

---

## 6. Requirements and local setup

Requirements:

* Java 21
* Maven 3.9+
* MySQL
* MongoDB

Expected SQL database:

* Name: mediatracker
* Configuration editable in `application.properties`.

Run:

```bash
mvn clean spring-boot:run
```

Swagger:

```
http://localhost:8080/swagger-ui.html
```

---

## 7. Demonstrated technical quality

* Separation of concerns.
* Coherent domain design.
* Combined use of relational and document models.
* Structured audit logging.
* DTOs to avoid direct entity exposure.
* Custom queries and aggregations.

---

## 8. Future improvements

* Authentication and authorization integration.
* Integration tests.
* Explicit composite transaction management.
* Additional validations at the input layer.
* Containerization with Docker.
