package com.example.demo.Entity;

import java.util.Date;
import lombok.*;

@Data
@Builder
@NoArgsConstructor 
@AllArgsConstructor 
public class TarifasEntity {

    private Integer id_tarifa; //Llave primaria de tarifa
    private Integer id_alojamiento; //Llave foranea de alojamiento
    private String temporada;
    private Date fecha_inicio;
    private Date fecha_fin;
    private Double precio;
}
