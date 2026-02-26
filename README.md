````markdown
<h1 align="center">MediaTracker</h1>

<p align="center">
  API REST para registrar obras y su trazabilidad con SQL + MongoDB
</p>

<p align="center">
  <img src="https://img.shields.io/badge/MySQL-4479A1?logo=mysql&logoColor=white"/>
  <img src="https://img.shields.io/badge/MongoDB-47A248?logo=mongodb&logoColor=white"/>
  <img src="https://img.shields.io/badge/Java-007396?logo=java&logoColor=white"/>
  <img src="https://img.shields.io/badge/Spring_Boot-6DB33F?logo=springboot&logoColor=white"/>
  <img src="https://img.shields.io/badge/Maven-005C4B?logo=apachemaven&logoColor=white"/>
  <img src="https://img.shields.io/badge/Swagger-85EA2D?logo=swagger&logoColor=black"/>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/status-en%20desarrollo-blue"/>
  <img src="https://img.shields.io/badge/license-MIT-lightgrey"/>
</p>

---

## Índice

- [Qué hace el proyecto](#1-qué-hace-el-proyecto)
- [Modelo SQL](#2-modelo-sql---entidades-y-relaciones)
- [Tema y reglas de negocio](#3-tema-y-reglas-de-negocio)
- [Mongo](#4-qué-se-guarda-en-mongo-y-por-qué)
- [Estructura](#5-estructura-del-proyecto)
- [Endpoints](#6-endpoints-principales)
- [Despliegue](#7-script-y-guía-de-despliegue-local)
- [Mejoras](#8-mejoras-pendientes-y-aprendizajes)

---

## Arquitectura general

```mermaid
flowchart LR
    Client --> Controller
    Controller --> Service
    Service --> MySQL[(MySQL)]
    Service --> MongoDB[(MongoDB)]
````

---

Realizado por: Alberto Nieto Lozano y Alejandro Prieto Mellado

API REST para registrar obras (videojuegos, películas, libros, series), sus reseñas y su trazabilidad de cambios. Combina SQL (MySQL + JPA/Hibernate) para el dato transaccional y MongoDB para auditoría e historial.

---

## 1) Qué hace el proyecto

**MediaTracker** permite:

* Gestionar usuarios y plataformas.
* Crear, editar, listar y eliminar obras.
* Crear, editar, listar y eliminar reseñas sobre obras.
* Registrar en Mongo los eventos de auditoría y los snapshots de cambios en obras.

---

## 2) Modelo SQL - entidades y relaciones

### Entidades JPA

* `Usuario` (`usuarios`): `id`, `nombre`, `email` único.
* `Plataforma` (`plataformas`): `id`, `nombre` único.
* `Obra` (`obras`):

  * `id`, `titulo`, `tipo`, `descripcion`, `fechaLanzamiento`, `estado`, `fechaRegistro`.
  * FK: `plataforma_id`, `creador_id`.
* `Resena` (`resenas`): `id`, `nota`, `comentario`, `fecha`, `usuario_id`, `obra_id`.

### Persistencia y restricciones

* Gestionado con JPA/Hibernate.
* `spring.jpa.hibernate.ddl-auto=update` para evolución de esquema en desarrollo.
* Unicidad en `usuarios.email` y `plataformas.nombre`.

### Relaciones

* `Obra` N:1 `Usuario`
* `Obra` N:1 `Plataforma`
* `Resena` N:1 `Usuario`
* `Resena` N:1 `Obra`

### Diagrama entidad-relación

```mermaid
erDiagram
    USUARIO ||--o{ OBRA : "crea"
    PLATAFORMA ||--o{ OBRA : "aloja"
    USUARIO ||--o{ RESENA : "escribe"
    OBRA ||--o{ RESENA : "tiene"

    USUARIO {
        bigint id PK
        varchar nombre
        varchar email UK
    }

    PLATAFORMA {
        bigint id PK
        varchar nombre UK
    }

    OBRA {
        bigint id PK
        varchar titulo
        enum tipo
        text descripcion
        date fechaLanzamiento
        enum estado
        datetime fechaRegistro
        bigint plataforma_id FK
        bigint creador_id FK
    }

    RESENA {
        bigint id PK
        int nota
        text comentario
        datetime fecha
        bigint usuario_id FK
        bigint obra_id FK
    }
```

### Consultas SQL relevantes

* Filtrado combinado de obras por `tipo`, `estado` y `creador` con paginación.
* Consulta de obras con reseñas mediante `JOIN` (`findObrasConResenas`).
* Cálculo de media de nota por tipo de obra (`mediaNotaPorTipo`).

---

## 3) Tema y reglas de negocio

### Tema

Seguimiento centralizado de obras de tipos `VIDEOJUEGO`, `PELICULA`, `LIBRO`, `SERIE` y su valoración mediante reseñas.

### Reglas de negocio

* Una obra requiere `titulo`, `tipo`, `estado`, `usuarioId` y `plataformaNombre`.
* `estado` normalizado con enum `PENDIENTE`, `EN_PROGRESO`, `COMPLETADO`, `ABANDONADO`.
* `tipo` normalizado con enum `VIDEOJUEGO`, `PELICULA`, `LIBRO`, `SERIE`.
* Reseña requiere `usuarioId`, `obraId` y `nota`.
* `nota` validada entre 0 y 10.
* Creación y actualización de obra exige existencia del usuario creador.
* Si la plataforma no existe, se crea automáticamente.
* Operaciones generan eventos en Mongo con tipos `CREATE_*`, `UPDATE_*`, `DELETE_*`.
* Actualización de obra almacena snapshot `antes` y `despues`.

---

## 4) Qué se guarda en Mongo y por qué

### Colecciones y propósito

#### `eventos`

Auditoría operativa de acciones sobre entidades.

Campos:
`timestamp`, `userId`, `entityType`, `entityId`, `type`, `payload`.

#### `historial_obras`

Trazabilidad detallada de cambios en obras.

Campos:
`obraId`, `userId`, `timestamp`, `accion`, `antes`, `despues`.

### Ejemplos de documento

```json
{
  "_id": "65f8a3b2c4e1d2f3a4b5c6d7",
  "timestamp": "2026-02-22T14:35:42.123Z",
  "userId": 1,
  "entityType": "Obra",
  "entityId": 5,
  "type": "CREATE_OBRA",
  "payload": {
    "titulo": "Elden Ring",
    "estado": "EN_PROGRESO",
    "tipo": "VIDEOJUEGO"
  }
}
```

```json
{
  "_id": "65f8b1c2d3e4f5a6b7c8d9e0",
  "obraId": 5,
  "userId": 1,
  "timestamp": "2026-02-22T15:20:18.456Z",
  "accion": "UPDATE_OBRA",
  "antes": { },
  "despues": { }
}
```

> Justificación arquitectónica
> SQL almacena el estado vigente con relaciones.
> Mongo guarda trazabilidad en JSON flexible para auditoría e historial.

---

## 5) Estructura del proyecto

```text
src/main/java/com/tuapp
├── config/
├── controller/
├── domain/
├── dto/
├── mongo/
├── repository/
└── service/
```

---

## 6) Endpoints principales

### Endpoints SQL

| Método | Endpoint               |
| ------ | ---------------------- |
| POST   | /usuarios              |
| GET    | /usuarios              |
| PUT    | /usuarios/{id}         |
| DELETE | /usuarios/{id}         |
| POST   | /plataformas           |
| GET    | /plataformas           |
| PUT    | /plataformas/{id}      |
| DELETE | /plataformas/{id}      |
| POST   | /obras                 |
| GET    | /obras                 |
| GET    | /obras/{id}            |
| PUT    | /obras/{id}            |
| DELETE | /obras/{id}            |
| POST   | /resenas               |
| GET    | /resenas/obra/{obraId} |
| PUT    | /resenas/{id}          |
| DELETE | /resenas/{id}          |

### Endpoints Mongo

| Método | Endpoint                                     |
| ------ | -------------------------------------------- |
| GET    | /eventos/usuario/{userId}                    |
| GET    | /eventos/entidad/{entityId}                  |
| GET    | /eventos/rango                               |
| GET    | /historial-obras/obra/{obraId}               |
| GET    | /historial-obras/usuario/{userId}            |
| GET    | /historial-obras/rango                       |
| GET    | /historial-obras/metricas/cambios-por-accion |

---

## 7) Script y guía de despliegue local

### Requisitos

| Requisito | Versión          |
| --------- | ---------------- |
| Java      | 17               |
| Maven     | 3.9+             |
| MongoDB   | Community Server |
| MySQL     | Server           |

```sql
CREATE DATABASE IF NOT EXISTS mediatracker;
CREATE USER IF NOT EXISTS 'mediatracker_user'@'%' IDENTIFIED BY 'mediatracker_pass';
GRANT ALL PRIVILEGES ON mediatracker.* TO 'mediatracker_user'@'%';
FLUSH PRIVILEGES;
```

```powershell
mvn clean spring-boot:run
```

API: `http://localhost:8080`
Swagger UI: `http://localhost:8080/swagger-ui.html`

---

## 8) Mejoras pendientes y aprendizajes

### Mejoras pendientes

* Añadir autenticación y autorización con Spring Security y JWT.
* Añadir tests de integración.
* Exponer endpoint `mediaNotaPorTipo`.
* Manejo transaccional explícito con `@Transactional`.

### Aprendizajes

> Modelado relacional frente a documental.
> Integración SQL y Mongo sin duplicar lógica.
> Diseño de auditoría trazable.

```
```
