package com.example.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
//Procedimiento almacenado para confirmar una reserva
public class ConfirmarReservaRequest {
    Integer idUsuario;
    Integer idAlojamiento;
    LocalDate fechaInicio;
    LocalDate fechaFin;
    Integer cantidadHuespedes;
    String tipoPago;
    
}
