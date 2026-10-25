package com.example.demo.Service;

import com.example.demo.Entity.TarifasEntity;
import com.example.demo.Repository.TarifasRepository;
import com.example.demo.Service.AlojamientoService;
import com.example.demo.Entity.AlojamientoEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service 
@RequiredArgsConstructor

public class TarifasService {
    
    private final TarifasRepository tarifasRepository;
    private final AlojamientoService alojamientoService;

    //1. Obtener tarifa por id
    public Optional<TarifasEntity> getTarifaById(Integer id) {
        return tarifasRepository.findById(id);
    }


    //2. Obtener todas las tarifas
    public List<TarifasEntity> getAllTarifas() {
        return tarifasRepository.findAll();
    }

    //3. Obtener tarifas por temporada
    public List<TarifasEntity> getTarifasByTemporada(String temporada) {
        return tarifasRepository.findByTemporada(temporada);
    }

    //4. Obtener tarifas por temporada y alojamiento
    public Optional<TarifasEntity> getTarifasByTemporadaAndAlojamiento(String temporada, Integer alojamiento) {
        return tarifasRepository.findByTemporadaAndAlojamiento(temporada, alojamiento);
    }

    //5. Obtener tarifas entre fechas
    public List<TarifasEntity> getTarifasBetweenFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        return tarifasRepository.findByFechaBetween(fechaInicio, fechaFin);
    }

    //6. Crear una nueva tarifa
    public TarifasEntity createTarifa(TarifasEntity tarifa) {
        return tarifasRepository.save(tarifa);
    }

    //7. Actualizar una tarifa existente
    public TarifasEntity updateTarifa(TarifasEntity tarifa) {
        return tarifasRepository.save(tarifa);
    }

    //8. Eliminar una tarifa por id
    public void deleteTarifa(Integer id){
        tarifasRepository.deleteById(id);
    }

    //9. Obtener tarifas por id de alojamiento
    public List<TarifasEntity> getTarifasByAlojamientoId(Integer idAlojamiento) {
        return tarifasRepository.findByAlojamiento(idAlojamiento);
    }

}