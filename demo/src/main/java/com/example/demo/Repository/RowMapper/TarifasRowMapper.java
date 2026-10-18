package com.example.demo.Repository.RowMapper;

import com.example.demo.Entity.TarifasEntity;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TarifasRowMapper implements RowMapper<TarifasEntity> {
    //Toma los datos de la base de datos y los mapea a un objeto TarifasEntity
    @Override
    public TarifasEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        return TarifasEntity.builder()
                .id_tarifa(rs.getInt("id_tarifa"))
                .id_alojamiento(rs.getInt("id_alojamiento"))
                .temporada(rs.getString("temporada"))
                .fecha_inicio(rs.getTimestamp("fecha_inicio")) 
                .fecha_fin(rs.getTimestamp("fecha_fin"))
                .precio(rs.getDouble("precio"))
                .build();
    }
}
