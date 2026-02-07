package com.altamira.incidentes_y_solicitudes.controller;

import com.altamira.incidentes_y_solicitudes.controller.dto.UsuarioDTO;
import com.altamira.incidentes_y_solicitudes.service.UsuarioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/usuarios")
@Validated
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> obtenerTodos() {
        log.info("Obteniendo lista de todos los usuarios");
        List<UsuarioDTO> usuarios = usuarioService.obtenerTodos();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        log.info("Obteniendo usuario con id: {}", id);
        UsuarioDTO usuario = usuarioService.obtenerPorId(id);
        if (usuario != null) {
            return ResponseEntity.ok(usuario);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Usuario no encontrado");
    }

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        log.info("Creando nuevo usuario: {}", usuarioDTO.getUsuario());
        try {
            usuarioDTO.setCreadoPor("ADMIN");
            UsuarioDTO usuarioCreado = usuarioService.crear(usuarioDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioCreado);
        } catch (Exception e) {
            log.error("Error al crear usuario", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody UsuarioDTO usuarioDTO) {
        log.info("Actualizando usuario: {}", id);
        try {
            usuarioDTO.setActualizadoPor("ADMIN");
            UsuarioDTO actualizado = usuarioService.actualizar(id, usuarioDTO);
            return ResponseEntity.ok(actualizado);
        } catch (Exception e) {
            log.error("Error al actualizar usuario", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        log.info("Eliminando usuario: {}", id);
        try {
            usuarioService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error al eliminar usuario", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: " + e.getMessage());
        }
    }
}

