package com.example.demo.config;

import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(org.springframework.dao.DataAccessException.class)
    public ResponseEntity<Map<String, String>> manejarErrorBD(DataAccessException e) {
        String mensaje = e.getMostSpecificCause().getMessage();
        return ResponseEntity.badRequest().body(Map.of("error", mensaje));
    }
}