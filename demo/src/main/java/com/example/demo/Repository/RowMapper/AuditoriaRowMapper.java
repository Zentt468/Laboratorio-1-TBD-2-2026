package com.example.demo.Repository.RowMapper;

import com.example.demo.Entity.AuditoriaEntity;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuditoriaRowMapper implements RowMapper<AuditoriaEntity> {
    //Toma los datos de la base de datos y los mapea a un objeto AuditoriaEntity
    @Override
    public AuditoriaEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        return AuditoriaEntity.builder()
                .id_auditoria(rs.getInt("id_auditoria"))
                .id_alojamiento(rs.getInt("id_alojamiento"))
                .campo_cambiado(rs.getString("campo_cambiado"))
                .valor_anterior(rs.getString("valor_anterior"))
                .valor_nuevo(rs.getString("valor_nuevo"))
                .usuario_cambio(rs.getString("usuario_cambio"))
                .fecha_cambio(rs.getDate("fecha_cambio").toLocalDate())
                .build();
    }
}
