package com.example.demo.Controller;

import com.example.demo.Entity.CalendarioEntity;
import com.example.demo.Service.CalendarioService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;

@RestController 
@RequestMapping("/api/calendario")
@RequiredArgsConstructor
public class CalendarioController {

    private final CalendarioService calendarioService;
    @PreAuthorize ("hasRole('ROLE_ADMIN') or hasRole('ROLE_ANFITRION') or hasRole('ROLE_HUESPED')")
    //Obtener todos los calendarios
    @GetMapping
    public ResponseEntity<List<CalendarioEntity>> getAllCalendarios() {
        List<CalendarioEntity> calendarios = calendarioService.findAll();
        return new ResponseEntity<>(calendarios, HttpStatus.OK);
    }

    //Obtener calendario por id
    @GetMapping("/{id}")
    @PreAuthorize ("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getCalendarioById(@PathVariable Integer id) {
        Optional<CalendarioEntity> calendario = calendarioService.findById(id);
        if (calendario.isPresent()) {
            return ResponseEntity.ok(calendario.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Fecha no encontrado");
        }
    }

    //Obtener calendario por id de reserva
    @GetMapping("/reserva/{idReserva}")
    @PreAuthorize ("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<CalendarioEntity>> getCalendariosByReservaId(@PathVariable Integer idReserva) {
        List<CalendarioEntity> calendarios = calendarioService.findByReservaId(idReserva);
        return new ResponseEntity<>(calendarios, HttpStatus.OK);
    }

    //Obtener calendario por fecha
    @GetMapping("/fecha/{fecha}")
    @PreAuthorize ("hasRole('ROLE_ADMIN') or hasRole('ROLE_ANFITRION') or hasRole('ROLE_HUESPED')")
    public ResponseEntity<?> getCalendarioByFecha(@PathVariable String fecha) {
        Optional<CalendarioEntity> calendario = calendarioService.findByFecha(LocalDate.parse(fecha));
        if (calendario.isPresent()) {
            return ResponseEntity.ok(calendario.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Fecha no encontrada");
        }
    }

    //Guardar calendario
    @PostMapping
    @PreAuthorize ("hasRole('ROLE_ADMIN') or hasRole('ROLE_ANFITRION') or hasRole('ROLE_HUESPED')")
    public ResponseEntity<String> saveCalendario(@RequestBody CalendarioEntity calendario) {
        calendarioService.saveCalendario(calendario);
        return ResponseEntity.status(HttpStatus.CREATED).body("Fecha guardada exitosamente");
    }

    //Eliminar calendario por id
    @DeleteMapping("/{id}")
    @PreAuthorize ("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> deleteCalendarioById(@PathVariable Integer id) {
        calendarioService.deleteCalendarioById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Fecha eliminada exitosamente");
    }

}
