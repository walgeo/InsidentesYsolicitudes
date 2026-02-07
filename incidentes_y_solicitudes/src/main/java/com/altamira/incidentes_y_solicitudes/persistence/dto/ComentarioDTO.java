package com.altamira.incidentes_y_solicitudes.persistence.dto;

import com.altamira.incidentes_y_solicitudes.validation.ValidationGroups;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComentarioDTO {
    private Long id;

    @NotNull(groups = ValidationGroups.Create.class)
    private Long ticketId;

    @NotBlank(groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    private String mensaje;

    private String creadoPor;
    private LocalDateTime creadoEn;
    private String actualizadoPor;
    private LocalDateTime actualizadoEn;
}