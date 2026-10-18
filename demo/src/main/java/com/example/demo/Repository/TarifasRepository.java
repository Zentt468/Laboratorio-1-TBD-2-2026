package com.example.demo.Repository;

import com.example.demo.Entity.TarifasEntity;
import com.example.demo.Repository.RowMapper.TarifasRowMapper;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TarifasRepository {
    private final JdbcClient jdbcClient;

    private final TarifasRowMapper tarifasRowMapper = new TarifasRowMapper();

    public TarifasRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    //1. Obtener tarifa por id
    public Optional<TarifasEntity> findById(int id_tarifa){
        String sql = """
            SELECT id_tarifa, id_alojamiento, temporada, fecha_inicio, fecha_fin, precio
            FROM tarifas
            WHERE id_tarifa = :id_tarifa
        """;

        return jdbcClient.sql(sql)
                .param("id_tarifa", id_tarifa)
                .query(tarifasRowMapper)
                .optional();
    }
    //2. Obtener todas las tarifas
    public List<TarifasEntity> findAll(){
        String sql = """
            SELECT id_tarifa, id_alojamiento, temporada, fecha_inicio, fecha_fin, precio
            FROM tarifas
        """;
        return jdbcClient.sql(sql)
                .query(tarifasRowMapper)
                .list();
    }

    //3. Obtener tarifas por temporada
    public List<TarifasEntity> findByTemporada(String temporada){
        String sql = """
            SELECT id_tarifa, id_alojamiento, temporada, fecha_inicio, fecha_fin, precio
            FROM tarifas
            WHERE temporada = :temporada
        """;

        return jdbcClient.sql(sql)
                .param("temporada", temporada)
                .query(tarifasRowMapper)
                .list();
    }

    //4. Obtener tarifa por temporada y alojamiento
    public Optional<TarifasEntity> findByTemporadaAndAlojamiento(String temporada, int id_alojamiento){
        String sql = """
            SELECT id_tarifa, id_alojamiento, temporada, fecha_inicio, fecha_fin, precio
            FROM tarifas
            WHERE temporada = :temporada AND id_alojamiento = :id_alojamiento
        """;

        return jdbcClient.sql(sql)
                .param("temporada", temporada)
                .param("id_alojamiento", id_alojamiento)
                .query(tarifasRowMapper)
                .optional();
    }

    //5. Obtener tarifas entre fechas
    public List<TarifasEntity> findByFechaBetween(String fecha_inicio, String fecha_fin){
        String sql = """
            SELECT id_tarifa, id_alojamiento, temporada, fecha_inicio, fecha_fin, precio
            FROM tarifas
            WHERE fecha_inicio >= :fecha_inicio AND fecha_fin <= :fecha_fin
        """;

        return jdbcClient.sql(sql)
                .param("fecha_inicio", fecha_inicio)
                .param("fecha_fin", fecha_fin)
                .query(tarifasRowMapper)
                .list();
    }

    //6. Crear una nueva tarifa
    public TarifasEntity save(TarifasEntity tarifa) {
        String sql = """
            INSERT INTO tarifas(id_alojamiento, temporada, fecha_inicio, fecha_fin, precio)
            Values(?, ?, ?, ?, ?)
            RETURNING id_tarifa
        """;
        Integer id_generado = jdbcClient.sql(sql)
                .param(tarifa.getId_alojamiento())
                .param(tarifa.getTemporada())
                .param(tarifa.getFecha_inicio())
                .param(tarifa.getFecha_fin())
                .param(tarifa.getPrecio())
                .query(Integer.class)
                .single();

        tarifa.setId_tarifa(id_generado);
        return tarifa;  
    }

    //7. Actualizar una tarifa existente
    public TarifasEntity update(TarifasEntity cambios){
        String sql = """
                UPDATE tarifas
                SET id_tarifa =?, id_alojamiento = ?, temporada = ?, fecha_inicio = ?, fecha_fin = ?, precio = ?
                WHERE id_tarifa = ?
            """;

        jdbcClient.sql(sql)
                .param(cambios.getId_tarifa())
                .param(cambios.getId_alojamiento())
                .param(cambios.getTemporada())
                .param(cambios.getFecha_inicio())
                .param(cambios.getFecha_fin())
                .param(cambios.getPrecio())
                .param(cambios.getId_tarifa())
                .update();
        return cambios;
    }


    //8. Eliminar una tarifa por id
    public void deleteById(int id_tarifa){

        String sql = """
            DELETE FROM tarifas
            WHERE id_tarifa = :id_tarifa
        """;

        jdbcClient.sql(sql)
                .param("id_tarifa", id_tarifa)
                .update();
    }
    




}
