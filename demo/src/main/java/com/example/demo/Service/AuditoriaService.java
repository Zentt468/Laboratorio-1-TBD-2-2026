package com.example.demo.Service;

import com.example.demo.Entity.AuditoriaEntity;
import com.example.demo.Repository.AuditoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service 
@RequiredArgsConstructor
public class AuditoriaService {

    private final AuditoriaRepository auditoriaRepository;

    //Obtener historial de una alojamiento
    public List<AuditoriaEntity> getHistorialByAlojamientoId(Integer idAlojamiento) {
        return auditoriaRepository.findByAlojamientoId(idAlojamiento);
    }

    //Obtener historial por su id
    public Optional<AuditoriaEntity> getHistorialById(Integer idAuditoria) {
        return auditoriaRepository.findById(idAuditoria);
    }
}