package com.example.demo.Repository;

import com.example.demo.DTO.AuditoriaDTO;
import com.example.demo.Entity.AuditoriaEntity;
import com.example.demo.Repository.RowMapper.AuditoriaRowMapper;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class AuditoriaRepository {
    private final JdbcClient jdbcClient;
    private final AuditoriaRowMapper auditoriaRowMapper = new AuditoriaRowMapper();

    public AuditoriaRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<AuditoriaDTO> findAll() {
        String sql = """
            SELECT * FROM auditoria
            ORDER BY fecha_cambio DESC
        """;

        return jdbcClient.sql(sql)
                .query(AuditoriaDTO.class)
                .list();
    }

    // Obtener historial de una alojamiento
    public List<AuditoriaEntity> findByAlojamientoId(Integer idAlojamiento) {
        String sql = """
            SELECT * FROM auditoria
            WHERE id_alojamiento = :idAlojamiento
            ORDER BY fecha DESC
        """;

        return jdbcClient.sql(sql)
                .param("idAlojamiento", idAlojamiento)
                .query(auditoriaRowMapper)
                .list();
    }

    // Obtener historial por su id
    public Optional<AuditoriaEntity> findById(Integer idAuditoria) {
        String sql = """
            SELECT * FROM auditoria
            WHERE id_auditoria = :idAuditoria
        """;

        return jdbcClient.sql(sql)
                .param("idAuditoria", idAuditoria)
                .query(auditoriaRowMapper)
                .optional();
    }
}