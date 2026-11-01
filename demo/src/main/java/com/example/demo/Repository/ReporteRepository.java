package com.example.demo.Repository;

import com.example.demo.DTO.ReporteOcupacionDTO;
import com.example.demo.Repository.RowMapper.ReporteOcupacionRowMapper;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class ReporteRepository {
    private final JdbcClient jdbcClient;
    private final ReporteOcupacionRowMapper mapper = new ReporteOcupacionRowMapper();

    public ReporteRepository(JdbcClient jdbcClient) { this.jdbcClient = jdbcClient; }

    public List<ReporteOcupacionDTO> getReporte() {
        String sql = "SELECT * FROM mv_ocupacion_ingresos ORDER BY anio DESC, mes DESC";
        return jdbcClient.sql(sql).query(mapper).list();
    }

    public void refrescarVista() {
       jdbcClient.sql("REFRESH MATERIALIZED VIEW mv_ocupacion_ingresos").update();
    }
}