package com.example.demo.DTO;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReporteOcupacionDTO {
    private String ciudad;
    private String tipo_alojamiento;
    private Integer anio;
    private Integer mes;
    private Integer total_reservas;
    private Integer ingresos_totales;
}