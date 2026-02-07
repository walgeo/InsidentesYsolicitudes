package com.altamira.incidentes_y_solicitudes.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RolDTO {
    private Integer id;
    private String nombre;
    private String descripcion;
    private String creadoPor;
    private String actualizadoPor;
    private LocalDateTime creadoEn;
    private LocalDateTime actualizadoEn;
}

