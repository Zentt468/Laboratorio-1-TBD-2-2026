package com.example.demo.Repository.RowMapper;

import com.example.demo.Entity.CalendarioEntity;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;


public class CalendarioRowMapper implements RowMapper<CalendarioEntity> {
    //Toma los datos de la base de datos y los mapea a un objeto CalendarioEntity
    @Override
    public CalendarioEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        return CalendarioEntity.builder()
                .id_calendario(rs.getInt("id_calendario"))
                .id_reserva(rs.getInt("id_reserva"))
                .fecha(rs.getDate("fecha").toLocalDate())
                .build();
    }
    
}
