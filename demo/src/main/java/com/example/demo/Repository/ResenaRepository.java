package com.example.demo.Repository;

import com.example.demo.DTO.DatosReservaDTO;
import com.example.demo.Entity.ResenaEntity;
import com.example.demo.Repository.RowMapper.ResenaRowMapper;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public class ResenaRepository {
    private final JdbcClient jdbcClient;
    private final ResenaRowMapper resenaRowMapper = new ResenaRowMapper();

    public ResenaRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    //Obtener todas las reseñas
    public List<ResenaEntity> findAll(){
        String sql = """
                SELECT * FROM resena
                """;
                return jdbcClient.sql(sql)
                        .query(resenaRowMapper)
                        .list();
    }
    //Buscar por id
    public Optional<ResenaEntity> findById(Integer id) {
        String sql = """
            SELECT * FROM resena
            WHERE id_resena = :id
        """;

        return jdbcClient.sql(sql)
                .param("id", id)
                .query(resenaRowMapper)
                .optional();
    }
    //Buscar por id de alojamiento
    public List<ResenaEntity> findByAlojamiento(Integer idAlojamiento) {
        String sql = """
            SELECT * FROM resena
            WHERE id_alojamiento = :idAlojamiento
        """;

        return jdbcClient.sql(sql)
                .param("idAlojamiento", idAlojamiento)
                .query(resenaRowMapper)
                .list();

    }
    //Buscar por id de usuario
    public List<ResenaEntity> findByUsuario(Integer idUsuario) {
        String sql = """
            SELECT * FROM resena
            WHERE id_usuario = :idUsuario
        """;

        return jdbcClient.sql(sql)
                .param("idUsuario", idUsuario)
                .query(resenaRowMapper)
                .list();

    }
    //Guardar reseña
    public void save(ResenaEntity resena){
        String sql = """
                INSERT INTO resena (id_alojamiento, id_usuario, id_reserva, titulo_resena, comentario, calificacion, fecha_resena, estado)
                VALUES (:idAlojamiento, :idUsuario, :idReserva, :tituloResena, :comentario, :calificacion, :fechaResena, :estado)
                """;
                jdbcClient.sql(sql)
                        .param("idAlojamiento", resena.getId_alojamiento())
                        .param("idUsuario", resena.getId_usuario())
                        .param("idReserva", resena.getId_reserva())
                        .param("tituloResena", resena.getTitulo_resena())
                        .param("comentario", resena.getComentario())
                        .param("calificacion", resena.getCalificacion())
                        .param("fechaResena", resena.getFecha_resena())
                        .param("estado", resena.getEstado())
                        .update();
    }
    //Actualizar reseña
    public int update(ResenaEntity resena) {
        return jdbcClient.sql("""
                UPDATE resena
                SET titulo_resena = :tituloResena,
                    comentario    = :comentario,
                    calificacion  = :calificacion,
                    estado        = :estado
                WHERE id_resena = :idResena
                """)
                .param("tituloResena", resena.getTitulo_resena())
                .param("comentario", resena.getComentario())
                .param("calificacion", resena.getCalificacion())
                .param("estado", resena.getEstado())
                .param("idResena", resena.getId_resena())
                .update();
    }

    //Eliminar reseña
    public void deleteById(Integer idResena){
        String sql = """
                DELETE FROM resena
                WHERE id_resena = :idResena
                """;
                jdbcClient.sql(sql)
                        .param("idResena", idResena)
                        .update();
    }

    // Obtener promedio de calificación de un alojamiento
    public Double getPromedioByAlojamientoId(Integer idAlojamiento) {
        String sql = "SELECT COALESCE(AVG(calificacion), 0.0) FROM resena WHERE id_alojamiento = :idAlojamiento";
        return jdbcClient.sql(sql)
                .param("idAlojamiento", idAlojamiento)
                .query(Double.class)
                .single();
    }

    // Obtener los datos necesarios de la reserva para validar la regla de negocio
    public Optional<DatosReservaDTO> findDatosReservaParaResenar(Integer idReserva) {
        String sql = """
            SELECT 
                id_usuario, 
                id_alojamiento, 
                estado_reserva AS estado
            FROM reserva
            WHERE id_reserva = :idReserva
        """;

        return jdbcClient.sql(sql)
                .param("idReserva", idReserva)
                .query(DatosReservaDTO.class)
                .optional();
    }

    public boolean existsByIdReserva(Integer idReserva) {
        String sql = "SELECT COUNT(*) FROM resena WHERE id_reserva = :idReserva";
        Integer total = jdbcClient.sql(sql)
                .param("idReserva", idReserva)
                .query(Integer.class)
                .single();
        return total != null && total > 0;
    }

    public Optional<ResenaEntity> findByIdReserva(Integer idReserva) {
        String sql = "SELECT * FROM resena WHERE id_reserva = :idReserva";
        return jdbcClient.sql(sql)
                .param("idReserva", idReserva)
                .query(ResenaEntity.class)
                .optional();
    }
}