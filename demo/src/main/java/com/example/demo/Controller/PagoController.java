package com.example.demo.Controller;

import com.example.demo.Entity.PagoEntity;
import com.example.demo.Service.PagoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
public class PagoController {

    private final PagoService pagoService;

    //Obtener todos los pagos
    @GetMapping 
    @PreAuthorize ("hasRole('ROLE_ADMIN') or hasRole('ROLE_ANFITRION') or hasRole('ROLE_HUESPED')")
    public ResponseEntity<List<PagoEntity>> getAllPagos() {
        List<PagoEntity> pagos = pagoService.getAllPagos();
        return new ResponseEntity<>(pagos, HttpStatus.OK);
    }

    //Obtener pago por id
    @GetMapping("/{id}")
    @PreAuthorize ("hasRole('ROLE_ADMIN') or hasRole('ROLE_ANFITRION') or hasRole('ROLE_HUESPED')")
    public ResponseEntity<?> getPagoById(@PathVariable Integer id){
        //Se verifica si el pago existe
        Optional<PagoEntity> pago = pagoService.getPagoById(id);
        if(pago.isPresent()){
            return ResponseEntity.ok(pago.get());
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pago no encontrado");
        }
       
    }
    //Obtener pagos por id de reserva
    @GetMapping("/reserva/{idReserva}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ANFITRION')")
    public ResponseEntity<List<PagoEntity>> getPagosByReservaId(@PathVariable Integer idReserva) {
        List<PagoEntity> pagos = pagoService.getPagosByReservaId(idReserva);
        return ResponseEntity.ok(pagos);
    }

    //Guardar pago
    @PostMapping
    @PreAuthorize ("hasRole('ROLE_ADMIN') or hasRole('ROLE_ANFITRION') or hasRole('ROLE_HUESPED')")
    public ResponseEntity<?> savePago(@RequestBody PagoEntity pago){
        pagoService.savePago(pago);
        return ResponseEntity.status(HttpStatus.CREATED).body("Pago creado exitosamente.");
    }

    //Actualizar pago
    @PutMapping
    @PreAuthorize ("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updatePago(@RequestBody PagoEntity pago){
        pagoService.updatePago(pago);
        return ResponseEntity.ok("Pago actualizado exitosamente.");
    }

    //Eliminar pago por id
    @DeleteMapping("/{id}")
    @PreAuthorize ("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deletePagoById(@PathVariable Integer id){
        pagoService.deletePagoById(id);
        return ResponseEntity.ok("Pago eliminado exitosamente.");
    }
}