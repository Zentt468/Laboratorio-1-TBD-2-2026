package com.example.demo.Entity;

import lombok.*;
import java.time.LocalDate;

@Data
@Builder 
@NoArgsConstructor 
@AllArgsConstructor
public class CalendarioEntity {

    private Integer id_calendario; //Llave primaria de calendario
    private Integer id_reserva; //Llave foranea de reserva
    private LocalDate fecha;

}