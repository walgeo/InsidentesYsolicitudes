package com.altamira.incidentes_y_solicitudes.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioDTO {
    private Long id;

    @NotBlank(message = "Usuario es requerido")
    private String usuario;

    @NotBlank(message = "Nombre es requerido")
    private String nombre;

    @NotBlank(message = "Apellido es requerido")
    private String apellido;

    @Email(message = "Correo debe ser valido")
    @NotBlank(message = "Correo es requerido")
    private String correo;

    private String contrasena;

    private Integer rolId;

    private String rolNombre;

    private Boolean activo;

    private String creadoPor;

    private String actualizadoPor;

    private LocalDateTime creadoEn;

    private LocalDateTime actualizadoEn;
}

