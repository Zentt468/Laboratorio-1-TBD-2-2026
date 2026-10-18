package com.example.demo.Service;

import com.example.demo.Entity.PagoEntity;
import com.example.demo.Repository.PagoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service 
@RequiredArgsConstructor
public class PagoService {

    private final PagoRepository pagoRepository;

    //Obtener todos los pagos
    public List<PagoEntity> getAllPagos() {
        return pagoRepository.findAll();
    }

    //Obtener pago por id
    public Optional<PagoEntity> getPagoById(Integer idPago) {
        return pagoRepository.findById(idPago);
    }

    //Obtener pagos por id de reserva
    public List<PagoEntity> getPagosByReservaId(Integer idReserva) {
        return pagoRepository.findByReserva(idReserva);
    }

    //Guardar pago
    public void savePago(PagoEntity pago) {
        pagoRepository.save(pago);
    }

    //Actualizar pago
    public void updatePago(PagoEntity pago) {
        pagoRepository.update(pago);
    }

    //Eliminar pago por id
    public void deletePagoById(Integer idPago) {
        pagoRepository.deleteById(idPago);
    }


}