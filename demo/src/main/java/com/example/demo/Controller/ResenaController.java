package com.example.demo.Controller;

import com.example.demo.DTO.CrearResenaRequest;
import com.example.demo.Entity.ResenaEntity;
import com.example.demo.Service.ResenaService;
import com.example.demo.Entity.AlojamientoEntity;
import com.example.demo.Service.AlojamientoService;
import com.example.demo.Entity.UsuarioEntity;
import com.example.demo.Service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.example.demo.Entity.ReservaEntity;
import com.example.demo.Service.ReservaService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import java.time.LocalDate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/resenas")
@RequiredArgsConstructor
public class ResenaController {

    private final ResenaService resenaService;
    private final AlojamientoService alojamientoService;
    private final UsuarioService usuarioService;
    private final ReservaService reservaService;

    //Obtener todas las resenas
    @GetMapping 
    @PreAuthorize ("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<ResenaEntity>> getAllResenas() {
        List<ResenaEntity> resenas = resenaService.getAllResenas();
        return new ResponseEntity<>(resenas, HttpStatus.OK);
    }
    //Obtener reseña por id
    @GetMapping("/{id}")
    @PreAuthorize ("hasRole('ROLE_ADMIN') or hasRole('ROLE_HUESPED')")
    public ResponseEntity<?> getResenaById(@PathVariable Integer id){
        //Se verifica si la reseña existe
        Optional<ResenaEntity> resena = resenaService.getResenaById(id);
        if(resena.isPresent()){
            return ResponseEntity.ok(resena.get());
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Resena no encontrado");
        }
       
    }

    //Obtener reseñas por id de alojamiento
    @GetMapping("/alojamiento/{idAlojamiento}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getResenasByAlojamientoId(@PathVariable Integer idAlojamiento) {
        //Se verifica si el alojamiento existe
        Optional<AlojamientoEntity> alojamiento = alojamientoService.getAlojamientoById(idAlojamiento);
        if (!alojamiento.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Alojamiento no encontrado");
        }
        //Se verifica si el alojamiento cuenta con reseñas asociadas
        List<ResenaEntity> resenas = resenaService.getResenasByAlojamientoId(idAlojamiento);
        if (resenas.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontraron resenas para el alojamiento con id: " + idAlojamiento);
        }
        return ResponseEntity.ok(resenas);
    }

    //Obtener reseñas por id de usuario
    @GetMapping("/usuario/{idUsuario}")
    @PreAuthorize ("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getResenasByUsuarioId(@PathVariable Integer idUsuario){
        //Se verifica si el usuario existe
        Optional<UsuarioEntity> usuario = usuarioService.getUsuarioPorId(idUsuario);
        if(!usuario.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }
        //Se verifica si el usuario cuenta con reseñas asociadas
        List<ResenaEntity> resenas = resenaService.getResenasByUsuarioId(idUsuario);
        if(resenas.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontraron resenas para el usuario con id: " + idUsuario);
        }
        return ResponseEntity.ok(resenas);
    }
    /*
    //Guardar reseña
       @PostMapping
    @PreAuthorize("hasRole('ROLE_HUESPED')")
    public ResponseEntity<?> saveResena(@RequestBody ResenaEntity resena, @AuthenticationPrincipal Jwt jwt){
        Optional<ReservaEntity> reserva = reservaService.getReservaById(resena.getId_reserva());
        if (reserva.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Reserva no encontrada");
        }
        ReservaEntity r = reserva.get();

        Integer idUsuario = usuarioService.getIdUsuarioActual(jwt);
        if (!r.getId_usuario().equals(idUsuario)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Solo puedes reseñar tus propias estadías");
        }

        boolean completada = "FINALIZADA".equals(r.getEstado_reserva())
                || ("CONFIRMADA".equals(r.getEstado_reserva()) && r.getFecha_fin().isBefore(LocalDate.now()));
        if (!completada) {
            return ResponseEntity.badRequest().body("Solo puedes reseñar estadías completadas");
        }

        resena.setId_usuario(idUsuario);
        resena.setId_alojamiento(r.getId_alojamiento());
        resenaService.saveResena(resena);
        return ResponseEntity.status(HttpStatus.CREATED).body("Resena creada exitosamente.");
    }
    */
    @PutMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateResena(@RequestBody ResenaEntity resena){
        resenaService.updateResena(resena);
        return ResponseEntity.ok("Resena actualizada exitosamente.");
    }

    //Eliminar reseña por id
    @DeleteMapping("/{id}")
    @PreAuthorize ("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteResenaById(@PathVariable Integer id){
        resenaService.deleteResenaById(id);
        return ResponseEntity.ok("Resena eliminada exitosamente.");
    }

    // Endpoint para obtener el promedio y el historial de un alojamiento
    @GetMapping("/alojamiento/{idAlojamiento}/resumen")
    public ResponseEntity<?> getResumenResenas(@PathVariable Integer idAlojamiento) {
        
        // Validamos si el alojamiento realmente existe usando el alojamientoService
        Optional<AlojamientoEntity> alojamiento = alojamientoService.getAlojamientoById(idAlojamiento);
        if (!alojamiento.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Alojamiento no encontrado");
        }
        
        // Llamamos al servicio que nos devuelve el DTO armado y lo enviamos en la respuesta
        return ResponseEntity.ok(resenaService.getResumenResenasByAlojamientoId(idAlojamiento));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_HUESPED')")
    public ResponseEntity<?> crearResena(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody CrearResenaRequest request) {
        try {
            // Sincroniza/obtiene el id_usuario entero de PostgreSQL desde el token de Keycloak
            Integer idUsuario = usuarioService.obtenerOCrear(jwt).getId_usuario();

            resenaService.crearResena(idUsuario, request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("mensaje", "Reseña registrada con éxito"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensaje", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("mensaje", e.getMessage()));
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("mensaje", e.getMessage()));
        }
    }
}