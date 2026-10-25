package com.example.demo.DTO;

import com.example.demo.Entity.ResenaEntity;
import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class ResumenResenaDTO {
    private Double promedio;
    private List<ResenaEntity> historial;
}