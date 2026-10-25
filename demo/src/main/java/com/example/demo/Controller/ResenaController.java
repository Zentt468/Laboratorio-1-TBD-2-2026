package com.example.demo.Controller;

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

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/resenas")
@RequiredArgsConstructor
public class ResenaController {

    private final ResenaService resenaService;
    private final AlojamientoService alojamientoService;
    private final UsuarioService usuarioService;

    //Obtener todas las resenas
    @GetMapping 
    @PreAuthorize ("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<List<ResenaEntity>> getAllResenas() {
        List<ResenaEntity> resenas = resenaService.getAllResenas();
        return new ResponseEntity<>(resenas, HttpStatus.OK);
    }
    //Obtener reseña por id
    @GetMapping("/{id}")
    @PreAuthorize ("hasRole('ADMIN') or hasRole('USER')")
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
    @PreAuthorize("hasRole('ADMIN')")
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
    @PreAuthorize ("hasRole('ADMIN')")
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

    //Guardar reseña
    @PostMapping
    @PreAuthorize ("hasRole('ADMIN')")
    public ResponseEntity<?> saveResena(@RequestBody ResenaEntity resena){
        resenaService.saveResena(resena);
        return ResponseEntity.status(HttpStatus.CREATED).body("Resena creada exitosamente.");
    }

    //Actualizar reseña
    @PutMapping
    @PreAuthorize ("hasRole('ADMIN')")
    public ResponseEntity<?> updateResena(@RequestBody ResenaEntity resena){
        resenaService.updateResena(resena);
        return ResponseEntity.ok("Resena actualizada exitosamente.");
    }

    //Eliminar reseña por id
    @DeleteMapping("/{id}")
    @PreAuthorize ("hasRole('ADMIN')")
    public ResponseEntity<?> deleteResenaById(@PathVariable Integer id){
        resenaService.deleteResenaById(id);
        return ResponseEntity.ok("Resena eliminada exitosamente.");
    }

    // Endpoint para obtener el promedio y el historial de un alojamiento
    @GetMapping("/alojamiento/{idAlojamiento}/resumen")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<?> getResumenResenas(@PathVariable Integer idAlojamiento) {
        
        // Validamos si el alojamiento realmente existe usando el alojamientoService
        Optional<AlojamientoEntity> alojamiento = alojamientoService.getAlojamientoById(idAlojamiento);
        if (!alojamiento.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Alojamiento no encontrado");
        }
        
        // Llamamos al servicio que nos devuelve el DTO armado y lo enviamos en la respuesta
        return ResponseEntity.ok(resenaService.getResumenResenasByAlojamientoId(idAlojamiento));
    }

}