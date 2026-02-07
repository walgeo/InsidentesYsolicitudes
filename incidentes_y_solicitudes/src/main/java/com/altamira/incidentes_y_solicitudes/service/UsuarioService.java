package com.altamira.incidentes_y_solicitudes.service;

import com.altamira.incidentes_y_solicitudes.controller.dto.UsuarioDTO;
import com.altamira.incidentes_y_solicitudes.persistence.entity.Usuario;

import java.util.List;

public interface UsuarioService {
    UsuarioDTO crear(UsuarioDTO usuarioDTO);
    UsuarioDTO obtenerPorId(Long id);
    UsuarioDTO obtenerPorUsuario(String usuario);
    List<UsuarioDTO> obtenerTodos();
    UsuarioDTO actualizar(Long id, UsuarioDTO usuarioDTO);
    void eliminar(Long id);
    boolean validarCredenciales(String usuario, String contrasena);
    Usuario obtenerEntidadPorUsuario(String usuario);
}

