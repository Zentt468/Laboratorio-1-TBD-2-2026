package com.example.demo.DTO;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;

@Data
@Builder
//Procedimiento almacenado para confirmar una reserva
public class ConfirmarReservaRequest {
    Integer idUsuario;
    Integer idAlojamiento;
    LocalDate fechaInicio;
    LocalDate fechaFin;
    Integer nHuespedes;
    String tipoPago;
    
}
