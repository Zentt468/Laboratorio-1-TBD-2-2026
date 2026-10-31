package com.example.demo.Repository.RowMapper;


import com.example.demo.Entity.AlojamientoEntity;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AlojamientoRowMapper implements RowMapper<AlojamientoEntity> {
    //Toma los datos de la base de datos y los mapea a un objeto AlojamientoEntity
    @Override
    public AlojamientoEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        return AlojamientoEntity.builder()
                .id_alojamiento(rs.getInt("id_alojamiento"))
                .id_usuario(rs.getInt("id_usuario"))
                .titulo(rs.getString("titulo"))
                .tipo_alojamiento(rs.getString("tipo_alojamiento"))
                .direccion(rs.getString("direccion"))
                .capacidad(rs.getInt("capacidad"))
                .estadia_min(rs.getInt("estadia_min"))
                .precio_noche(rs.getInt("precio_noche"))
                .politica_cancel(rs.getString("politica_cancel"))
                .ciudad(rs.getString("ciudad"))
                .imagenUrl(rs.getString("imagen_url"))
                .build();
    }
}