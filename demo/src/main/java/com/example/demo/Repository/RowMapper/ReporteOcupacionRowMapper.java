package com.example.demo.Repository.RowMapper;

import com.example.demo.DTO.ReporteOcupacionDTO;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReporteOcupacionRowMapper implements RowMapper<ReporteOcupacionDTO> {
    //Toma los datos de la base de datos y los mapea a un objeto ReporteOcupacionDTO
    @Override
    public ReporteOcupacionDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        return ReporteOcupacionDTO.builder()
                .ciudad(rs.getString("ciudad"))
                .tipo_alojamiento(rs.getString("tipo_alojamiento"))
                .anio(rs.getInt("anio"))
                .mes(rs.getInt("mes"))
                .total_reservas(rs.getInt("total_reservas"))
                .ingresos_totales(rs.getInt("ingresos_totales"))
                .build();
    }
}
