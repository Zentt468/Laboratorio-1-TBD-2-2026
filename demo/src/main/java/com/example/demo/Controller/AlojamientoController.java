package com.example.demo.Controller;

import com.example.demo.Entity.AlojamientoEntity;
import com.example.demo.Service.AlojamientoService;
import com.example.demo.Entity.UsuarioEntity;
import com.example.demo.Service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/alojamientos")
@RequiredArgsConstructor
public class AlojamientoController {

    private final AlojamientoService alojamientoService;
    private final UsuarioService usuarioService;

    //Obtener todos los alojamientos
    @GetMapping 
    public ResponseEntity<List<AlojamientoEntity>> getAllAlojamientos() {
        List<AlojamientoEntity> alojamientos = alojamientoService.getAllAlojamientos();
        return new ResponseEntity<>(alojamientos, HttpStatus.OK);
    }

    //Obtener alojamiento por id
    @GetMapping("/{id}")
    public ResponseEntity<?> getAlojamientoById(@PathVariable Integer id){
        //Se verifica si el alojamiento existe
        Optional<AlojamientoEntity> alojamiento = alojamientoService.getAlojamientoById(id);
        if(alojamiento.isPresent()){
            return ResponseEntity.ok(alojamiento.get());
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Alojamiento no encontrado");
        }
       
    }

    //Obtener alojamiento por id de usuario
    @GetMapping("/usuario/{idUsuario}")
    @PreAuthorize ("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getAlojamientosByUsuarioId(@PathVariable Integer idUsuario){
        //Se verifica si el usuario existe
        Optional<UsuarioEntity> usuario = usuarioService.getUsuarioPorId(idUsuario);
        if(!usuario.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }
        //Se verifica si el usuario cuenta con alojamientos asociados
        List<AlojamientoEntity> alojamientos = alojamientoService.getAlojamientosByUsuarioId(idUsuario);
        if(alojamientos.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontraron alojamientos para el usuario con id: " + idUsuario);
        }
        return ResponseEntity.ok(alojamientos);
    }

    //Obtener por tipo de alojamiento
    @GetMapping("/tipo/{tipo}")
    @PreAuthorize ("hasRole('ROLE_ADMIN') or hasRole('ROLE_ANFITRION') or hasRole('ROLE_HUESPED')")
    public ResponseEntity<?> getAlojamientosByTipo(@PathVariable String tipo){
        //Se verifica si el tipo de alojamiento es válido
        List<AlojamientoEntity> alojamientos = alojamientoService.getAlojamientosByTipo(tipo);
        if(alojamientos.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontraron alojamientos del tipo: " + tipo);
        }
        return ResponseEntity.ok(alojamientos);
    }

    //Obtener por capacidad de alojamiento
    @GetMapping("/capacidad/{capacidad}")
    @PreAuthorize ("hasRole('ROLE_ADMIN') or hasRole('ROLE_ANFITRION') or hasRole('ROLE_HUESPED')")
    public ResponseEntity<?> getAlojamientosByCapacidad(@PathVariable Integer capacidad){
        //Se verifica si la capacidad es válida
        if(capacidad <= 0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La capacidad debe ser un número positivo.");
        }
        //Se verifica si existen alojamientos con la capacidad especificada
        List<AlojamientoEntity> alojamientos = alojamientoService.getAlojamientosByCapacidad(capacidad);
        if(alojamientos.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontraron alojamientos con capacidad: " + capacidad);
        }
        return ResponseEntity.ok(alojamientos);
    }

    //Obtener por precio de alojamiento menor
    @GetMapping("/precio/{precio}")
    @PreAuthorize ("hasRole('ROLE_ADMIN') or hasRole('ROLE_ANFITRION') or hasRole('ROLE_HUESPED')")
    public ResponseEntity<?> getAlojamientosByPrecioMenor(@PathVariable Integer precio){
        //Se verifica si el precio es válido
        if(precio <= 0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El precio debe ser un número positivo.");
        }
        //Se verifica si existen alojamientos con precio menor al especificado
        List<AlojamientoEntity> alojamientos = alojamientoService.getAlojamientosByPrecioMenor(precio);
        if(alojamientos.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontraron alojamientos con precio menor a: " + precio);
        }
        return ResponseEntity.ok(alojamientos);
    }

    //Obtener por ciudad de alojamiento
    @GetMapping("/ciudad/{ciudad}")
    @PreAuthorize ("hasRole('ROLE_ADMIN') or hasRole('ROLE_ANFITRION') or hasRole('ROLE_HUESPED')")
    public ResponseEntity<?> getAlojamientosByCiudad(@PathVariable String ciudad){
        //Se verifica si existen alojamientos en la ciudad especificada
        List<AlojamientoEntity> alojamientos = alojamientoService.getAlojamientosByCiudad(ciudad);
        if(alojamientos.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontraron alojamientos en la ciudad: " + ciudad);
        }
        return ResponseEntity.ok(alojamientos);
    }

    //Existe alojamiento por id
    @GetMapping("/exists/{id}")
    @PreAuthorize ("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> existsAlojamientoById(@PathVariable Integer id){
        //Se verifica si el alojamiento existe
        boolean exists = alojamientoService.existsAlojamientoById(id);
        if(exists){
            return ResponseEntity.ok("El alojamiento con id: " + id + " existe.");
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El alojamiento con id: " + id + " no existe.");
        }
    }

    //Guardar alojamiento
     @PostMapping
    @PreAuthorize("hasRole('ROLE_ANFITRION') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> saveAlojamiento(@RequestBody AlojamientoEntity alojamiento,
                                             @AuthenticationPrincipal Jwt jwt){
        if (!usuarioService.esAdmin(jwt)) {
            alojamiento.setId_usuario(usuarioService.getIdUsuarioActual(jwt));
        }
        if (usuarioService.getUsuarioPorId(alojamiento.getId_usuario()).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }
        alojamientoService.saveAlojamiento(alojamiento);
        return ResponseEntity.status(HttpStatus.CREATED).body("Alojamiento creado exitosamente.");
    }

    //Actualizar alojamiento
    @PutMapping
    @PreAuthorize("hasRole('ROLE_ANFITRION') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateAlojamiento(@RequestBody AlojamientoEntity alojamiento,
                                               @AuthenticationPrincipal Jwt jwt){
        Optional<AlojamientoEntity> existente = alojamientoService.getAlojamientoById(alojamiento.getId_alojamiento());
        if (existente.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El alojamiento no existe.");
        }
        if (!esDuenoOAdmin(existente.get(), jwt)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Solo puedes modificar tus propios alojamientos");
        }
        alojamiento.setId_usuario(existente.get().getId_usuario());
        alojamientoService.updateAlojamiento(alojamiento, jwt.getClaimAsString("email"));
        return ResponseEntity.ok("Alojamiento actualizado exitosamente.");
    }
    //Eliminar alojamiento por id
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ANFITRION') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteAlojamientoById(@PathVariable Integer id,
                                                   @AuthenticationPrincipal Jwt jwt){
        Optional<AlojamientoEntity> existente = alojamientoService.getAlojamientoById(id);
        if (existente.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El alojamiento con id: " + id + " no existe.");
        }
        if (!esDuenoOAdmin(existente.get(), jwt)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Solo puedes eliminar tus propios alojamientos");
        }
        alojamientoService.deleteAlojamientoById(id);
        return ResponseEntity.ok("Alojamiento eliminado exitosamente.");
    }

    private boolean esDuenoOAdmin(AlojamientoEntity alojamiento, Jwt jwt) {
        if (usuarioService.esAdmin(jwt)) {
            return true;
        }
        return alojamiento.getId_usuario().equals(usuarioService.getIdUsuarioActual(jwt));
    }

    // Endpoint para buscar alojamientos disponibles
    @GetMapping("/buscar")
    public ResponseEntity<?> buscarAlojamientos(@RequestParam String ciudad, @RequestParam String fechaInicio, @RequestParam String fechaFin,@RequestParam Integer huespedes) {
        //Se buscan los alojamientos que estan disponibles en la ciudad, fechas y cantidad de huespedes especificados
        List<AlojamientoEntity> disponibles = alojamientoService.buscarDisponibles(
                ciudad, 
                java.time.LocalDate.parse(fechaInicio), 
                java.time.LocalDate.parse(fechaFin), 
                huespedes
        );
        //Si la lista es vacia entoces se retorna un mensaje de que no hay alojamientos disponibles
        if(disponibles.isEmpty()){
             return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No hay alojamientos disponibles con esas características.");
        }
        
        return ResponseEntity.ok(disponibles);
    }
}
