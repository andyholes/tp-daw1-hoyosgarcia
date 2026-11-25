# TP Integrador · DAW1 — Gestor Bibliotecario Unificado

**Alumno/a:** Hoyos Garcia, Andres · **Legajo:** 510068
**Enunciado elegido:** biblioteca


## Descripción

Este sistema permite llevar cuenta de los libros disponibles, gestionar socios y regular el prestamo de libros, aplicando penalizaciones a quienes no cumplan los plazos de devolucion.

## Cómo ejecutarlo

Requisitos: Java 21 (Maven viene incluido con `mvnw`).

```bash
./mvnw spring-boot:run
```

La API queda en `http://localhost:8080/api`. La base H2 se guarda en `data/` (no se sube al repositorio).
Consola H2: `http://localhost:8080/h2-console`.

## Modelo

| Entidad  | Atributos principales                     | Relaciones                       |
|----------|-------------------------------------------|----------------------------------|
| libro    | id, titulo, autor, isbn                   | 1 a N con prestamo               |
| socio    | id, nombre, email                         | 1 a N con prestamo               |
| prestamo | id, libro, socio, fechaDevolucion, estado | N a 1 con socio y con libro |

Estados del préstamo: `ACTIVO → DEVUELTO | VENCIDO` · `VENCIDO → DEVUELTO` · `DEVUELTO` es final.

## Endpoints

### Libros
| Método | Ruta                        | Descripción                                                | Respuestas         |
|---|-----------------------------|------------------------------------------------------------|--------------------|
| POST | /api/libros                 | Registrar un libro                                         | 201, 400, 404, 409 |
| PUT | /api/libros/{id}            | Modificar un libro                                         | 200, 400, 404, 409 |
| DELETE | /api/libros/{id}            | Borrar un libro                                            | 204, 404, 409      |
| GET | /api/libros                 | Listar libros                                              | 200                |
| GET | /api/libros/{id}            | Buscar un libro por id                                     | 200, 400, 404      |
| GET | /api/libros?disponible=true | Listar libros disponibles para prestar                     | 200                |
| GET | /api/libros/mas-prestados?top=5  | Obtener libros con más préstamos y la cantidad de cada uno | 200         |

### Socios
| Método | Ruta                       | Descripción                       | Respuestas |
|--------|----------------------------|-----------------------------------|---|
| GET    | /api/socios               | Listar socios                     | 200 |
| GET    | /api/socios/{id}          | Buscar un socio por id            | 200, 404 |
| POST   | /api/socios               | Crear un socio                    | 201, 400, 404, 409 |
| GET    | /api/socios/{id}/prestamos | Consultar los prestamos del socio | 200, 400, 404 |

### Prestamos
| Método | Ruta                            | Descripción                                                               | Respuestas |
|--------|---------------------------------|---------------------------------------------------------------------------|---|
| POST   | /api/prestamos                  | Registrar el prestamo de un libro                                         | 200, 400, 404, 409 |
| GET    | /api/prestamos                  | Obtener prestamos activos y vencidos                                      | 200 |
| GET    | /api/prestamos?estado=ACTIVO    | Filtrar prestamos por estado activo                                       | 200 |
| GET    | /api/prestamos/{id}             | Buscar un prestamo                                                        | 200, 400, 404 |
| PATCH  | /api/prestamos/{id}/estado      | Cambiar el estado                                                         | 200, 400, 404, 409 |
| PATCH  | /api/prestamos/{id}/renovar     | Sumar 7 dias a la fecha de devolucion solo por unica vez y si esta activo | 200, 400, 404, 409 |
| GET    | /api/prestamos/{id}/pagar-multa | Saldar la multa de un prestamo                                            | 200, 400, 404, 409 |

## Reglas de negocio (409)

1. Un libro con un préstamo no devuelto no se puede volver a prestar.
```json
//completar
```
2. Un socio puede tener como máximo 3 préstamos no devueltos.
```json
//completar
```
3. No puede haber dos libros con el mismo ISBN.
```json
//completar
```

## Pruebas

En `pruebas.http` están todas las peticiones de ejemplo (se ejecutan desde IntelliJ).
