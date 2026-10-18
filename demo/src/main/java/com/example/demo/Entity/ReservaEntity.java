package com.example.demo.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;
import java.util.Date;


@Data
@Builder 
@NoArgsConstructor 
@AllArgsConstructor
public class ReservaEntity {

    private Integer id_reserva; //Llave primaria de reserva
    private Integer id_usuario; //Llave foranea de usuario
    private Integer id_alojamiento; //Llave foranea de alojamiento
    private Integer n_huespedes;
    private Date fecha_inicio;
    private Date fecha_fin;
    private String estado_reserva;
    private Integer precio_total;
    
}