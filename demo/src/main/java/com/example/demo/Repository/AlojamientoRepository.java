package com.example.demo.Repository;

import com.example.demo.Entity.UsuarioEntity;
import com.example.demo.Repository.RowMapper.UsuarioRowMapper;
import com.example.demo.Repository.RowMapper.AlojamientoRowMapper;

import com.example.demo.Entity.AlojamientoEntity;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository 
public class AlojamientoRepository{

    private final JdbcClient jdbcClient;
    private final AlojamientoRowMapper alojamientoRowMapper = new AlojamientoRowMapper();
    //Constructor de la clase para las inyecciones de dependencias
    public AlojamientoRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    //Obtener todos los alojamientos
    public List<AlojamientoEntity> findAll(){
        String sql = """
                SELECT * FROM alojamiento
                """;
                return jdbcClient.sql(sql)
                        .query(alojamientoRowMapper)
                        .list();
    }

    //Obtener alojamiento por id
    public Optional<AlojamientoEntity> findById(Integer idAlojamiento){
        String sql = """
                SELECT * FROM alojamiento
                WHERE id_alojamiento = :idAlojamiento
                """;
                return jdbcClient.sql(sql)
                        .param("idAlojamiento", idAlojamiento)
                        .query(alojamientoRowMapper)
                        .optional();
    }

    //Obtener alojamiento por id de usuario
    public List<AlojamientoEntity> findByUsuarioId(Integer idUsuario){
        String sql = """
                SELECT * FROM alojamiento
                WHERE id_usuario = :idUsuario
                """;
                return jdbcClient.sql(sql)
                        .param("idUsuario", idUsuario)
                        .query(alojamientoRowMapper)
                        .list();
    }

    //Obtener por tipo de alojamiento
    public List<AlojamientoEntity> findByTipoAlojamiento(String tipo){
        String sql = """
                SELECT * FROM alojamiento
                WHERE tipo_alojamiento = :tipo
                """;
                return jdbcClient.sql(sql)
                        .param("tipo", tipo)
                        .query(alojamientoRowMapper)
                        .list();
    }

    //Obtener por capacidad
    public List<AlojamientoEntity> findByCapacidad(Integer capacidad){
        String sql = """
                SELECT * FROM alojamiento
                WHERE capacidad >= :capacidad
                """;
                return jdbcClient.sql(sql)
                        .param("capacidad", capacidad)
                        .query(alojamientoRowMapper)
                        .list();
    }

    //Obtener por precio menor por noche
    public List<AlojamientoEntity> findByPrecioNocheMenor(Integer precio){
        String sql = """
                SELECT * FROM alojamiento
                WHERE precio_noche <= :precio
                """;
                return jdbcClient.sql(sql)
                        .param("precio", precio)
                        .query(alojamientoRowMapper)
                        .list();
    }

    //Obtener por ciudad
    public List<AlojamientoEntity> findByCiudad(String ciudad){
        String sql = """
                SELECT * FROM alojamiento
                WHERE ciudad = :ciudad
                """;
                return jdbcClient.sql(sql)
                        .param("ciudad", ciudad)
                        .query(alojamientoRowMapper)
                        .list();
    }

    //Existe alojamiento por id
    public boolean existsById(Integer idAlojamiento){
        String sql = """
                SELECT COUNT(*) > 0
                FROM alojamiento
                WHERE id_alojamiento = :idAlojamiento
                """;
                return jdbcClient.sql(sql)
                        .param("idAlojamiento", idAlojamiento)
                        //El resultado esperado es un booleano
                        .query(Boolean.class)
                        //Obtiene un solo resultado, ya que es una consulta de existencia
                        .single();
    }

    //Guardar alojamiento
    public void save(AlojamientoEntity alojamiento){
        String sql = """
                INSERT INTO alojamiento (id_usuario, titulo, tipo_alojamiento, direccion, capacidad, estadia_min, precio_noche, politica_cancel, ciudad)
                VALUES (:idUsuario, :titulo, :tipoAlojamiento, :direccion, :capacidad, :estadiaMin, :precioNoche, :politicaCancel, :ciudad)
                """;
                jdbcClient.sql(sql)
                        .param("idUsuario", alojamiento.getId_usuario())
                        .param("titulo", alojamiento.getTitulo())
                        .param("tipoAlojamiento", alojamiento.getTipo_alojamiento())
                        .param("direccion", alojamiento.getDireccion())
                        .param("capacidad", alojamiento.getCapacidad())
                        .param("estadiaMin", alojamiento.getEstadia_min())
                        .param("precioNoche", alojamiento.getPrecio_noche())
                        .param("politicaCancel", alojamiento.getPolitica_cancel())
                        .param("ciudad", alojamiento.getCiudad())
                        .update();
    }

    //Actualizar alojamiento
    public void update(AlojamientoEntity alojamiento){
        String sql = """
                UPDATE alojamiento
                SET id_usuario = :idUsuario,
                    titulo = :titulo,
                    tipo_alojamiento = :tipoAlojamiento,
                    direccion = :direccion,
                    capacidad = :capacidad,
                    estadia_min = :estadiaMin,
                    precio_noche = :precioNoche,
                    politica_cancel = :politicaCancel,
                    ciudad = :ciudad
                WHERE id_alojamiento = :idAlojamiento
                """;
                jdbcClient.sql(sql)
                        .param("idUsuario", alojamiento.getId_usuario())
                        .param("titulo", alojamiento.getTitulo())
                        .param("tipoAlojamiento", alojamiento.getTipo_alojamiento())
                        .param("direccion", alojamiento.getDireccion())
                        .param("capacidad", alojamiento.getCapacidad())
                        .param("estadiaMin", alojamiento.getEstadia_min())
                        .param("precioNoche", alojamiento.getPrecio_noche())
                        .param("politicaCancel", alojamiento.getPolitica_cancel())
                        .param("ciudad", alojamiento.getCiudad())
                        .param("idAlojamiento", alojamiento.getId_alojamiento())
                        .update();
    }

    //Eliminar alojamiento
    public void deleteById(Integer idAlojamiento){
        String sql = """
                DELETE FROM alojamiento
                WHERE id_alojamiento = :idAlojamiento
                """;
                jdbcClient.sql(sql)
                        .param("idAlojamiento", idAlojamiento)
                        .update();
    }


}