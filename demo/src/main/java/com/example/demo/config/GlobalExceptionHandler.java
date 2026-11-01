package com.example.demo.config;

import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
//Clase para manejar errores de base de datos y devolver un mensaje de error al cliente
@RestControllerAdvice
public class GlobalExceptionHandler {
    //Muestra un mensaje de error al cliente cuando ocurre una excepción de acceso a datos
    @ExceptionHandler(org.springframework.dao.DataAccessException.class)
    public ResponseEntity<Map<String, String>> manejarErrorBD(DataAccessException e) {
        String mensaje = e.getMostSpecificCause().getMessage();
        return ResponseEntity.badRequest().body(Map.of("error", mensaje));
    }
}