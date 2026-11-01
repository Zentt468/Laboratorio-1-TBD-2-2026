# Mish: Plataforma de Arriendo de Alojamientos Turísticos

Laboratorio 1 · Taller de Bases de Datos 2-2026 · Grupo 4

Marketplace donde los **anfitriones** publican alojamientos, los **huéspedes** los reservan y el **administrador** modera publicaciones y reseñas. Gran parte de la lógica de negocio vive en PostgreSQL: procedimientos almacenados, triggers, vista materializada e índices.

---

## 1. Arquitectura y tecnologías

```
 Navegador ── http://localhost:5173 ──▶ Frontend (React + Vite)
     │                                        │
     │ 1. Login (OpenID Connect)              │ 2. Peticiones REST con "Authorization: Bearer <token>"
     ▼                                        ▼
 Keycloak  (:8080)  ◀── valida la firma ──  Backend Spring Boot (:8081)
                                              │ 3. SQL con JdbcClient (sin ORM)
                                              ▼
                                        PostgreSQL 15 (:5432)
```

| Capa | Tecnología | Detalle |
| --- | --- | --- |
| Base de datos | PostgreSQL 15 | Tablas, 2 procedimientos almacenados, 2 triggers, 1 vista materializada, 3 índices |
| Backend | Java 25, Spring Boot 4.1, `JdbcClient` | API REST **sin ORM**: todas las consultas son SQL escrito a mano |
| Seguridad | Keycloak 26.8.1, JWT, Spring Security OAuth2 Resource Server | Control de acceso por rol (RBAC) con `@PreAuthorize` |
| Frontend | React 19, Vite, axios, keycloak-js, Bootstrap 5 | Cliente axios que agrega el token a cada petición |
| Despliegue | Docker y Docker Compose | Un solo comando levanta los 4 servicios |

### Roles

| Rol | Puede |
| --- | --- |
| `ROLE_HUESPED` | Ver alojamientos, crear y cancelar **sus propias** reservas, reseñar **sus** estadías completadas |
| `ROLE_ANFITRION` | Publicar, editar y eliminar **sus propios** alojamientos y tarifas |
| `ROLE_ADMIN` | Moderar alojamientos y reseñas, ver auditorías y el reporte de ocupación |

Ver alojamientos, buscar disponibilidad y ver el resumen de reseñas no requiere iniciar sesión.

### Archivos de la base de datos

- Dentro de la carpeta resources : demo -> src -> main -> resources , se encuentran los archivos correspondientes a la base de datos.

---

## 2. Manual de instalación y despliegue

### Requisitos

- [Docker Desktop](https://www.docker.com/products/docker-desktop/) instalado y **en ejecución**.
- Git.
- Los puertos **5173, 8080, 8081 y 5432** libres.

No es necesario instalar Java, Maven, Node ni PostgreSQL: todo se compila y ejecuta dentro de los contenedores.

### Paso a paso

**1. Clonar el repositorio**
```bash
git clone <URL_DEL_REPOSITORIO>
cd Laboratorio-1-TBD-2-2026
```

**2. Levantar todos los servicios**
```bash
docker compose up --build
```
La primera vez tarda varios minutos, porque descarga las imágenes y compila el backend.

**3. Esperar a que todo esté listo**

El sistema está listo cuando en la consola aparece `Started DemoApplication` (backend) y `Listening on: http://0.0.0.0:8080` (Keycloak). Al arrancar, el backend crea automáticamente las tablas, los procedimientos, los triggers, la vista materializada y los datos de prueba, y Keycloak importa el realm con los usuarios.

**4. Abrir la aplicación**

| Servicio | URL |
| --- | --- |
| Aplicación (frontend) | http://localhost:5173 |
| API (backend) | http://localhost:8081/api |
| Consola de Keycloak | http://localhost:8080 (usuario `admin`, contraseña `admin`) | Realm : alojamientos-realm


### Usuarios de prueba

| Usuario | Correo | Contraseña | Rol | Datos de prueba |
| --- | --- | --- | --- | --- |
| admin | admin@mish.cl | admin | ROLE_ADMIN | Moderación, auditorías y reportes |
| juan | juan@mish.cl | 1234567 | ROLE_ANFITRION | Dueño de los alojamientos 1 y 2 |
| maria | maria@mish.cl | 12345678 | ROLE_ANFITRION | Dueña de los alojamientos 3 y 4 |
| cristobal | cristobal@mish.cl |  12345 | ROLE_ANFITRION | Dueño del alojamiento 5 |
| diego | diego@mish.cl | 123456 | ROLE_HUESPED | Tiene reservas confirmadas y una finalizada |
| carla | carla@mish.cl | 1234 | ROLE_HUESPED | Tiene reservas confirmadas |

### Variables de entorno

Están definidas en `docker-compose.yml` y no hace falta modificarlas para ejecutar el proyecto.

| Variable | Valor | Uso |
| --- | --- | --- |
| `POSTGRES_DB` / `POSTGRES_USER` / `POSTGRES_PASSWORD` | `alojamientos_db` / `postgres` / `password` | Base de datos que crea el contenedor |
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://postgres-db:5432/alojamientos_db` | Conexión del backend a la base |
| `SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_ISSUER_URI` | `http://localhost:8080/realms/alojamientos-realm` | Emisor que debe tener el token (como lo ve el navegador) |
| `SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_JWK_SET_URI` | `http://keycloak:8080/realms/alojamientos-realm/protocol/openid-connect/certs` | Desde dónde el backend descarga las llaves para verificar la firma |
| `KEYCLOAK_ADMIN` / `KEYCLOAK_ADMIN_PASSWORD` | `admin` / `admin` | Acceso a la consola de Keycloak |

### Comandos útiles

| Acción | Comando |
| --- | --- |
| Detener los servicios | `docker compose down` |
| Reiniciar desde cero (borra base de datos y Keycloak) | `docker compose down -v` y luego `docker compose up --build` |
| Recompilar solo el backend tras cambiar código | `docker compose up --build -d backend` |
| Ver los logs del backend | `docker compose logs -f backend` |

### Conectarse a la base de datos (opcional)

Con pgAdmin u otro cliente: host `localhost`, puerto `5432`, base `alojamientos_db`, usuario `postgres`, contraseña `password`.

### Solución de problemas

| Síntoma | Causa y solución |
| --- | --- |
| `port is already allocated` | Otro programa usa ese puerto (por ejemplo, un PostgreSQL local en el 5432). Deténlo y vuelve a ejecutar `docker compose up`. |
| El backend se detiene al arrancar | Revisa `docker compose logs backend`. Si el problema fue de datos, reinicia desde cero con `docker compose down -v`. |
| No se puede iniciar sesión | Keycloak tarda en arrancar. Espera a que aparezca `Listening on` en sus logs y recarga la página. |
| `401` en las peticiones | El token no es válido o expiró. Cierra sesión y vuelve a entrar. |
| `403` en una acción | El usuario no tiene el rol requerido (ver tabla de roles). |

---

## 3. Base de datos

Los scripts están en `demo/src/main/resources/` y el backend los ejecuta en este orden en cada arranque:

1. **`schema.sql`**: tablas, índices y vista materializada.
2. **`procedures.sql`**: procedimientos almacenados y triggers.
3. **`data.sql`**: limpia las tablas y carga los datos de prueba.

Las sentencias terminan en `^;` en lugar de `;`, porque el cuerpo de las funciones PL/pgSQL contiene `;` internos. Esto se configura con `spring.sql.init.separator=^;`.

### Modelo

`usuario` · `alojamiento` · `tarifa` · `reserva` · `pago` · `calendario` · `resena` · `auditoria`

### Objetos programables y de rendimiento

| Requisito | Objeto | Qué hace |
| --- | --- | --- |
| Procedimiento 1 | `sp_confirmar_reserva` | En una sola transacción: verifica disponibilidad, calcula el total noche a noche según la tarifa de temporada (o el precio base), crea la reserva, registra el pago y bloquea las fechas en el calendario. Bloquea el alojamiento con `FOR UPDATE` para evitar reservas simultáneas. |
| Procedimiento 2 | `cancelarReserva` | Calcula el reembolso según la política del alojamiento y los días de anticipación, marca la reserva como `CANCELADA`, registra la devolución como pago negativo y libera las fechas del calendario. |
| Trigger 1 | `trg_validar_reserva` | Antes de insertar o actualizar una reserva confirmada, rechaza la operación si supera la capacidad, no cumple la estadía mínima o se traslapa con otra reserva confirmada del mismo alojamiento. |
| Trigger 2 | `trg_auditoria_alojamiento` | Registra en `auditoria` cada cambio de `precio_noche` o `politica_cancel`, con valor anterior, valor nuevo, correo del usuario y fecha y hora. El backend entrega el usuario con `set_config('app.usuario', ...)` dentro de la misma transacción. |
| Vista materializada | `mv_ocupacion_ingresos` | Reservas e ingresos por ciudad, tipo de alojamiento, año y mes. Se refresca al cargar los datos y todos los días a las 02:00 mediante una tarea programada del backend (`@Scheduled`). |
| Índice GIST | `idx_reserva_rango_fechas` | Sobre `daterange(fecha_inicio, fecha_fin)` de `reserva`; acelera la búsqueda de disponibilidad con el operador `&&`. |
| Índice B-Tree | `idx_reserva_id_alojamiento` | Sobre `reserva(id_alojamiento)`. |
| Índice B-Tree | `idx_alojamiento_ciudad` | Sobre `alojamiento(ciudad)`. |

### Políticas de cancelación

| Política | 100 % de reembolso | 50 % de reembolso | Sin reembolso |
| --- | --- | --- | --- |
| `flexible` | 1 día o más antes | — | Menos de 1 día |
| `moderada` | 5 días o más antes | 2 a 4 días antes | Menos de 2 días |
| `estricta` | 14 días o más antes | 7 a 13 días antes | Menos de 7 días |


Los errores de negocio que lanzan los triggers y procedimientos se devuelven con código `400` y este formato:
```json
{ "error": "La cantidad de huéspedes (8) supera la capacidad máxima de alojamiento permitido (4)." }
```

### Endpoints principales

| Método | Ruta | Rol | Descripción |
| --- | --- | --- | --- |
| GET | `/alojamientos` | Público | Lista todos los alojamientos |
| GET | `/alojamientos/{id}` | Público | Detalle de un alojamiento |
| GET | `/alojamientos/buscar?ciudad=&fechaInicio=&fechaFin=&huespedes=` | Público | Alojamientos disponibles (usa el índice GIST) |
| POST | `/alojamientos` | Anfitrión, Admin | Publica un alojamiento (el anfitrión queda como dueño) |
| PUT | `/alojamientos` | Anfitrión dueño, Admin | Modifica un alojamiento (dispara el Trigger 2) |
| DELETE | `/alojamientos/{id}` | Anfitrión dueño, Admin | Elimina un alojamiento |
| POST | `/reservas/confirmar` | Huésped | Crea una reserva con el Procedimiento 1 |
| POST | `/reservas/{id}/cancelar` | Huésped dueño | Cancela una reserva con el Procedimiento 2 |
| GET | `/reservas/usuario/{idUsuario}` | Huésped, Admin | Reservas de un usuario, con datos del alojamiento |
| GET | `/resenas/alojamiento/{id}/resumen` | Público | Calificación promedio e historial de reseñas |
| POST | `/resenas` | Huésped | Reseña una estadía propia y completada |
| PUT | `/resenas` | Admin | Modera una reseña |
| POST / PUT / DELETE | `/tarifas` | Anfitrión dueño, Admin | Gestiona las tarifas por temporada |
| GET | `/auditorias` | Admin | Cambios registrados por el Trigger 2 |
| GET | `/reportes/ocupacion` | Admin | Datos de la vista materializada |
| GET | `/usuarios/me` | Autenticado | Devuelve el usuario logueado; si es su primer ingreso, lo vincula o lo crea a partir del token |

Además, cada entidad tiene su CRUD en `/usuarios`, `/alojamientos`, `/tarifas`, `/reservas`, `/pagos`, `/calendario` y `/resenas`.

