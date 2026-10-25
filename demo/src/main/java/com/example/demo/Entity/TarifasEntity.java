package com.example.demo.Entity;

import java.time.LocalDate;
import lombok.*;

@Data
@Builder
@NoArgsConstructor 
@AllArgsConstructor 
public class TarifasEntity {

    private Integer id_tarifa; //Llave primaria de tarifa
    private Integer id_alojamiento; //Llave foranea de alojamiento
    private String temporada;
    private LocalDate fecha_inicio;
    private LocalDate fecha_fin;
    private Double precio;
}
