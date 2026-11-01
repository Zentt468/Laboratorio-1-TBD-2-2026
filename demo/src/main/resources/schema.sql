-- Crear tablas para la base de datos

CREATE TABLE IF NOT EXISTS usuario (
	id_usuario SERIAL PRIMARY KEY,
	keycloak_id VARCHAR(100) UNIQUE,
	nombre VARCHAR(100) NOT NULL,
	correo VARCHAR(40) UNIQUE NOT NULL
)^;

CREATE TABLE IF NOT EXISTS alojamiento (
	id_alojamiento SERIAL PRIMARY KEY,
	id_usuario INT NOT NULL REFERENCES usuario(id_usuario) ON DELETE CASCADE,
	titulo VARCHAR(100) NOT NULL,
	tipo_alojamiento VARCHAR(30) NOT NULL,
	ciudad VARCHAR(30) NOT NULL,
	direccion VARCHAR(150) NOT NULL,
	capacidad INT NOT NULL,
	estadia_min INT NOT NULL DEFAULT 1,
	precio_noche NUMERIC(10,2) NOT NULL, 
	politica_cancel VARCHAR(20) NOT NULL,
	imagen_url TEXT NOT NULL
)^;



CREATE TABLE IF NOT EXISTS tarifa(
	id_tarifa SERIAL PRIMARY KEY,
	id_alojamiento INT NOT NULL REFERENCES alojamiento(id_alojamiento) ON DELETE CASCADE,
	temporada VARCHAR(20) NOT NULL,
	fecha_inicio DATE NOT NULL,
	fecha_fin DATE NOT NULL,
	precio NUMERIC(10,2) NOT NULL
)^;

CREATE TABLE IF NOT EXISTS reserva (
	id_reserva SERIAL PRIMARY KEY,
	id_usuario INT NOT NULL REFERENCES usuario(id_usuario) ON DELETE CASCADE,
	id_alojamiento INT NOT NULL REFERENCES alojamiento(id_alojamiento) ON DELETE CASCADE,
	n_huespedes INT NOT NULL,
	fecha_inicio DATE NOT NULL,
	fecha_fin DATE NOT NULL,
	estado_reserva VARCHAR(20) NOT NULL DEFAULT 'CONFIRMADA',
	precio_total NUMERIC(10,2) NOT NULL
)^;

CREATE TABLE IF NOT EXISTS pago(
	id_pago SERIAL PRIMARY KEY,
	id_reserva INT NOT NULL REFERENCES reserva(id_reserva) ON DELETE CASCADE,
	monto_pago NUMERIC(10,2) NOT NULL,
	fecha_pago TIMESTAMP NOT NULL DEFAULT NOW(),
	tipo_pago VARCHAR(20) NOT NULL
)^;

CREATE TABLE IF NOT EXISTS resena (
	id_resena SERIAL PRIMARY KEY,
	id_alojamiento INT NOT NULL REFERENCES alojamiento(id_alojamiento) ON DELETE CASCADE,
	id_usuario INT NOT NULL REFERENCES usuario(id_usuario) ON DELETE CASCADE,
	id_reserva INT NOT NULL REFERENCES reserva(id_reserva) ON DELETE CASCADE,
	titulo_resena VARCHAR(30),
	comentario TEXT,
	calificacion INT CHECK (calificacion BETWEEN 1 AND 5),
	fecha_resena TIMESTAMP NOT NULL DEFAULT NOW(),
	estado VARCHAR(20) DEFAULT 'PUBLICADA'
)^;

CREATE TABLE IF NOT EXISTS auditoria (
    id_auditoria SERIAL PRIMARY KEY,
    id_alojamiento INT NOT NULL REFERENCES alojamiento(id_alojamiento) ON DELETE CASCADE,
    campo_cambiado VARCHAR(20) NOT NULL,
    valor_anterior TEXT,
    valor_nuevo TEXT,
    usuario_cambio VARCHAR(100) NOT NULL,
    fecha_cambio TIMESTAMP NOT NULL DEFAULT NOW()
)^;


CREATE TABLE IF NOT EXISTS calendario (
	id_calendario SERIAL PRIMARY KEY,
	id_reserva INT NOT NULL REFERENCES reserva(id_reserva) ON DELETE CASCADE,
	fecha DATE NOT NULL,
	CONSTRAINT unique_reserva_fecha UNIQUE (id_reserva, fecha)
)^;


--Vista materializada y indices

-- Índices
CREATE INDEX IF NOT EXISTS idx_reserva_rango_fechas
    ON reserva USING GIST (daterange(fecha_inicio, fecha_fin))^;
CREATE INDEX IF NOT EXISTS idx_reserva_id_alojamiento ON reserva (id_alojamiento)^;
CREATE INDEX IF NOT EXISTS idx_alojamiento_ciudad ON alojamiento (ciudad)^;

-- Vista materializada: ocupación e ingresos mensuales por ciudad y tipo
CREATE MATERIALIZED VIEW IF NOT EXISTS mv_ocupacion_ingresos AS
SELECT
    a.ciudad,
    a.tipo_alojamiento,
    EXTRACT(YEAR FROM r.fecha_inicio)::INT  AS anio,
    EXTRACT(MONTH FROM r.fecha_inicio)::INT AS mes,
    COUNT(r.id_reserva)::INT                AS total_reservas,
    COALESCE(SUM(r.precio_total), 0)        AS ingresos_totales
FROM reserva r
JOIN alojamiento a ON a.id_alojamiento = r.id_alojamiento
WHERE r.estado_reserva IN ('CONFIRMADA', 'FINALIZADA')
GROUP BY a.ciudad, a.tipo_alojamiento, anio, mes^;

ALTER TABLE auditoria ALTER COLUMN usuario_cambio TYPE VARCHAR(100)^;
ALTER TABLE alojamiento ALTER COLUMN titulo TYPE VARCHAR(100)^;