package com.example.demo.Entity;

import lombok.*;

@Data
@Builder 
@NoArgsConstructor 
@AllArgsConstructor
public class UsuarioEntity{
    private Integer id_usuario; //Llave primaria de Usuario
    private Integer keycloak_id;
    private String nombre;
    private String correo;
    
    
    
}