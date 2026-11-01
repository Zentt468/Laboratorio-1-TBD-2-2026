package com.example.demo.DTO;

import com.example.demo.Entity.UsuarioEntity;
import lombok.Data;
import lombok.Builder;

@Data 
@Builder 
public class UsuarioDTO {
    private Integer id_usuario; //Llave primaria de Usuario
    private Integer keycloak_id;
    private String nombre;
    private String correo;    
}
