package com.altamira.incidentes_y_solicitudes.service.impl;

import com.altamira.incidentes_y_solicitudes.controller.dto.UsuarioDTO;
import com.altamira.incidentes_y_solicitudes.persistence.entity.Usuario;
import com.altamira.incidentes_y_solicitudes.persistence.entity.Rol;
import com.altamira.incidentes_y_solicitudes.persistence.mapper.UsuarioMapper;
import com.altamira.incidentes_y_solicitudes.persistence.repository.UsuarioRepository;
import com.altamira.incidentes_y_solicitudes.persistence.repository.RolRepository;
import com.altamira.incidentes_y_solicitudes.service.UsuarioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, RolRepository rolRepository,
                            UsuarioMapper usuarioMapper, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.usuarioMapper = usuarioMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UsuarioDTO crear(UsuarioDTO usuarioDTO) {
        log.info("Creando nuevo usuario: {}", usuarioDTO.getUsuario());

        Rol rol = rolRepository.findById(usuarioDTO.getRolId())
                .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado"));

        Usuario usuario = new Usuario();
        usuario.setUsuario(usuarioDTO.getUsuario());
        usuario.setNombre(usuarioDTO.getNombre());
        usuario.setApellido(usuarioDTO.getApellido());
        usuario.setCorreo(usuarioDTO.getCorreo());
        usuario.setContrasena(passwordEncoder.encode(usuarioDTO.getContrasena()));
        usuario.setRol(rol);
        usuario.setActivo(true);
        usuario.setCreadoPor(usuarioDTO.getCreadoPor());

        Usuario guardado = usuarioRepository.save(usuario);
        log.info("Usuario creado con exito: {}", guardado.getId());
        return usuarioMapper.toDto(guardado);
    }

    @Override
    public UsuarioDTO obtenerPorId(Long id) {
        log.info("Obteniendo usuario: {}", id);
        return usuarioRepository.findById(id)
                .map(usuarioMapper::toDto)
                .orElse(null);
    }

    @Override
    public UsuarioDTO obtenerPorUsuario(String usuario) {
        log.info("Buscando usuario: {}", usuario);
        return usuarioRepository.findByUsuario(usuario)
                .map(usuarioMapper::toDto)
                .orElse(null);
    }

    @Override
    public List<UsuarioDTO> obtenerTodos() {
        log.info("Obteniendo todos los usuarios activos");
        return usuarioRepository.findByActivoTrue()
                .stream()
                .map(usuarioMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UsuarioDTO actualizar(Long id, UsuarioDTO usuarioDTO) {
        log.info("Actualizando usuario: {}", id);

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        usuario.setNombre(usuarioDTO.getNombre());
        usuario.setApellido(usuarioDTO.getApellido());
        usuario.setCorreo(usuarioDTO.getCorreo());
        usuario.setActivo(usuarioDTO.getActivo());
        usuario.setActualizadoPor(usuarioDTO.getActualizadoPor());

        if (usuarioDTO.getContrasena() != null && !usuarioDTO.getContrasena().isEmpty()) {
            usuario.setContrasena(passwordEncoder.encode(usuarioDTO.getContrasena()));
        }

        if (usuarioDTO.getRolId() != null) {
            Rol rol = rolRepository.findById(usuarioDTO.getRolId())
                    .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado"));
            usuario.setRol(rol);
        }

        Usuario actualizado = usuarioRepository.save(usuario);
        log.info("Usuario actualizado con exito: {}", id);
        return usuarioMapper.toDto(actualizado);
    }

    @Override
    public void eliminar(Long id) {
        log.info("Eliminando usuario: {}", id);
        usuarioRepository.deleteById(id);
    }

    @Override
    public boolean validarCredenciales(String usuario, String contrasena) {
        log.info("Validando credenciales de usuario: {}", usuario);

        return usuarioRepository.findByUsuario(usuario)
                .map(u -> passwordEncoder.matches(contrasena, u.getContrasena()))
                .orElse(false);
    }

    @Override
    public Usuario obtenerEntidadPorUsuario(String usuario) {
        return usuarioRepository.findByUsuario(usuario).orElse(null);
    }
}

