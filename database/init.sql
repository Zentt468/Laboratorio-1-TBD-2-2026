CREATE TABLE IF NOT EXISTS usuario (
	id_usuario SERIAL PRIMARY KEY,
	rut VARCHAR(15) UNIQUE NOT NULL,
	nombre VARCHAR(100) NOT NULL,
	correo VARCHAR(40) UNIQUE NOT NULL,
	contrasena VARCHAR(255) NOT NULL,
	rol VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS alojamiento (
	id_alojamiento SERIAL PRIMARY KEY,
	id_usuario INT NOT NULL REFERENCES usuario(id_usuario) ON DELETE CASCADE,
	titulo VARCHAR(30) NOT NULL,
	tipo_alojamiento VARCHAR(30) NOT NULL,
	ciudad VARCHAR(30) NOT NULL,
	direccion VARCHAR(150) NOT NULL,
	capacidad INT NOT NULL,
	estadia_min INT NOT NULL DEFAULT 1,
	precio_noche NUMERIC(10,2) NOT NULL, 
	politica_cancel VARCHAR(20) NOT NULL
);



CREATE TABLE IF NOT EXISTS tarifa(
	id_tarifa SERIAL PRIMARY KEY,
	id_alojamiento INT NOT NULL REFERENCES alojamiento(id_alojamiento) ON DELETE CASCADE,
	temporada VARCHAR(20) NOT NULL,
	fecha_inicio DATE NOT NULL,
	fecha_fin DATE NOT NULL,
	precio NUMERIC(10,2) NOT NULL
);

CREATE TABLE IF NOT EXISTS reserva (
	id_reserva SERIAL PRIMARY KEY,
	id_usuario INT NOT NULL REFERENCES usuario(id_usuario) ON DELETE CASCADE,
	id_alojamiento INT NOT NULL REFERENCES alojamiento(id_alojamiento) ON DELETE CASCADE,
	n_huespedes INT NOT NULL,
	fecha_inicio DATE NOT NULL,
	fecha_fin DATE NOT NULL,
	estado_reserva VARCHAR(20) NOT NULL DEFAULT 'CONFIRMADA',
	precio_total NUMERIC(10,2) NOT NULL
);

CREATE TABLE IF NOT EXISTS pago(
	id_pago SERIAL PRIMARY KEY,
	id_reserva INT NOT NULL REFERENCES reserva(id_reserva) ON DELETE CASCADE,
	monto_pago NUMERIC(10,2) NOT NULL,
	fecha_pago TIMESTAMP NOT NULL DEFAULT NOW(),
	tipo_pago VARCHAR(20) NOT NULL
);

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
);

CREATE TABLE IF NOT EXISTS auditoria (
    id_auditoria SERIAL PRIMARY KEY,
    id_alojamiento INT NOT NULL REFERENCES alojamiento(id_alojamiento) ON DELETE CASCADE,
    campo_cambiado VARCHAR(20) NOT NULL,
    valor_anterior TEXT,
    valor_nuevo TEXT,
    usuario_cambio VARCHAR(20) NOT NULL,
    fecha_cambio TIMESTAMP NOT NULL DEFAULT NOW()
);


CREATE TABLE IF NOT EXISTS calendario (
	id_calendario SERIAL PRIMARY KEY,
	id_reserva INT NOT NULL REFERENCES reserva(id_reserva) ON DELETE CASCADE,
	fecha DATE NOT NULL,
	CONSTRAINT unique_reserva_fecha UNIQUE (id_reserva, fecha)
);

INSERT INTO usuario (rut, nombre, correo, contrasena, rol) 
VALUES 
('11.111.111-1', 'Anfitrión Principal', 'anfitrion@alojamientos.cl', '1234', 'ANFITRION'),
('22.222.222-2', 'Huésped Inicial', 'huesped@alojamientos.cl', '1234', 'HUESPED')
ON CONFLICT (rut) DO NOTHING;

-- Alojamientos de prueba vinculados al id_usuario = 1
INSERT INTO alojamiento (id_usuario, titulo, tipo_alojamiento, ciudad, direccion, capacidad, estadia_min, precio_noche, politica_cancel)
VALUES 
(1, 'Cabaña en el Lago', 'Cabaña', 'Pucón', 'Camino al Volcán 1234', 4, 2, 65000.00, 'FLEXIBLE'),
(1, 'Depto Centro', 'Departamento', 'Santiago', 'Av. Libertador 450', 2, 1, 45000.00, 'MODERADA');
