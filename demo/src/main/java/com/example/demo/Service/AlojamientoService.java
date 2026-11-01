package com.example.demo.Service;

import com.example.demo.Entity.AlojamientoEntity;
import com.example.demo.Repository.AlojamientoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service 
@RequiredArgsConstructor
public class AlojamientoService {

    private final AlojamientoRepository alojamientoRepository;


    //Obtener todos los alojamientos
    public List<AlojamientoEntity> getAllAlojamientos() {
        return alojamientoRepository.findAll();
    }

    //Obtener alojamiento por id
    public Optional<AlojamientoEntity> getAlojamientoById(Integer idAlojamiento) {
        return alojamientoRepository.findById(idAlojamiento);
    }

    //Obtener alojamiento por id de usuario
    public List<AlojamientoEntity> getAlojamientosByUsuarioId(Integer idUsuario) {
        return alojamientoRepository.findByUsuarioId(idUsuario);
    }

    //Obtener por tipo de alojamiento
    public List<AlojamientoEntity> getAlojamientosByTipo(String tipo) {
        return alojamientoRepository.findByTipoAlojamiento(tipo);
    }

    //Obtener por capacidad de alojamiento
    public List<AlojamientoEntity> getAlojamientosByCapacidad(Integer capacidad) {
        return alojamientoRepository.findByCapacidad(capacidad);
    }

    //Obtener por precio de alojamiento menor
    public List<AlojamientoEntity> getAlojamientosByPrecioMenor(Integer precio) {
        return alojamientoRepository.findByPrecioNocheMenor(precio);
    }

    //Obtener por ciudad de alojamiento
    public List<AlojamientoEntity> getAlojamientosByCiudad(String ciudad) {
        return alojamientoRepository.findByCiudad(ciudad);
    }

    //Existe alojamiento por id
    public boolean existsAlojamientoById(Integer idAlojamiento) {
        return alojamientoRepository.existsById(idAlojamiento);
    }

    //Guardar alojamiento
    public void saveAlojamiento(AlojamientoEntity alojamiento) {
        alojamientoRepository.save(alojamiento);
    }

    //Actualizar alojamiento
   @Transactional
    public void updateAlojamiento(AlojamientoEntity alojamiento, String usuario) {
        alojamientoRepository.update(alojamiento, usuario);
    }

    //Eliminar alojamiento por id
    public void deleteAlojamientoById(Integer idAlojamiento) {
        alojamientoRepository.deleteById(idAlojamiento);
    }

    // Buscar alojamientos disponibles
    public List<AlojamientoEntity> buscarDisponibles(String ciudad, java.time.LocalDate fechaInicio, java.time.LocalDate fechaFin, Integer huespedes) {
        return alojamientoRepository.findDisponibles(ciudad, fechaInicio, fechaFin, huespedes);
    }


}