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
public class ResenaEntity {
    
    private Integer id_resena; //Llave primaria de resena
    private Integer id_alojamiento; //Llave foranea de alojamiento
    private Integer id_usuario; //Llave foranea de usuario
    private Integer id_reserva; //Llave foranea de reserva
    private String titulo_resena;
    private String comentario;
    private Integer calificacion;
    private Date fecha_resena;
    private String estado;
    
}