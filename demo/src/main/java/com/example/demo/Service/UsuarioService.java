package com.example.demo.Service;

import com.example.demo.Entity.UsuarioEntity;
import com.example.demo.Repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service 
@RequiredArgsConstructor
public class UsuarioService {
     
    private final UsuarioRepository usuarioRepository;

    //1. Obtener usuario por id
    public Optional<UsuarioEntity> getUsuarioPorId(Integer id_usuario) {
        return usuarioRepository.findById(id_usuario);
    }


    //2. Obtener usuario por id de keycloak
    public Optional<UsuarioEntity> getUsuarioPorKeycloakId(String keycloak_id) {
        return usuarioRepository.findByKeycloakId(keycloak_id);
    }

    //3. Buscar por correo
    public Optional<UsuarioEntity> getUsuarioPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }
    //4. Verificar si existe un correo en la base de datos
    public boolean existsCorreo(String correo) {
        return usuarioRepository.existsByCorreo(correo);
    }

    //5. Obtener el usuario del token, vincularlo o crearlo
    public UsuarioEntity obtenerOCrear(Jwt jwt) {
        String keycloakId = jwt.getSubject();
        String correo = jwt.getClaimAsString("email");
        String nombre = jwt.getClaimAsString("name");

        // Caso 1: el usuario ya está vinculado con Keycloak
        UsuarioEntity usuario = usuarioRepository.findByKeycloakId(keycloakId).orElse(null);
        if (usuario != null) {
            return usuario;
        }

        // Caso 2: el usuario existe (por ejemplo, desde data.sql) pero no está vinculado
        usuario = usuarioRepository.findByCorreo(correo).orElse(null);
        if (usuario != null) {
            return vincularUsuario(usuario, keycloakId);
        }

        // Caso 3: el usuario no existe en la base de datos
        return crearUsuario(keycloakId, nombre, correo);
    }

    //6. Guarda el id de Keycloak en un usuario que ya existía
    private UsuarioEntity vincularUsuario(UsuarioEntity usuario, String keycloakId) {
        usuarioRepository.vincularKeycloakId(usuario.getId_usuario(), keycloakId);
        usuario.setKeycloak_id(keycloakId);
        return usuario;
    }

    //7. Crea un usuario nuevo con los datos del token
    private UsuarioEntity crearUsuario(String keycloakId, String nombre, String correo) {
        UsuarioEntity nuevo = new UsuarioEntity();
        nuevo.setKeycloak_id(keycloakId);
        nuevo.setNombre(nombre);
        nuevo.setCorreo(correo);
        return usuarioRepository.save(nuevo);
    }


    //8. Crear un nuevo usuario
    public UsuarioEntity createUsuario(UsuarioEntity usuario) {
        return usuarioRepository.save(usuario);
    }

    //9. Actualizar un usuario existente
    public UsuarioEntity updateUsuario(UsuarioEntity usuario) {
        return usuarioRepository.update(usuario);
    }

    //10. Eliminar un usuario por id
    public void deleteUsuario(Integer id) {
        usuarioRepository.deleteById(id);
    }

    //11. Obtener todos los usuarios
    public List<UsuarioEntity> getTodosUsuarios() {
        return usuarioRepository.findAll();
    }

     //12.Se verifica el Id del usuario que hace una petición
    public Integer getIdUsuarioActual(Jwt jwt) {
        return obtenerOCrear(jwt).getId_usuario();
    }

    //13. Se verifica si el usuario tiene rol de admin en Keycloak
    public boolean esAdmin(Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        if (realmAccess == null || realmAccess.get("roles") == null) {
            return false;
        }
        Collection<?> roles = (Collection<?>) realmAccess.get("roles");
        return roles.contains("ADMIN");
    }


}