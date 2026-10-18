package com.example.demo.Entity;

import lombok.*;
import java.util.Date;

@Data
@Builder 
@NoArgsConstructor 
@AllArgsConstructor
public class PagoEntity{

    private Integer id_pago; //Llave primaria de pago
    private Integer id_reserva; //Llave foranea de reserva
    private Integer monto_pago;
    private Date fecha_pago;
    private String tipo_pago;
}