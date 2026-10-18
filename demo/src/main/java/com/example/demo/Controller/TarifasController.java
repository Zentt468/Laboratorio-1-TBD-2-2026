package com.example.demo.Controller;

import com.example.demo.Entity.TarifasEntity;
import com.example.demo.Service.TarifasService;
import com.example.demo.Entity.AlojamientoEntity;
import com.example.demo.Service.AlojamientoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tarifas")
@RequiredArgsConstructor
public class TarifasController {

    private final TarifasService tarifasService;
    private final AlojamientoService alojamientoService;

    //Obtener todas las tarifas
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TarifasEntity>> getAllTarifas() {
        List<TarifasEntity> tarifas = tarifasService.getAllTarifas();
        return new ResponseEntity<>(tarifas, HttpStatus.OK);
    }

    //Obtener tarifa por id
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getTarifasBetweenFechas(@RequestParam Date fechaInicio, @RequestParam Date fechaFin) {
        List<TarifasEntity> tarifas = tarifasService.getTarifasBetweenFechas(fechaInicio, fechaFin);
        //Se verifica si se encontraron tarifas para el rango de fechas especificado
        if (tarifas.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontraron tarifas entre las fechas: " + fechaInicio + " y " + fechaFin);
        }
        return ResponseEntity.ok(tarifas);
    }

    //Crear una nueva tarifa
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createTarifa(@RequestBody TarifasEntity tarifa) {
        TarifasEntity nuevaTarifa = tarifasService.createTarifa(tarifa);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaTarifa);
    }

    //Actualizar una tarifa existente
    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateTarifa(@RequestBody TarifasEntity tarifa) {
        //Se verifica si la tarifa existe
        Optional<TarifasEntity> tarifaExistente = tarifasService.getTarifaById(tarifa.getId_tarifa());
        if (!tarifaExistente.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tarifa no encontrada");
        }
        TarifasEntity tarifaActualizada = tarifasService.updateTarifa(tarifa);
        return ResponseEntity.ok(tarifaActualizada);
    }

    //Eliminar una tarifa por id
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteTarifaById(@PathVariable Integer id) {
        //Se verifica si la tarifa existe
        Optional<TarifasEntity> tarifa = tarifasService.getTarifaById(id);
        if (!tarifa.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tarifa no encontrada");
        }
        tarifasService.deleteTarifa(id);
        return ResponseEntity.ok("Tarifa eliminada exitosamente.");
    }


}