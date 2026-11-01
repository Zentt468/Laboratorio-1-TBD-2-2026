
-- Procedimientos Almacenados y Triggers para la gestión de reservas y auditoría de cambios en alojamientos


-- Procedimiento Almacenado 1

CREATE OR REPLACE PROCEDURE sp_confirmar_reserva(
	p_id_usuario INT,
	p_id_alojamiento INT,
	p_fecha_inicio DATE,
	p_fecha_fin DATE,
	p_n_huespedes INT,
	p_tipo_pago VARCHAR,
	INOUT p_id_reserva INT DEFAULT NULL
)
LANGUAGE plpgsql
AS $$
DECLARE
	v_fechas_ocupadas INT;
	v_total NUMERIC := 0;
	v_precio_noche NUMERIC;  
	v_fecha_actual DATE;
BEGIN
	
	-- EVITAR DUPLICADOS EN CONCURRENCIA

	PERFORM id_alojamiento 
FROM alojamiento 
WHERE id_alojamiento = p_id_alojamiento 
FOR UPDATE; 
	
	
	-- VALIDAR DISPONIBILIDAD

	-- Contar cuantas de las fechas solicitadas ya están registradas en el calendario
	SELECT COUNT(*) INTO v_fechas_ocupadas
	FROM calendario c
	JOIN reserva r ON c.id_reserva = r.id_reserva
	WHERE r.id_alojamiento = p_id_alojamiento
	  AND c.fecha >= p_fecha_inicio
	  AND c.fecha < p_fecha_fin;

	-- Si el conteo es mayor a 0, una fecha al menos está tomada
	IF v_fechas_ocupadas > 0 THEN
		RAISE EXCEPTION 'El alojamiento no se encuentra disponible en una de las fechas seleccionadas';
	END IF;

	
	-- CALCULAR EL TOTAL SEGUN LA TARIFA DE TEMPORADA DE CADA NOCHE
	
	v_fecha_actual := p_fecha_inicio;
	-- iterar mientras la fecha sea menor a la determinada
	WHILE v_fecha_actual < p_fecha_fin LOOP
		-- Tarifa de temporada, busca
		SELECT precio INTO v_precio_noche
		FROM tarifa
		WHERE id_alojamiento = p_id_alojamiento
		  AND v_fecha_actual BETWEEN fecha_inicio AND fecha_fin
		LIMIT 1;

		-- precio normal, caso que no encuentre
		IF v_precio_noche IS NULL THEN
			SELECT precio_noche INTO v_precio_noche
			FROM alojamiento
			WHERE id_alojamiento = p_id_alojamiento;
		END IF;

		-- Sumar el precio de este dia, con los demas dias
		v_total := v_total + v_precio_noche;
		-- Incrementar 1 dia, para ver el siguiente dia en la fecha
		v_fecha_actual := v_fecha_actual + INTERVAL '1 day';
	END LOOP;

	
	-- CREAR RESERVA Y REGISTRAR EL PAGO


	INSERT INTO reserva (
		id_usuario,
		id_alojamiento,
		n_huespedes,
		fecha_inicio,
		fecha_fin,
		estado_reserva,
		precio_total
	)
	VALUES (
		p_id_usuario,
		p_id_alojamiento,
		p_n_huespedes,
		p_fecha_inicio,
		p_fecha_fin,
		'CONFIRMADA',
		v_total
	)
	RETURNING id_reserva INTO p_id_reserva;

	-- Registrar transaccion en pago entity
	INSERT INTO pago (id_reserva, monto_pago, fecha_pago, tipo_pago)
	VALUES (p_id_reserva, v_total, NOW(), p_tipo_pago);
	-- NOW, introduce la fecha exacta de este caso el pago

	
	-- BLOQUEAR FECHAS EN EL CALENDARIO
	

	v_fecha_actual := p_fecha_inicio;

	WHILE v_fecha_actual < p_fecha_fin LOOP
		INSERT INTO calendario(id_reserva, fecha)
		VALUES (p_id_reserva, v_fecha_actual);
		v_fecha_actual := v_fecha_actual + INTERVAL '1 day';
	END LOOP;
END;
$$^;


-- Trigger 1 


CREATE OR REPLACE FUNCTION fn_validar_reserva()
RETURNS TRIGGER AS $$
DECLARE
    v_capacidad INT;
    v_estadia_min INT;
    v_traslapes INT;
BEGIN
    IF NEW.estado_reserva = 'CONFIRMADA' THEN

        
        -- 1. VALIDAR CAPACIDAD Y ESTADÍA MÍNIMA DEL ALOJAMIENTO
        
        SELECT capacidad, estadia_min
        INTO v_capacidad, v_estadia_min
        FROM alojamiento
        WHERE id_alojamiento = NEW.id_alojamiento;

        -- Validar capacidad de huéspedes
        IF NEW.n_huespedes > v_capacidad THEN
            RAISE EXCEPTION 'La cantidad de huéspedes (%) supera la capacidad máxima de alojamiento permitido (%).', 
                            NEW.n_huespedes, v_capacidad;
        END IF;

        -- Validar estadía mínima (SE CIERRA SU BLOQUE IF DE INMEDIATO)
        IF (NEW.fecha_fin - NEW.fecha_inicio) < v_estadia_min THEN
            RAISE EXCEPTION 'La estadía seleccionada (%) es menor que la estadía mínima (%) requerida.', 
                            (NEW.fecha_fin - NEW.fecha_inicio), v_estadia_min;
        END IF;

        
        -- 2. VALIDAR TRASLAPE DE FECHAS
        
        SELECT COUNT(*) INTO v_traslapes
        FROM reserva
        WHERE id_alojamiento = NEW.id_alojamiento
          AND estado_reserva = 'CONFIRMADA'
          AND (NEW.id_reserva IS NULL OR id_reserva <> NEW.id_reserva)
          AND NEW.fecha_inicio < fecha_fin
          AND NEW.fecha_fin > fecha_inicio;

        IF v_traslapes > 0 THEN
            RAISE EXCEPTION 'Las fechas seleccionadas se traslapan con otra reservación confirmada para este alojamiento.';
        END IF;

    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql^;

-- Asociación del trigger a la tabla reserva
CREATE OR REPLACE TRIGGER trg_validar_reserva
BEFORE INSERT OR UPDATE ON reserva
FOR EACH ROW
EXECUTE FUNCTION fn_validar_reserva()^;



-- Procedimiento Almacenado 2

CREATE OR REPLACE PROCEDURE cancelarReserva(
	p_id_reserva INT
)
LANGUAGE plpgsql
AS $$
DECLARE
	-- variables locales 
	v_estado_actual VARCHAR;
	v_politica_cancelacion VARCHAR;
	v_fecha_inicio DATE;
	v_total_pagado INT;
	v_porcentaje_reembolso NUMERIC(5, 2);
	v_monto_reembolso INT;
	v_dias_anticipacion INTEGER;
	v_id_alojamiento INT;
BEGIN
	-- Se obtienen los datos de la reserva actual y el alojamiento 
	SELECT
		r.estado_reserva,
		r.fecha_inicio,
		r.precio_total,
		r.id_alojamiento,
		a.politica_cancel
	INTO
		v_estado_actual,
		v_fecha_inicio,
		v_total_pagado,
		v_id_alojamiento,
		v_politica_cancelacion
	FROM reserva r
	JOIN alojamiento a ON r.id_alojamiento = a.id_alojamiento
	WHERE r.id_reserva = p_id_reserva;

	-- Validaciones de los datos 
	IF v_estado_actual IS NULL THEN
		RAISE EXCEPTION 'La reserva con ID % no existe', p_id_reserva;
	END IF;
	IF v_estado_actual != 'CONFIRMADA' THEN
		RAISE EXCEPTION 'Solo se puede cancelar reservas con estado CONFIRMADA';
	END IF;

	-- Calcular los dias restantes 
	v_dias_anticipacion := v_fecha_inicio - CURRENT_DATE;

	-- Se calcula el reembolso 
	IF v_politica_cancelacion = 'flexible' THEN
		IF v_dias_anticipacion >= 1 THEN
			v_porcentaje_reembolso := 1.00;
		ELSE
			v_porcentaje_reembolso := 0.00;
		END IF;
	ELSIF v_politica_cancelacion = 'moderada' THEN
		IF v_dias_anticipacion >= 5 THEN
			v_porcentaje_reembolso := 1.00;
		ELSIF v_dias_anticipacion >= 2 THEN
			v_porcentaje_reembolso := 0.50;
		ELSE
			v_porcentaje_reembolso := 0.00;
		END IF;
	ELSIF v_politica_cancelacion = 'estricta' THEN
		IF v_dias_anticipacion >= 14 THEN
			v_porcentaje_reembolso := 1.00;
		ELSIF v_dias_anticipacion >= 7 THEN
			v_porcentaje_reembolso := 0.50;
		ELSE
			v_porcentaje_reembolso := 0.00;
		END IF;
	ELSE
		RAISE EXCEPTION 'Política de cancelación desconocida: %', v_politica_cancelacion;
	END IF;

	-- Se calcula el reembolso a aplicar 
	v_monto_reembolso := (v_total_pagado * v_porcentaje_reembolso)::INT;

	UPDATE reserva
	SET estado_reserva = 'CANCELADA'
	WHERE id_reserva = p_id_reserva;

	-- Registrar la devolución (monto negativo en pago) 
	IF v_monto_reembolso > 0 THEN
		INSERT INTO pago (id_reserva, monto_pago, fecha_pago, tipo_pago)
		VALUES (p_id_reserva, -v_monto_reembolso, NOW(), 'REEMBOLSO');
	END IF;

	-- Liberar las fechas del calendario --
	DELETE FROM calendario
	WHERE id_reserva = p_id_reserva;
END;
$$^;




-- Trigger 2

-- Crear la función del Trigger 
CREATE OR REPLACE FUNCTION fn_auditar_alojamiento()
RETURNS TRIGGER AS $$
BEGIN
    -- Validar si cambió el precio_noche
    IF NEW.precio_noche IS DISTINCT FROM OLD.precio_noche THEN
        INSERT INTO auditoria (
            id_alojamiento, 
            campo_cambiado, 
            valor_anterior, 
            valor_nuevo, 
            usuario_cambio, 
            fecha_cambio
        )
        VALUES (
            OLD.id_alojamiento, 
            'precio_noche', 
            OLD.precio_noche::VARCHAR, 
            NEW.precio_noche::VARCHAR, 
            COALESCE(NULLIF(current_setting('app.usuario', true), ''), 'SISTEMA'),
            NOW()
        );
    END IF;

    -- Validar si cambió la politica_cancel
    IF NEW.politica_cancel IS DISTINCT FROM OLD.politica_cancel THEN
        INSERT INTO auditoria (
            id_alojamiento, 
            campo_cambiado, 
            valor_anterior, 
            valor_nuevo, 
            usuario_cambio, 
            fecha_cambio
        )
        VALUES (
            OLD.id_alojamiento, 
            'politica_cancel', 
            OLD.politica_cancel, 
            NEW.politica_cancel, 
            COALESCE(NULLIF(current_setting('app.usuario', true), ''), 'SISTEMA'),
            NOW()
        );
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql^;

-- Crear el Trigger y vincularlo a la tabla 'alojamiento
CREATE OR REPLACE TRIGGER trg_auditoria_alojamiento
BEFORE UPDATE ON alojamiento
FOR EACH ROW
EXECUTE FUNCTION fn_auditar_alojamiento()^;




