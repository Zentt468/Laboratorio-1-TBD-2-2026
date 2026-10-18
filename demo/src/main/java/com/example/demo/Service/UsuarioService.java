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

    //2. Buscar por rut
    public Optional<UsuarioEntity> getUsuarioPorRut(String rut) {
        return usuarioRepository.findByRut(rut);
    }

    //3. Buscar por correo
    public Optional<UsuarioEntity> getUsuarioPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }
    //4. Verificar si existe un correo en la base de datos
    public boolean existsCorreo(String correo) {
        return usuarioRepository.existsByCorreo(correo);
    }

    //5. Verificar si existe el rut en la bd
    public boolean existsRut(String rut) {
        return usuarioRepository.existsByRut(rut);
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

    //10. Listar por rol
    public List<UsuarioEntity> getUsuariosPorRol(String rol) {
        return usuarioRepository.findByRol(rol);
    }

    
}