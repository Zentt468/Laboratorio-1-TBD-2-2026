package com.example.demo.Controller;

import com.example.demo.Entity.AuditoriaEntity;
import com.example.demo.Service.AuditoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auditorias")
@RequiredArgsConstructor
public class AuditoriaController {

    private final AuditoriaService auditoriaService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AuditoriaEntity>> getAllAuditorias() {
        List<AuditoriaEntity> auditorias = auditoriaService.getAllAuditorias();
        return ResponseEntity.ok(auditorias);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AuditoriaEntity> getAuditoriaById(@PathVariable Integer id){
        return auditoriaService.getAuditoriaById(id).
                map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/alojamiento/{idAlojamiento}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AuditoriaEntity>> getAuditoriasByAlojamientoId(@PathVariable Integer idAlojamiento) {
        List<AuditoriaEntity> auditorias = auditoriaService.getAuditoriaByAlojamientoId(idAlojamiento);
        return ResponseEntity.ok(auditorias);
    }
}