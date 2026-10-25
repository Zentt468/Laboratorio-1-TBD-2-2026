package com.example.demo.Service;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.example.demo.Entity.CalendarioEntity;
import com.example.demo.Repository.CalendarioRepository;
import java.util.Optional;
import java.time.LocalDate;
import java.util.List;


@Service 
@RequiredArgsConstructor
public class CalendarioService {

    private final CalendarioRepository calendarioRepository;

    //Obtener todos los calendarios
    public List<CalendarioEntity> findAll() {
        return calendarioRepository.findAll();
    }

    //Obtener calendario por id
    public Optional<CalendarioEntity> findById(Integer idCalendario) {
        return calendarioRepository.findById(idCalendario);
    }

    //Obtener calendario por id de reserva
    public List<CalendarioEntity> findByReservaId(Integer idReserva) {
        return calendarioRepository.findByReservaId(idReserva);
    }

    //Obtener calendario por fecha
    public Optional<CalendarioEntity> findByFecha(LocalDate fecha) {
        return calendarioRepository.findByFecha(fecha);
    }

    //guardar calendario
    public void saveCalendario(CalendarioEntity calendario) {
        calendarioRepository.save(calendario);
    }

    //Eliminar calendario por id
    public void deleteCalendarioById(Integer idCalendario) {
        calendarioRepository.deleteById(idCalendario);
    }



}