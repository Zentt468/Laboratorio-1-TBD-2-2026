package com.example.demo.Repository.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import com.example.demo.Entity.ReservaEntity;
import org.springframework.jdbc.core.RowMapper;

public class ReservaRowMapper implements RowMapper<ReservaEntity> {
    //Toma los datos de la base de datos y los mapea a un objeto ReservaEntity
    @Override
    public ReservaEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        return ReservaEntity.builder()
                .id_reserva(rs.getInt("id_reserva"))
                .id_usuario(rs.getInt("id_usuario"))
                .id_alojamiento(rs.getInt("id_alojamiento"))
                .n_huespedes(rs.getInt("n_huespedes"))
                .fecha_inicio(rs.getDate("fecha_inicio").toLocalDate())
                .fecha_fin(rs.getDate("fecha_fin").toLocalDate())
                .estado_reserva(rs.getString("estado_reserva"))
                .precio_total(rs.getInt("precio_total"))
                .build();
    }
}
    