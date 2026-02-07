package com.altamira.incidentes_y_solicitudes.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequestDTO {

    @NotBlank(message = "Usuario es requerido")
    private String usuario;

    @NotBlank(message = "Contrasena es requerida")
    private String contrasena;
}

