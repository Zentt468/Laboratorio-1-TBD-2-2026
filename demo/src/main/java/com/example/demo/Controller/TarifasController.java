package com.example.demo.Controller;

import com.example.demo.Entity.TarifasEntity;
import com.example.demo.Service.TarifasService;
import com.example.demo.Service.UsuarioService;
import com.example.demo.Entity.AlojamientoEntity;
import com.example.demo.Service.AlojamientoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tarifas")
@RequiredArgsConstructor
public class TarifasController {

    private final TarifasService tarifasService;
    private final AlojamientoService alojamientoService;
    private final UsuarioService usuarioService;

    //Obtener todas las tarifas
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ANFITRION') or hasRole('ROLE_HUESPED')")
    public ResponseEntity<List<TarifasEntity>> getAllTarifas() {
        List<TarifasEntity> tarifas = tarifasService.getAllTarifas();
        return new ResponseEntity<>(tarifas, HttpStatus.OK);
    }

    //Obtener tarifa por id
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getTarifaById(@PathVariable Integer id) {
        //Se verifica si la tarifa existe
        Optional<TarifasEntity> tarifa = tarifasService.getTarifaById(id);
        if (tarifa.isPresent()) {
            return ResponseEntity.ok(tarifa.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tarifa no encontrada");
        }
    }

    //Obtener tarifas por id de alojamiento
    @GetMapping("/alojamiento/{idAlojamiento}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getTarifasByAlojamientoId(@PathVariable Integer idAlojamiento) {
        //Se verifica si el alojamiento existe
        Optional<AlojamientoEntity> alojamiento = alojamientoService.getAlojamientoById(idAlojamiento);
        if (!alojamiento.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Alojamiento no encontrado");
        }
        //Se verifica si el alojamiento cuenta con tarifas asociadas
        List<TarifasEntity> tarifas = tarifasService.getTarifasByAlojamientoId(idAlojamiento);
        if (tarifas.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontraron tarifas para el alojamiento con id: " + idAlojamiento);
        }
        return ResponseEntity.ok(tarifas);
    }

    //Obtener tarifas por temporada
    @GetMapping("/temporada/{temporada}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ANFITRION') or hasRole('ROLE_HUESPED')")
    public ResponseEntity<?> getTarifasByTemporada(@PathVariable String temporada) {
        List<TarifasEntity> tarifas = tarifasService.getTarifasByTemporada(temporada);
        //Se verifica si se encontraron tarifas para la temporada especificada
        if (tarifas.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontraron tarifas para la temporada: " + temporada);
        }
        return ResponseEntity.ok(tarifas);
    }

    //Obtener tarifas por temporada y alojamiento
    @GetMapping("/temporada/{temporada}/alojamiento/{idAlojamiento}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ANFITRION') or hasRole('ROLE_HUESPED')")
    public ResponseEntity<?> getTarifasByTemporadaAndAlojamiento(@PathVariable String temporada, @PathVariable Integer idAlojamiento) {
        //Se verifica si el alojamiento existe
        Optional<AlojamientoEntity> alojamiento = alojamientoService.getAlojamientoById(idAlojamiento);
        if (!alojamiento.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Alojamiento no encontrado");
        }
        //Se verifica si se encontraron tarifas para la temporada y alojamiento especificados
        Optional<TarifasEntity> tarifa = tarifasService.getTarifasByTemporadaAndAlojamiento(temporada, idAlojamiento);
        if (!tarifa.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontraron tarifas para la temporada: " + temporada + " y el alojamiento con id: " + idAlojamiento);
        }
        return ResponseEntity.ok(tarifa.get());
    }

    //Obtener tarifas entre fechas
    @GetMapping("/fechas")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ANFITRION') or hasRole('ROLE_HUESPED')")
    public ResponseEntity<?> getTarifasBetweenFechas(@RequestParam LocalDate fechaInicio, @RequestParam LocalDate fechaFin) {
        List<TarifasEntity> tarifas = tarifasService.getTarifasBetweenFechas(fechaInicio, fechaFin);
        //Se verifica si se encontraron tarifas para el rango de fechas especificado
        if (tarifas.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontraron tarifas entre las fechas: " + fechaInicio + " y " + fechaFin);
        }
        return ResponseEntity.ok(tarifas);
    }

    //Crear una nueva tarifa
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ANFITRION')")
    public ResponseEntity<?> createTarifa(@RequestBody TarifasEntity tarifa, @AuthenticationPrincipal Jwt jwt) {
        if (!puedeGestionar(tarifa.getId_alojamiento(), jwt)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Solo puedes crear tarifas para tus alojamientos");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(tarifasService.createTarifa(tarifa));
    }
    //Actualizar una tarifa existente
    @PutMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ANFITRION')")
    public ResponseEntity<?> updateTarifa(@RequestBody TarifasEntity tarifa, @AuthenticationPrincipal Jwt jwt) {
        Optional<TarifasEntity> existente = tarifasService.getTarifaById(tarifa.getId_tarifa());
        if (existente.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tarifa no encontrada");
        }
        if (!puedeGestionar(existente.get().getId_alojamiento(), jwt)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Solo puedes modificar tarifas de tus alojamientos");
        }
        tarifa.setId_alojamiento(existente.get().getId_alojamiento());
        return ResponseEntity.ok(tarifasService.updateTarifa(tarifa));
    }
    //Eliminar una tarifa por id
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ANFITRION')")
    public ResponseEntity<?> deleteTarifaById(@PathVariable Integer id, @AuthenticationPrincipal Jwt jwt) {
        Optional<TarifasEntity> tarifa = tarifasService.getTarifaById(id);
        if (tarifa.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tarifa no encontrada");
        }
        if (!puedeGestionar(tarifa.get().getId_alojamiento(), jwt)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Solo puedes eliminar tarifas de tus alojamientos");
        }
        tarifasService.deleteTarifa(id);
        return ResponseEntity.ok("Tarifa eliminada exitosamente.");
    }

    private boolean puedeGestionar(Integer idAlojamiento, Jwt jwt) {
        if (usuarioService.esAdmin(jwt)) {
            return true;
        }
        Optional<AlojamientoEntity> alojamiento = alojamientoService.getAlojamientoById(idAlojamiento);
        if (alojamiento.isEmpty()) {
            return false;
        }
        return alojamiento.get().getId_usuario().equals(usuarioService.getIdUsuarioActual(jwt));
    }


}