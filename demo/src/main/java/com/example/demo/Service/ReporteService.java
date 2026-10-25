package com.example.demo.Service;

import com.example.demo.DTO.ReporteOcupacionDTO;
import com.example.demo.Repository.ReporteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service 
@RequiredArgsConstructor
public class ReporteService {
    private final ReporteRepository reporteRepository;

    public List<ReporteOcupacionDTO> obtenerReporte() {
        return reporteRepository.getReporte();
    }

    // Refresca la vista todos los días a las 2:00 AM
    @Scheduled(cron = "0 0 2 * * ?")
    public void refrescarReporteDiario() {
        reporteRepository.refrescarVista();
    }
}