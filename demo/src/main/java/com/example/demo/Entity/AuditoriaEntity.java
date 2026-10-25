package com.example.demo.Entity;

import lombok.*;
import java.time.LocalDate;
@Data
@Builder 
@NoArgsConstructor 
@AllArgsConstructor
public class AuditoriaEntity{

    private Integer id_auditoria; //Llave primaria de auditoria
    private Integer id_alojamiento; //Llave foranea de alojamiento
    private String campo_cambiado;
    private String valor_anterior;
    private String valor_nuevo;
    private String usuario_cambio;
    private LocalDate fecha_cambio;
}