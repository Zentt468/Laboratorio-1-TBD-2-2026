package com.example.demo.DTO;


import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;



@Data 
@Builder 
@AllArgsConstructor 
@NoArgsConstructor 



public class ReservaDetalleDTO {
    //Datos de la reserva
    private Integer id_reserva; 
    private Integer id_usuario; 
    private Integer id_alojamiento; 
    private Integer n_huespedes;
    private LocalDate fecha_inicio;
    private LocalDate fecha_fin;
    private String estado_reserva;
    private Integer precio_total;

    //Datos del alojamiento
    private String titulo;
    private String ciudad;
    private String imagenUrl;
}
