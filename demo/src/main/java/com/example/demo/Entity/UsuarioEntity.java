package com.example.demo.Entity;

import lombok.*;

@Data
@Builder 
@NoArgsConstructor 
@AllArgsConstructor
public class UsuarioEntity{
    private Integer id_usuario; //Llave primaria de Usuario
    private String rut;
    private String nombre;
    private String correo;
    private String contrasena;
    private String rol;
    
}