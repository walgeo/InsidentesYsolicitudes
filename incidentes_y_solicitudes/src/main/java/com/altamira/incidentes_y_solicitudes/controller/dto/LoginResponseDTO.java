package com.altamira.incidentes_y_solicitudes.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponseDTO {
    private String token;
    private String usuario;
    private String nombre;
    private String apellido;
    private String rol;
    private Long usuarioId;
}

