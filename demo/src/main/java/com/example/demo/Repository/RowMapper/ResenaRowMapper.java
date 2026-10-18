package com.example.demo.Repository.RowMapper;

import com.example.demo.Entity.ResenaEntity;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ResenaRowMapper implements RowMapper<ResenaEntity> {
    //Toma los datos de la base de datos y los mapea a un objeto ResenaEntity
    @Override
    public ResenaEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        return ResenaEntity.builder()
                .id_resena(rs.getInt("id_resena"))
                .id_alojamiento(rs.getInt("id_alojamiento"))
                .id_usuario(rs.getInt("id_usuario"))
                .id_reserva(rs.getInt("id_reserva"))
                .titulo_resena(rs.getString("titulo_resena"))
                .comentario(rs.getString("comentario"))
                .calificacion(rs.getInt("calificacion"))
                .fecha_resena(rs.getTimestamp("fecha_resena"))
                .estado(rs.getString("estado"))
                .build();
    }
}
