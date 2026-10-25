package com.example.demo.Service;

import com.example.demo.Entity.UsuarioEntity;
import com.example.demo.Repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service 
@RequiredArgsConstructor
public class UsuarioService {
    @Autowired 
    private final UsuarioRepository usuarioRepository;

    //1. Obtener usuario por id
    public Optional<UsuarioEntity> getUsuarioPorId(Integer id_usuario) {
        return usuarioRepository.findById(id_usuario);
    }


    //2. Obtener usuario por id de keycloak
    public Optional<UsuarioEntity> getUsuarioPorKeycloakId(Integer keycloak_id) {
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



    //6. Crear un nuevo usuario
    public UsuarioEntity createUsuario(UsuarioEntity usuario) {
        return usuarioRepository.save(usuario);
    }

    //7. Actualizar un usuario existente
    public UsuarioEntity updateUsuario(UsuarioEntity usuario) {
        return usuarioRepository.update(usuario);
    }

    //8. Eliminar un usuario por id
    public void deleteUsuario(Integer id) {
        usuarioRepository.deleteById(id);
    }

    //9. Obtener todos los usuarios
    public List<UsuarioEntity> getTodosUsuarios() {
        return usuarioRepository.findAll();
    }


    
}