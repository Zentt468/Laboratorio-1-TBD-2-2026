package com.example.demo.Repository;

import com.example.demo.Entity.PagoEntity;
import com.example.demo.Repository.RowMapper.PagoRowMapper;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public class PagoRepository {
    private final JdbcClient jdbcClient;
    private final PagoRowMapper pagoRowMapper = new PagoRowMapper();

    public PagoRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    //Obtener todos los pagos 
    public List<PagoEntity> findAll(){
        String sql = """
                SELECT * FROM pago
                """;
                return jdbcClient.sql(sql)
                        .query(pagoRowMapper)
                        .list();
    }

    //Buscar por id
    public Optional<PagoEntity> findById(Integer id) {
        String sql = """
            SELECT * FROM pago
            WHERE id_pago = :id
        """;

        return jdbcClient.sql(sql)
                .param("id", id)
                .query(pagoRowMapper)
                .optional();
    }

    //Buscar por id de reserva
    public List<PagoEntity> findByReserva(Integer idReserva) {
        String sql = """
            SELECT * FROM pago
            WHERE id_reserva = :idReserva
        """;

        return jdbcClient.sql(sql)
                .param("idReserva", idReserva)
                .query(pagoRowMapper)
                .list();

    }

    public void save(PagoEntity pago){
        String sql = """
                INSERT INTO pago (id_reserva, monto_pago, fecha_pago, tipo_pago)
                VALUES (:idReserva, :montoPago, :fechaPago, :tipoPago)
                """;
                jdbcClient.sql(sql)
                        .param("idReserva", pago.getId_reserva())
                        .param("montoPago", pago.getMonto_pago())
                        .param("fechaPago", pago.getFecha_pago())
                        .param("tipoPago", pago.getTipo_pago())
                        .update();
    }

    public int update(PagoEntity pago) {
        return jdbcClient.sql("""
                UPDATE pago
                SET id_reserva = :idReserva
                    monto_pago = :montoPago
                    fecha_pago = :fechaPago
                    tipo_pago = :tipoPago
                WHERE id_pago = :idPago
                """)
                .param("idReserva", pago.getId_reserva())
                .param("montoPago", pago.getMonto_pago())
                .param("fechaPago", pago.getFecha_pago())
                .param("tipoPago", pago.getTipo_pago())
                .update();
    }

    //Eliminar pago
    public void deleteById(Integer idPago){
        String sql = """
                DELETE FROM pago
                WHERE id_pago = :idPago
                """;
                jdbcClient.sql(sql)
                        .param("idPago", idPago)
                        .update();
    }

}