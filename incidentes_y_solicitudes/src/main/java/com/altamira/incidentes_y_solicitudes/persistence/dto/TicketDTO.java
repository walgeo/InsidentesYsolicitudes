package com.altamira.incidentes_y_solicitudes.persistence.dto;

import com.altamira.incidentes_y_solicitudes.validation.ValidationGroups;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketDTO {
    private Long id;

    @NotBlank(groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    @Size(max = 100)
    private String titulo;

    private String descripcion;

    @NotBlank(groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    @Pattern(regexp = "^(incidente|solicitud|INCIDENTE|SOLICITUD)$", message = "tipo debe ser 'incidente' o 'solicitud'")
    private String tipo;

    @NotBlank(groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    @Size(max = 50)
    private String aplicacion;

    @NotNull(groups = ValidationGroups.Create.class)
    private Integer prioridadId;
    private String prioridadNombre;

    @NotNull(groups = ValidationGroups.Create.class)
    private Integer estadoId;
    private String estadoNombre;

    @Size(max = 50)
    private String asignadoA;

    private String creadoPor;
    private LocalDateTime creadoEn;
    private String actualizadoPor;
    private LocalDateTime actualizadoEn;
}