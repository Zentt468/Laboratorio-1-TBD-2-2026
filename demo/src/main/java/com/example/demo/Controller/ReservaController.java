package com.example.demo.Controller;

import com.example.demo.DTO.ReservaDetalleDTO;
import com.example.demo.DTO.ConfirmarReservaRequest;
import com.example.demo.Entity.ReservaEntity;
import com.example.demo.Service.ReservaService;
import com.example.demo.Service.UsuarioService;
import com.example.demo.Entity.UsuarioEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/reservas")
@RequiredArgsConstructor
public class ReservaController {

    private final ReservaService reservaService;
    private final UsuarioService usuarioService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ANFITRION')")
    public ResponseEntity<List<ReservaEntity>> getAllReservas() {
        List<ReservaEntity> reservas = reservaService.getAllReservas();
        return ResponseEntity.ok(reservas);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ANFITRION')")
    public ResponseEntity<ReservaEntity> getReservaById(@PathVariable Integer id) {
        return reservaService.getReservaById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    /* POSIBLE A ELIMINAR
    @GetMapping("/usuario/{idUsuario}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_HUESPED')")
    public ResponseEntity<List<ReservaEntity>> getReservasByUsuarioId(@PathVariable Integer idUsuario) {
        List<ReservaEntity> reservas = reservaService.getReservasByUsuarioId(idUsuario);
        return ResponseEntity.ok(reservas);
    }
    */
    @GetMapping("/alojamiento/{idAlojamiento}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ANFITRION')")
    public ResponseEntity<List<ReservaEntity>> getReservasByAlojamientoId(@PathVariable Integer idAlojamiento) {
        List<ReservaEntity> reservas = reservaService.getReservasByAlojamientoId(idAlojamiento);
        return ResponseEntity.ok(reservas);
    }

    @GetMapping("/exists/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ANFITRION')")
    public ResponseEntity<Boolean> existsReservaById(@PathVariable Integer id) {
        boolean exists = reservaService.existsReserva(id);
        return ResponseEntity.ok(exists);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> saveReserva(@RequestBody ReservaEntity reserva) {
        Optional<UsuarioEntity> usuario = usuarioService.getUsuarioPorId(reserva.getId_usuario());
        if(!usuario.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }

        reservaService.saveReserva(reserva);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ANFITRION')")
    public ResponseEntity<?> updateReserva(@PathVariable Integer id, @RequestBody ReservaEntity reserva) {
        if (!reservaService.existsReserva(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Reserva no encontrada");
        }

        if (reserva.getId_usuario() != null) {
            Optional<UsuarioEntity> usuario = usuarioService.getUsuarioPorId(reserva.getId_usuario());
            if (!usuario.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
            }
        }
        reserva.setId_reserva(id);
        reservaService.updateReserva(reserva);
        return ResponseEntity.ok().build();
    }

    @GetMapping("usuario/{idUsuario}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<ReservaDetalleDTO>> getReservaDetalleByIdUsuario(@PathVariable Integer idUsuario){
        List<ReservaDetalleDTO> reservas = reservaService.getReservaDetalleByUsuarioId(idUsuario);
        return ResponseEntity.ok(reservas);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ANFITRION')")
    public ResponseEntity<?> deleteReserva(@PathVariable Integer id) {
        if (!reservaService.existsReserva(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Reserva no encontrada");
        }

        reservaService.deleteReservaById(id);
        return ResponseEntity.ok().build();
    }

    //Procedimientos almacenados

    @PostMapping("/confirmar")
    @PreAuthorize("hasRole('ROLE_HUESPED')")
    public ResponseEntity<?> confirmarReserva(@RequestBody ConfirmarReservaRequest request, @AuthenticationPrincipal Jwt jwt) {
        request.setIdUsuario(usuarioService.getIdUsuarioActual(jwt));
        Integer idReserva = reservaService.confirmarReserva(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("idReserva", idReserva));
    }

    @PostMapping("/{id}/cancelar")
    @PreAuthorize("hasRole('ROLE_HUESPED')")
    public ResponseEntity<?> cancelarReserva(@PathVariable Integer id,@AuthenticationPrincipal Jwt jwt) {
        Optional<ReservaEntity> reserva = reservaService.getReservaById(id);
        if (reserva.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Reserva no encontrada");
        }
        Integer idUsuario = usuarioService.getIdUsuarioActual(jwt);
        if (!reserva.get().getId_usuario().equals(idUsuario)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Solo puedes cancelar tus propias reservas");
        }
        reservaService.cancelarReserva(id);
        return ResponseEntity.ok(Map.of("mensaje", "Reserva cancelada"));
    }
}