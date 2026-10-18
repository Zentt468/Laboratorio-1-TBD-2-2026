package com.example.demo.Repository.RowMapper;

import com.example.demo.Entity.PagoEntity;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PagoRowMapper implements RowMapper<PagoEntity> {
    //Toma los datos de la base de datos y los mapea a un objeto PagoEntity
    @Override
    public PagoEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        return PagoEntity.builder()
                .id_pago(rs.getInt("id"))
                .id_reserva(rs.getInt("id_reserva"))
                .monto_pago(rs.getInt("monto_pago"))
                .fecha_pago(rs.getTimestamp("fecha_pago"))
                .tipo_pago(rs.getString("tipo_pago"))
                .build();
    }
    
}
