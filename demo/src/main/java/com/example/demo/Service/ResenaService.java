package com.example.demo.Service;

import com.example.demo.DTO.CrearResenaRequest;
import com.example.demo.DTO.DatosReservaDTO;
import com.example.demo.Entity.ResenaEntity;
import com.example.demo.Repository.ResenaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.DTO.ResumenResenaDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
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
    public void crearResena(Integer idUsuario, CrearResenaRequest request) {
        // 1. Obtener datos tipados y validar existencia
        DatosReservaDTO reserva = resenaRepository.findDatosReservaParaResenar(request.getIdReserva())
                .orElseThrow(() -> new IllegalArgumentException("La reserva no existe."));

        // 2. Validar pertenencia con getter
        if (!reserva.getId_usuario().equals(idUsuario)) {
            throw new SecurityException("No puedes reseñar una reserva ajena.");
        }

        // 3. Validar estado completado
        String estado = reserva.getEstado();
        if (!"COMPLETADA".equalsIgnoreCase(estado) && !"FINALIZADA".equalsIgnoreCase(estado)) {
            throw new IllegalStateException("Solo puedes dejar una reseña para estadías completadas.");
        }

        // 4. Validar duplicados
        if (resenaRepository.existsByIdReserva(request.getIdReserva())) {
            throw new IllegalStateException("Esta estadía ya cuenta con una reseña.");
        }

        // 5. Construir y guardar entidad
        ResenaEntity nuevaResena = ResenaEntity.builder()
                .id_alojamiento(reserva.getId_alojamiento())
                .id_usuario(idUsuario)
                .id_reserva(request.getIdReserva())
                .titulo_resena(request.getTituloResena())
                .comentario(request.getComentario())
                .calificacion(request.getCalificacion())
                .fecha_resena(LocalDate.now())
                .estado("PUBLICADA")
                .build();

        resenaRepository.save(nuevaResena);
    }

    //Actualizar reseña
    public void updateResena(ResenaEntity resena) {
        resenaRepository.update(resena);
    }

    //Eliminar reseña por id
    public void deleteResenaById(Integer idResena) {
        resenaRepository.deleteById(idResena);
    }

    // Obtener resumen de reseñas 
    public ResumenResenaDTO getResumenResenasByAlojamientoId(Integer idAlojamiento) {
        //Se obtiene el número promedio
        Double promedio = resenaRepository.getPromedioByAlojamientoId(idAlojamiento);
        
        //Se obtiene la lista de reseñas 
        List<ResenaEntity> historial = resenaRepository.findByAlojamiento(idAlojamiento);
        
        //Se crea y devuelve al DTO 
        return ResumenResenaDTO.builder()
                .promedio(promedio)
                .historial(historial)
                .build();
    }

    public Optional<ResenaEntity> obtenerPorIdReserva(Integer idReserva) {
        return resenaRepository.findByIdReserva(idReserva);
    }
}