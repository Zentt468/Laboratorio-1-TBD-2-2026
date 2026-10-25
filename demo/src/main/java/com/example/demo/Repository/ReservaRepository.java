package com.example.demo.Repository;

import com.example.demo.Entity.ReservaEntity;
import com.example.demo.Repository.RowMapper.ReservaRowMapper;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class ReservaRepository {
    private final JdbcClient jdbcClient;
    private final ReservaRowMapper reservaRowMapper = new ReservaRowMapper();

    public ReservaRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<ReservaEntity> findAll(){
        String sql = """
            SELECT * FROM reserva
            ORDER BY fecha_inicio DESC
        """;

        return jdbcClient.sql(sql)
                .query(reservaRowMapper)
                .list();
    }

    public Optional<ReservaEntity> findById(Integer idReserva) {
        String sql = """
            SELECT * FROM reserva
            WHERE id_reserva = :idReserva
        """;

        return jdbcClient.sql(sql)
                .param("idReserva", idReserva)
                .query(reservaRowMapper)
                .optional();
    }

    public List<ReservaEntity> findByUsuario(Integer idUsuario) {
        String sql = """
            SELECT * FROM reserva
            WHERE id_usuario = :idUsuario
            ORDER BY fecha_inicio DESC
        """;

        return jdbcClient.sql(sql)
                .param("idUsuario", idUsuario)
                .query(reservaRowMapper)
                .list();
    }

    public List<ReservaEntity> findByAlojamiento(Integer idAlojamiento) {
        String sql = """
            SELECT * FROM reserva
            WHERE id_alojamiento = :idAlojamiento
            ORDER BY fecha_inicio DESC
        """;

        return jdbcClient.sql(sql)
                .param("idAlojamiento", idAlojamiento)
                .query(reservaRowMapper)
                .list();
    }

    public boolean existsByReservaId(Integer idReserva) {
        String sql = """
            SELECT COUNT(*) FROM reserva
            WHERE id_reserva = :idReserva
            """;
        
        Integer count = jdbcClient.sql(sql)
                .param("idReserva", idReserva)
                .query(Integer.class)
                .single();

        return count > 0;
    }

    public void save(ReservaEntity reserva) {
        String sql = """
            INSERT INTO reservas (
                id_usuario, id_alojamiento, n_huespedes,
                fecha_inicio, fecha_fin, estado_reserva, precio_total)
            Values(
                :idUsuario, :idAlojamiento, :nHuespedes,
                :fechaInicio, :fechaFin, :estadoReserva, :precioTotal)
                RETURNING id_reserva
        """;

        jdbcClient.sql(sql)
            .param("idUsuario", reserva.getId_usuario())
            .param("idAlojamiento", reserva.getId_alojamiento())
            .param("nHuespedes", reserva.getN_huespedes())
            .param("fechaInicio", reserva.getFecha_inicio())
            .param("fechaFin", reserva.getFecha_fin())
            .param("estadoReserva", reserva.getEstado_reserva())
            .param("precioTotal", reserva.getPrecio_total())
            .update();
    }

    public void delete(Integer idReserva) {
        String sql = """
            DELETE FROM reserva
            WHERE id_reserva = :idReserva
        """;

        jdbcClient.sql(sql)
                .param("idReserva", idReserva)
                .update();
    }

    //Procedimientos almacenados

    public Integer confirmarReserva(Integer idUsuario, Integer idAlojamiento,
                                LocalDate fechaInicio, LocalDate fechaFin,
                                Integer nHuespedes, String tipoPago) {
    String sql = """
        CALL sp_confirmar_reserva(
            :idUsuario, :idAlojamiento,
            CAST(:fechaInicio AS DATE), CAST(:fechaFin AS DATE),
            :nHuespedes, :tipoPago, NULL)
    """;

    // El parámetro INOUT p_id_reserva vuelve como una fila con una columna
    return jdbcClient.sql(sql)
            .param("idUsuario", idUsuario)
            .param("idAlojamiento", idAlojamiento)
            .param("fechaInicio", fechaInicio)
            .param("fechaFin", fechaFin)
            .param("nHuespedes", nHuespedes)
            .param("tipoPago", tipoPago)
            .query(Integer.class)
            .single();
    }

    public void cancelarReserva(Integer idReserva) {
    jdbcClient.sql("CALL cancelarReserva(:idReserva)")
            .param("idReserva", idReserva)
            .update();
    }
}