package com.example.demo.Entity;

import lombok.*;

@Data
@Builder 
@NoArgsConstructor 
@AllArgsConstructor
public class AlojamientoEntity{

    private Integer id_alojamiento; //Llave primaria de alojamiento
    private Integer id_usuario; //Llave foranea de usuario
    private String titulo;
    private String tipo_alojamiento;
    private String direccion;
    private Integer capacidad;
    private Integer estadia_min;
    private Integer precio_noche;
    private String politica_cancel;
    private String ciudad;
}