package com.example.demo.Repository;

import com.example.demo.Entity.UsuarioEntity;
import com.example.demo.Repository.RowMapper.UsuarioRowMapper;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public class UsuarioRepository {
    private final JdbcClient jdbcClient;
    private final UsuarioRowMapper usuarioRowMapper = new UsuarioRowMapper();

    public UsuarioRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    //1. Obtener usuario por id
    public Optional<UsuarioEntity> findById(Integer idUsuario) {
        String sql  = """
            SELECT id_usuario, rut, nombre, correo, contrasena, rol
            FROM usuario
            WHERE id_usuario = ?
            """ ;
            return jdbcClient.sql(sql)
                    .param(idUsuario)
                    .query(usuarioRowMapper)
                    .optional();
    }

    //2. Buscar por rut
    public Optional<UsuarioEntity> findByRut(String rut) {
        String sql  = """
            SELECT id_usuario, rut, nombre, correo, contrasena, rol
            FROM usuario
            WHERE rut = ?
            """ ;
            return jdbcClient.sql(sql)
                    .param(rut)
                    .query(usuarioRowMapper)
                    .optional();
    }

    //3. Buscar por correo
    public Optional<UsuarioEntity> findByCorreo(String correo) {
        String sql  = """
            SELECT id_usuario, rut, nombre, correo, contrasena, rol
            FROM usuario
            WHERE correo = ?
            """ ;
            return jdbcClient.sql(sql)
                    .param(correo)
                    .query(usuarioRowMapper)
                    .optional();
    }

    //4. Verificar si existe un correo en la base de datos
    public boolean existsByCorreo(String correo) {
        String sql  = """
            SELECT COUNT(*) > 0
            FROM usuario
            WHERE correo = ?
            """ ;
            return jdbcClient.sql(sql)
                    .param(correo)
                    .query(Boolean.class)
                    .optional()
                    .orElse(false);
    }


    //5. Verificar si existe el rut en la bd
    public boolean existsByRut(String rut){
        String sql = """
            SELECT COUNT(*) > 0
            FROM usuario
            WHERE rut = ?
            """ ;

        return jdbcClient.sql(sql)
                .param(rut)
                .query(Boolean.class)
                .optional()
                .orElse(false);
    }

    //6. Crear un nuevo usuario
    public UsuarioEntity save(UsuarioEntity usuario) {
        String sql = """
            INSERT INTO usuarios(rut, nombre, correo, contrasena, rol)
            Values(?, ?, ?, ?, ?)
            RETURNING id_usuario
        """;
        Integer id_generado = jdbcClient.sql(sql)
                .param(usuario.getRut())
                .param(usuario.getNombre())
                .param(usuario.getCorreo())
                .param(usuario.getContrasena())
                .param(usuario.getRol())
                .query(Integer.class)
                .single();

        usuario.setId_usuario(id_generado);
        return usuario;
                
    }

    //7. Actualizar un usuario existente
    public UsuarioEntity update(UsuarioEntity cambios) {
        String sql  = """
            UPDATE usuario
            SET rut = ?, nombre = ?, correo = ?, contrasena = ?, rol = ?
            WHERE id_usuario = ?
        """;
        jdbcClient.sql(sql)
                .param(cambios.getRut())
                .param(cambios.getNombre())
                .param(cambios.getCorreo())
                .param(cambios.getContrasena())
                .param(cambios.getRol())
                .param(cambios.getId_usuario())
                .update();
        return cambios;
        
    }


    //8. Eliminar un usuario por id
    public void deleteById(Integer idUsuario){
        String sql = """
            DELETE FROM usuario
            WHERE id_usuario = ?
            """;
        jdbcClient.sql(sql)
                .param(idUsuario)
                .update();
    }


    //9. Obtener todos los usuarios
    public List<UsuarioEntity> findAll(){
        String sql = """
            SELECT id_usuario, rut, nombre, correo, contrasena, rol
            FROM usuario
        """;
        return jdbcClient.sql(sql)
                .query(usuarioRowMapper)
                .list();
    }

    //10. Listar por rol
    public List<UsuarioEntity> findByRol(String rol){
        String sql = """
            SELECT id_usuario, rut, nombre, correo, contrasena, rol
            FROM usuario
            WHERE rol = ?
        """;
        return jdbcClient.sql(sql)
                .param(rol)
                .query(usuarioRowMapper)
                .list();
    }




}