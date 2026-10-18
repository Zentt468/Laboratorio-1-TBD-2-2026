package com.example.demo.Repository.RowMapper;

import com.example.demo.Entity.UsuarioEntity;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;


public class UsuarioRowMapper implements RowMapper<UsuarioEntity> {
    //Toma los datos de la base de datos y los mapea a un objeto UsuarioEntity
    @Override 
    public UsuarioEntity mapRow(ResultSet rs, int rowNum) throws SQLException{
        return UsuarioEntity.builder()
                .id_usuario(rs.getInt("id_usuario"))
                .rut(rs.getString("rut"))
                .nombre(rs.getString("nombre"))
                .correo(rs.getString("correo"))
                .contrasena(rs.getString("contrasena"))
                .rol(rs.getString("rol"))
                .build();
    }
}