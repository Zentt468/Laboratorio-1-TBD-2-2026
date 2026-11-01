package com.example.demo.Controller;

import com.example.demo.DTO.ReporteOcupacionDTO;
import com.example.demo.Service.ReporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
public class ReporteController {
    private final ReporteService reporteService;
    // Obtiene el reporte de ocupación
    @GetMapping("/ocupacion")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<ReporteOcupacionDTO>> getReporteOcupacion() {
        return ResponseEntity.ok(reporteService.obtenerReporte());
    }
}