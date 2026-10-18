package com.example.demo.Service;

import com.example.demo.Entity.ReservaEntity;
import com.example.demo.Repository.ReservaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;

    public ReservaService(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
    }

    public List<ReservaEntity> getAllReservas() {
        return reservaRepository.findAll();
    }

    public Optional<ReservaEntity> getReservaById(Integer id) {
        return reservaRepository.findById(id);
    }

    public List<ReservaEntity> getReservasByUsuarioId(Integer idUsuario) {
        return reservaRepository.findByUsuario(idUsuario);
    }

    public List<ReservaEntity> getReservasByAlojamientoId(Integer idAlojamiento) {
        return reservaRepository.findByAlojamiento(idAlojamiento);
    }

    public boolean existsReserva(Integer idReserva) {
        return reservaRepository.existsByReservaId(idReserva);
    }

    public void saveReserva(ReservaEntity reserva) {
        reservaRepository.save(reserva);
    }

    public void updateReserva(ReservaEntity reserva) {
        reservaRepository.save(reserva);
    }

    public void deleteReservaById(Integer idReserva) {
        reservaRepository.delete(idReserva);
    }
}