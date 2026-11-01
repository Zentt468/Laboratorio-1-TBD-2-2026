--Datos

-- 1. LIMPIEZA DE TABLAS Y REINICIO DE ID SERIAL
TRUNCATE TABLE calendario, auditoria, resena, pago, reserva, tarifa, alojamiento, usuario RESTART IDENTITY CASCADE^;


-- 2. TABLA: usuario

INSERT INTO usuario (nombre, correo) VALUES
('Juan Perez', 'juan@mish.cl'),
('Maria Gomez', 'maria@mish.cl'),
('Diego Left', 'diego@mish.cl'),
('Carla Silva', 'carla@mish.cl'),
('Benjamin Weapon', 'benjamin@mish.cl'),
('Cristobal Filipo', 'cristobal@mish.cl')^;


-- 3. TABLA: alojamiento

INSERT INTO alojamiento (id_usuario, titulo, tipo_alojamiento, ciudad, direccion, capacidad, estadia_min, precio_noche, politica_cancel, imagen_url) VALUES
(1, 'Cabana en Pucon', 'Cabana', 'Pucon', 'Av. Colo Colo 123', 4, 2, 50000.00, 'flexible', 'https://plus.unsplash.com/premium_photo-1686090450479-370d5ddf4de1?q=80&w=1974&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=NHwxMjA3fDA%3D'), -- poner imagenes
(1, 'Dept Centrico Stgo', 'Departamento', 'Santiago', 'Av. Providencia 456', 2, 1, 35000.00, 'moderada', 'https://images.unsplash.com/photo-1621080870912-2303acd36026?q=80&w=687&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=NHwxMjA3fDA%3D'),
(2, 'Chalet Vina del Mar', 'Chalet', 'Vina del Mar', 'Av. Marina 789', 6, 3, 120000.00, 'estricta','https://images.unsplash.com/photo-1610803523148-a0052fa341bb?q=80&w=944&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=NHwxMjA3fDA%3D'),
(2, 'Cabana en la playa', 'Cabana', 'Vina del Mar', 'Av. Playa 870', 4, 2, 90000.00, 'estricta','https://images.unsplash.com/photo-1449158743715-0a90ebb6d2d8?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=NHwxMjA3fDA%3D'),
(6, 'Casa en Centro de Stgo', 'Casa', 'Santiago', 'Calle Huerfanos 1020', 4, 2, 90000.00, 'estricta','https://images.unsplash.com/photo-1570129477492-45c003edd2be?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=NHwxMjA3fDA%3D')^;


-- 4. TABLA: tarifa

INSERT INTO tarifa (id_alojamiento, temporada, fecha_inicio, fecha_fin, precio) VALUES
(1, 'Baja', CURRENT_DATE - INTERVAL '30 days', CURRENT_DATE + INTERVAL '60 days', 50000.00),
(2, 'Regular', CURRENT_DATE - INTERVAL '30 days', CURRENT_DATE + INTERVAL '60 days', 35000.00),
(3, 'Alta', CURRENT_DATE - INTERVAL '30 days', CURRENT_DATE + INTERVAL '60 days', 120000.00)^;


-- 5. TABLA: reserva

INSERT INTO reserva (id_usuario, id_alojamiento, n_huespedes, fecha_inicio, fecha_fin, estado_reserva, precio_total) VALUES
(3, 1, 2, CURRENT_DATE + INTERVAL '3 days', CURRENT_DATE + INTERVAL '6 days', 'CONFIRMADA', 150000.00),   -- Reserva 1 (Flexible, 3 noches)
(4, 2, 2, CURRENT_DATE + INTERVAL '3 days', CURRENT_DATE + INTERVAL '5 days', 'CONFIRMADA', 70000.00),    -- Reserva 2 (Moderada, 2 noches)
(3, 3, 4, CURRENT_DATE + INTERVAL '15 days', CURRENT_DATE + INTERVAL '18 days', 'CONFIRMADA', 360000.00), -- Reserva 3 (Estricta >=14d, 3 noches)
(4, 3, 2, CURRENT_DATE + INTERVAL '2 days', CURRENT_DATE + INTERVAL '5 days', 'CONFIRMADA', 360000.00),   -- Reserva 4 (Estricta <7d, 3 noches)
(3, 1, 2, CURRENT_DATE - INTERVAL '20 days', CURRENT_DATE - INTERVAL '17 days', 'FINALIZADA', 150000.00)^; -- Reserva 5


-- 6. TABLA: pago

INSERT INTO pago (id_reserva, monto_pago, fecha_pago, tipo_pago) VALUES
(1, 150000.00, NOW() - INTERVAL '1 day', 'TARJETA_CREDITO'),
(2, 70000.00, NOW() - INTERVAL '2 days', 'TRANSFERENCIA'),
(3, 360000.00, NOW() - INTERVAL '5 days', 'WEBPAY'),
(4, 360000.00, NOW() - INTERVAL '1 day', 'TARJETA_DEBITO'),
(5, 150000.00, NOW() - INTERVAL '21 days', 'TARJETA_CREDITO')^;


-- 7. TABLA: resena

INSERT INTO resena (id_alojamiento, id_usuario, id_reserva, titulo_resena, comentario, calificacion, fecha_resena, estado) VALUES
(1, 3, 5, 'Excelente estadia', 'La cabana estaba muy limpia y la vista al lago impecable.', 5, NOW() - INTERVAL '16 days', 'PUBLICADA')^;


-- 8. TABLA: calendario

INSERT INTO calendario (id_reserva, fecha) VALUES
-- Dias de Reserva 1
(1, CURRENT_DATE + INTERVAL '3 days'),
(1, CURRENT_DATE + INTERVAL '4 days'),
(1, CURRENT_DATE + INTERVAL '5 days'),
-- Dias de Reserva 2
(2, CURRENT_DATE + INTERVAL '3 days'),
(2, CURRENT_DATE + INTERVAL '4 days'),
-- Dias de Reserva 3
(3, CURRENT_DATE + INTERVAL '15 days'),
(3, CURRENT_DATE + INTERVAL '16 days'),
(3, CURRENT_DATE + INTERVAL '17 days'),
-- Dias de Reserva 4
(4, CURRENT_DATE + INTERVAL '2 days'),
(4, CURRENT_DATE + INTERVAL '3 days'),
(4, CURRENT_DATE + INTERVAL '4 days')^;

REFRESH MATERIALIZED VIEW mv_ocupacion_ingresos^;
