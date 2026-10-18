package com.example.demo.Repository;

import com.example.demo.Entity.ReservaEntity;
import com.example.demo.Repository.RowMapper.ReservaRowMapper;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

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

    public boolean canPostAReview(Integer idReserva, Integer idUsuario) {
        String sql = """
            SELECT COUNT(*)
            FROM reserva
            WHERE id_reserva = :idReserva
            AND id_usuario = :idUsuario
            AND (estado_reserva = 'CONFIRMADA')
        """;

        Integer count = jdbcClient.sql(sql)
                .param("idReserva", idReserva)
                .param("idUsuario", idUsuario)
                .query(Integer.class)
                .single();
        
        return count > 0;
    }

    public Integer save(ReservaEntity reserva) {
        String sql = """
            INSERT INTO reservas (
                id_usuario, id_alojamiento, n_huespedes,
                fecha_inicio, fecha_fin, estado_reserva, precio_total)
            Values(
                :idUsuario, :idAlojamiento, :nHuespedes,
                :fechaInicio, :fechaFin, :estadoReserva, :precioTotal)
                RETURNING id_reserva
        """;

        return jdbcClient.sql(sql)
                .param("idUsuario", reserva.getId_usuario())
                .param("idAlojamiento", reserva.getId_alojamiento())
                .param("nHuespedes", reserva.getN_huespedes())
                .param("fechaInicio", reserva.getFecha_inicio())
                .param("fechaFin", reserva.getFecha_fin())
                .param("estadoReserva", reserva.getEstado_reserva())
                .param("precioTotal", reserva.getPrecio_total())
                .query(Integer.class)
                .single();
    }

    public boolean delete(Integer idReserva) {
        String sql = """
            DELETE FROM reserva
            WHERE id_reserva = :idReserva
        """;

        int filasAfectadas = jdbcClient.sql(sql)
                .param("idReserva", idReserva)
                .update();
        return filasAfectadas > 0;
    }
}