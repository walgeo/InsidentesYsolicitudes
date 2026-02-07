package com.altamira.incidentes_y_solicitudes.persistence.dto;

import com.altamira.incidentes_y_solicitudes.validation.ValidationGroups;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstadoDTO {
    private Integer id;

    @NotBlank(groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    @Size(max = 20)
    private String nombre;

    private String creadoPor;
    private LocalDateTime creadoEn;
    private String actualizadoPor;
    private LocalDateTime actualizadoEn;
}