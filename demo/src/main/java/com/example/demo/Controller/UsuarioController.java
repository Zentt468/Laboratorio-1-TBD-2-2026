package com.example.demo.Controller;

import com.example.demo.Entity.UsuarioEntity;
import com.example.demo.Service.UsuarioService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import java.util.Optional;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import java.util.List;
@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {
    
    private final UsuarioService usuarioService;

    //1. Obtener reserva por id
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<UsuarioEntity> obtenerUsuarioPorId(@PathVariable Integer id_usuario){
        Optional<UsuarioEntity> usuario = usuarioService.getUsuarioPorId(id_usuario);
        if(usuario.isPresent()){
            return ResponseEntity.ok(usuario.get());
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    //2. Listar todos los usuarios
    @GetMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UsuarioEntity>> listarUsuarios(){
        return ResponseEntity.ok(usuarioService.getTodosUsuarios());
    }


    //3. Buscar usuario por correo
    @GetMapping("/correo/{correo}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<UsuarioEntity> buscarUsuarioPorCorreo(@PathVariable String correo){
        Optional<UsuarioEntity> usuario = usuarioService.getUsuarioPorCorreo(correo);
        if(usuario.isPresent()){
            return ResponseEntity.ok(usuario.get());

        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    //4. Buscar usuario por RUT
    @GetMapping("/rut/{rut}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<UsuarioEntity> buscarUsuarioPorRut(@PathVariable String rut){
        Optional<UsuarioEntity> usuario = usuarioService.getUsuarioPorRut(rut);
        if(usuario.isPresent()){
            return ResponseEntity.ok(usuario.get());
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    //5. Crear usuario
    @PostMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> crearUsuario(@RequestBody UsuarioEntity usuario){ //El return puede ser de cualquier tipo (entidad o void)
        if(usuarioService.existsCorreo(usuario.getCorreo())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El correo ya está registrado");
        }
        if(usuarioService.existsRut(usuario.getRut())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El RUT ya está registrado");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.createUsuario(usuario));
    }


    //6. Actualizar usuario
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')") 
    public ResponseEntity<?>  actualizarUsuario(@PathVariable Integer id_usuario, @RequestBody UsuarioEntity usuarioActualizado){
            Optional<UsuarioEntity> usuarioExistente = usuarioService.getUsuarioPorId(id_usuario);
            if(usuarioExistente.isPresent()){
                UsuarioEntity usuario = usuarioExistente.get();
                usuario.setRut(usuarioActualizado.getRut());
                usuario.setNombre(usuarioActualizado.getNombre());
                usuario.setCorreo(usuarioActualizado.getCorreo());
                usuario.setContrasena(usuarioActualizado.getContrasena());
                usuario.setRol(usuarioActualizado.getRol());
                usuarioService.updateUsuario(usuario);
                return ResponseEntity.ok("Usuario actualizado correctamente");
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
            }
    }

    //7. Eliminar usuario
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Integer id_usuario){
        if(usuarioService.getUsuarioPorId(id_usuario).isEmpty()){
            return ResponseEntity.notFound().build();
        }else {
            usuarioService.deleteUsuario(id_usuario);
            return ResponseEntity.noContent().build();
        }
    }

    


}