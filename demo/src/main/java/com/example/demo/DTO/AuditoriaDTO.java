package com.example.demo.DTO;

import java.time.LocalDateTime;

public class AuditoriaDTO {
    private Integer idAuditoria;
    private String tabla;
    private String operacion; // INSERT, UPDATE, DELETE
    private String usuario;
    private LocalDateTime fecha;
    private String detalle;
}