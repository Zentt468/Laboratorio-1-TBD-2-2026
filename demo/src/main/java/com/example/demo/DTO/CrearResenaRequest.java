package com.example.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrearResenaRequest {
    private Integer idReserva;
    private String tituloResena;
    private String comentario;
    private Integer calificacion; // 1 a 5
}