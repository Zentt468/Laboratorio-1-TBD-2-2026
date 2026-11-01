package com.example.demo.Service;

import com.example.demo.DTO.ReservaDetalleDTO;
import com.example.demo.DTO.ConfirmarReservaRequest;
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
    // Obtener todas las reservas
    public List<ReservaEntity> getAllReservas() {
        return reservaRepository.findAll();
    }
    // Obtener reserva por id
    public Optional<ReservaEntity> getReservaById(Integer id) {
        return reservaRepository.findById(id);
    }
    // Obtener reservas por id de usuario
    public List<ReservaEntity> getReservasByUsuarioId(Integer idUsuario) {
        return reservaRepository.findByUsuario(idUsuario);
    }
    // Obtener reservas por id de alojamiento
    public List<ReservaEntity> getReservasByAlojamientoId(Integer idAlojamiento) {
        return reservaRepository.findByAlojamiento(idAlojamiento);
    }
    // Verificar si existe una reserva por id
    public boolean existsReserva(Integer idReserva) {
        return reservaRepository.existsByReservaId(idReserva);
    }
    // Guardar reserva
    public void saveReserva(ReservaEntity reserva) {
        reservaRepository.save(reserva);
    }
    // Actualizar reserva
    public void updateReserva(ReservaEntity reserva) {
        reservaRepository.update(reserva);
    }
    // Obtiene los detalles de la reserva por id de usuario
    public List<ReservaDetalleDTO> getReservaDetalleByUsuarioId(Integer idUsuario){
        return reservaRepository.findReservaDetalleByIdUsuario(idUsuario);
    }
    // Eliminar reserva por id
    public void deleteReservaById(Integer idReserva) {
        reservaRepository.delete(idReserva);
    }

    //Procedimientos almacenados

    // Se cambió el tipo de retorno de void a Integer
    public Integer confirmarReserva(ConfirmarReservaRequest r) {
    return reservaRepository.confirmarReserva(
        r.getIdUsuario(), r.getIdAlojamiento(),
        r.getFechaInicio(), r.getFechaFin(),
        r.getCantidadHuespedes(), r.getTipoPago());
    }

    // Se cambió el tipo de retorno de void a Integer
    public Integer cancelarReserva(Integer idReserva) {
        return reservaRepository.cancelarReserva(idReserva);
    }
}