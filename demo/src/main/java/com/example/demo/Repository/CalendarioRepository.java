package com.example.demo.Repository;

import com.example.demo.Entity.CalendarioEntity;
import com.example.demo.Repository.RowMapper.CalendarioRowMapper;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.time.LocalDate;
import java.util.List;

@Repository
public class CalendarioRepository {

    private final JdbcClient jdbcClient;
    private final CalendarioRowMapper calendarioRowMapper = new CalendarioRowMapper();

    public CalendarioRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    //Obtener todos los calendarios
    public List<CalendarioEntity> findAll() {
    String sql = """
            SELECT * FROM calendario
            ORDER BY fecha ASC
    
        """;

        return jdbcClient.sql(sql)
                .query(calendarioRowMapper)
                .list();
    }

    // Obtener calendario por id
    public Optional<CalendarioEntity> findById(Integer idCalendario) {
        String sql = """
            SELECT * FROM calendario
            WHERE id_calendario = :idCalendario
        """;

        return jdbcClient.sql(sql)
                .param("idCalendario", idCalendario)
                .query(calendarioRowMapper)
                .optional();
    }

    //Obtener calendario por id de reserva
    public List<CalendarioEntity> findByReservaId(Integer idReserva) {
        String sql = """
            SELECT * FROM calendario
            WHERE id_reserva = :idReserva
        """;

        return jdbcClient.sql(sql)
                .param("idReserva", idReserva)
                .query(calendarioRowMapper)
                .list();
    }

    //Obtner calendario por fecha
    public Optional<CalendarioEntity> findByFecha(LocalDate fecha) {
        String sql = """
            SELECT * FROM calendario
            WHERE fecha = :fecha
        """;

        return jdbcClient.sql(sql)
                .param("fecha", fecha)
                .query(calendarioRowMapper)
                .optional();
    }

    //Crear un nuevo calendario
    public void save(CalendarioEntity calendario) {
        String sql = """
            INSERT INTO calendario (id_reserva, fecha)
            VALUES (:idReserva, :fecha)
        """;

        jdbcClient.sql(sql)
                .param("idReserva", calendario.getId_reserva())
                .param("fecha", calendario.getFecha())
                .update();
    }

    //Eliminar un calendario por id
    public void deleteById(Integer idCalendario) {
        String sql = """
            DELETE FROM calendario
            WHERE id_calendario = :idCalendario
        """;

        jdbcClient.sql(sql)
                .param("idCalendario", idCalendario)
                .update();
    }
 
}
