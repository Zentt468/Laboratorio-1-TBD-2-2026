package com.example.demo.Service;

import com.example.demo.Entity.ResenaEntity;
import com.example.demo.Repository.ResenaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service 
@RequiredArgsConstructor
public class ResenaService {

    private final ResenaRepository resenaRepository;

    //Obtener todas las reseñas
    public List<ResenaEntity> getAllResenas() {
        return resenaRepository.findAll();
    }

    //Obtener reseña por id
    public Optional<ResenaEntity> getResenaById(Integer idResena) {
        return resenaRepository.findById(idResena);
    }

    //Obtener reseñas por id de usuario
    public List<ResenaEntity> getResenasByUsuarioId(Integer idUsuario) {
        return resenaRepository.findByUsuario(idUsuario);
    }

    //Obtener reseñas por id de alojamiento
    public List<ResenaEntity> getResenasByAlojamientoId(Integer idAlojamiento) {
        return resenaRepository.findByAlojamiento(idAlojamiento);
    }

    //Guardar reseña
    public void saveResena(ResenaEntity resena) {
        resenaRepository.save(resena);
    }

    //Actualizar reseña
    public void updateResena(ResenaEntity resena) {
        resenaRepository.update(resena);
    }

    //Eliminar reseña por id
    public void deleteResenaById(Integer idResena) {
        resenaRepository.deleteById(idResena);
    }
}